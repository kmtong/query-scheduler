package services.routes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import models.QueryJob;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.StringUtils;

import services.PlayConfigurationProvider;
import services.bean.MailOutputProcessor;
import services.bean.QueryJobExecution;

public class QueryJobRouteBuilder extends RouteBuilder {

	final QueryJob job;
	final PlayConfigurationProvider configProvider;

	public QueryJobRouteBuilder(PlayConfigurationProvider configProvider,
			QueryJob job) {
		this.job = job;
		this.configProvider = configProvider;
	}

	public static String getJobTriggerID(QueryJob job) {
		return "QueryJob-" + job.getId() + "-Trigger";
	}

	public static String getJobProcessID(QueryJob job) {
		return "QueryJob-" + job.getId() + "-Process";
	}

	public static String getProcessEndpoint(QueryJob job) {
		return "direct:QueryJob-" + job.getId();
	}

	public String getJobTriggerID() {
		return getJobTriggerID(job);
	}

	public String getJobProcessID() {
		return getJobProcessID(job);
	}

	public String getProcessEndpoint() {
		return getProcessEndpoint(job);
	}

	@Override
	public void configure() throws Exception {

		MailOutputProcessor mailoutProcess = new MailOutputProcessor(job);

		String fromUri = "quartz://QueryJob/" + job.getId() + "?cron="
				+ encode(job.getCron());

		String mailProtocol = configProvider.getSmtpSSL() ? "smtps" : "smtp";

		String port = configProvider.getSmtpPort() != null ? ":"
				+ configProvider.getSmtpPort().toString() : "";

		List<String> parameters = new LinkedList<String>();

		if (configProvider.getSmtpUsername() != null) {
			parameters.add("username="
					+ encode(configProvider.getSmtpUsername()));
		}
		if (configProvider.getSmtpPassword() != null) {
			parameters.add("password="
					+ encode(configProvider.getSmtpPassword()));
		}
		if (configProvider.getFrom() != null) {
			parameters.add("from=" + encode(configProvider.getFrom()));
		}

		// mail subject
		parameters.add("subject=" + encode(job.getMailSubject()));

		// XXX comma-separated processing
		// To recipients
		parameters.add("to=" + encode(job.getRecipients()));
		parameters.add("contentType=" + mailoutProcess.getContentType());

		String params = StringUtils.join(parameters, '&');
		String mailUri = mailProtocol + "://" + configProvider.getSmtpHost()
				+ port + "?" + params;

		// part 1: scheduling
		from(fromUri).id(getJobTriggerID()).to(getProcessEndpoint());

		// part 2: query execution
		from(getProcessEndpoint()).id(getJobProcessID())
				// completion hook registration
				.onCompletion().to(TrackerRouteBuilder.getTrackerEndEndpoint())
				.end()
				// begin execution
				.to(TrackerRouteBuilder.getTrackerBeginEndpoint())
				// timestamp logging
				.setHeader("timestamp").method(this, "getTimestamp")
				// execute
				.bean(new QueryJobExecution(job), "execute")
				// part 3: result transformation
				.process(mailoutProcess)
				// part 4: mail result
				.to(mailUri)
				// log history
				.to(JobLogRouteBuilder.getJobLogEndpoint());
	}

	public Date getTimestamp() {
		return new Date();
	}

	protected String encode(String param) {
		try {
			return URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
