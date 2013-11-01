package services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import models.QueryJob;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.StringUtils;

import services.bean.MailOutputProcessor;
import services.bean.QueryJobExecution;

public class SchedulerService {

	final CamelContext camel;
	final QueryJobService jobService;
	final PlayConfigurationProvider configProvider;

	@Inject
	public SchedulerService(CamelContext camel, QueryJobService jobService,
			PlayConfigurationProvider configProvider) {
		this.camel = camel;
		this.jobService = jobService;
		this.configProvider = configProvider;
	}

	public void restoreStates() throws Exception {
		clearStates();
		// query all active jobs and build camel routes
		for (QueryJob job : jobService.findAll()) {
			setupJob(job);
		}
		camel.start();
	}

	public void clearStates() throws Exception {
		for (Route r : camel.getRoutes()) {
			camel.stopRoute(r.getId());
			camel.removeRoute(r.getId());
		}
		camel.stop();
	}

	public void clearJob(QueryJob job) throws Exception {
		String id = getJobKey(job);
		camel.stopRoute(id);
		camel.removeRoute(id);
	}

	public void setupJob(QueryJob job) throws Exception {
		clearJob(job);
		camel.addRoutes(createRouteBuilder(job));
	}

	/**
	 * Create a RouteBuilder for a Query Job and utilize Camel-Quartz components
	 * for scheduling
	 */
	protected RouteBuilder createRouteBuilder(final QueryJob job) {
		RouteBuilder builder = new RouteBuilder() {
			@Override
			public void configure() throws Exception {

				MailOutputProcessor mailoutProcess = new MailOutputProcessor(
						job);

				String id = getJobKey(job);
				String fromUri = "quartz://QueryJob/" + job.getId() + "?cron="
						+ encode(job.getCron());

				String mailProtocol = configProvider.getSmtpSSL() ? "smtps"
						: "smtp";

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

				// mail subject
				parameters.add("subject=" + encode(job.getMailSubject()));

				// XXX comma-separated processing
				// To recipients
				parameters.add("to=" + encode(job.getRecipients()));
				parameters
						.add("contentType=" + mailoutProcess.getContentType());

				String params = StringUtils.join(parameters, '&');
				String mailUri = mailProtocol + "://"
						+ configProvider.getSmtpHost() + port + "?" + params;

				// part 1: scheduling
				from(fromUri).id(id)
						// part 2: query execution
						.bean(new QueryJobExecution(jobService, job), "execute")
						// part 3: result transformation
						.process(mailoutProcess)
						// part 4: mail result
						.to(mailUri)
						// log to see it
						.to("log:" + id);
			}

		};
		return builder;
	}

	protected String encode(String param) {
		try {
			return URLEncoder.encode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	protected String getJobKey(QueryJob job) {
		return "QueryJob-" + job.getId();
	}

}
