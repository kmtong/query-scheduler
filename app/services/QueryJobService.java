package services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.inject.Inject;

import models.QueryJob;
import play.Logger;
import services.bean.HtmlResultConverter;
import services.bean.IConverter;

import com.avaje.ebean.Ebean;

import data.OutputContext;
import data.OutputResult;
import data.QueryResult;

public class QueryJobService {

	final ConnectionService connService;

	@Inject
	public QueryJobService(ConnectionService connService) {
		this.connService = connService;
	}

	public List<QueryJob> findAll() {
		return Ebean.find(QueryJob.class).findList();
	}

	public QueryJob getQueryJobById(Long jobId) {
		return Ebean.find(QueryJob.class, jobId);
	}

	public void save(QueryJob jobEntity) {
		Ebean.save(jobEntity);
	}

	public String test(Long jobId) throws Exception {
		Logger.info("Test Execution of Job: " + jobId);
		QueryJob job = getQueryJobById(jobId);
		QueryResult result = executeQuery(job);
		OutputContext context = new OutputContext(null /* for testing */);
		IConverter c = new HtmlResultConverter();
		OutputResult r = c.extractResult(context, result);
		return context.composeBody(r);
	}

	// XXX add batching support for multiple results
	public QueryResult executeQuery(QueryJob job) throws Exception {
		Connection conn = connService.getConnection(job.getConnection());
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(job.getQuery());
			return new QueryResult(job.getName(), rs);
		} finally {
			conn.close();
		}
	}

}
