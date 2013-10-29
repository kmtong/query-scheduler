package services;

import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import models.QueryJob;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.StringUtils;

import play.Logger;
import services.bean.QueryJobExecution;
import services.bean.ResultTransformation;

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
		Logger.info("Clear Job: " + id);
		camel.stopRoute(id);
		camel.removeRoute(id);
	}

	public void setupJob(QueryJob job) throws Exception {
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
				String id = getJobKey(job);
				String fromUri = "quartz://QueryJob/" + job.getId() + "?cron="
						+ URLEncoder.encode(job.getCron(), "UTF-8");

				String mailProtocol = configProvider.getSmtpSSL() ? "smtps"
						: "smtp";

				String port = configProvider.getSmtpPort() != null ? ":"
						+ configProvider.getSmtpPort().toString() : "";

				List<String> parameters = new LinkedList<String>();

				if (configProvider.getSmtpUsername() != null) {
					parameters.add("username="
							+ URLEncoder.encode(
									configProvider.getSmtpUsername(), "UTF-8"));
				}
				if (configProvider.getSmtpPassword() != null) {
					parameters.add("password="
							+ URLEncoder.encode(
									configProvider.getSmtpPassword(), "UTF-8"));
				}
				
				// XXX comma-separated processing
				// To recipients
				parameters.add("to="
						+ URLEncoder.encode(job.getRecipients(), "UTF-8"));
				
				String params = StringUtils.join(parameters, '&');
				String mailUri = mailProtocol + "://"
						+ configProvider.getSmtpHost() + port + "?" + params;

				// part 1: scheduling
				from(fromUri).id(id)
						// part 2: query execution
						.bean(new QueryJobExecution(jobService, job), "execute")
						// part 3: result transformation
						.bean(new ResultTransformation(job), "transform")
						// part 4: mail result
						.to(mailUri)
						// log to see it
						.to("log:" + id);
			}
		};
		return builder;
	}

	protected String getJobKey(QueryJob job) {
		return "QueryJob-" + job.getId();
	}

}
