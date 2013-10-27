package services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.inject.Inject;

import play.Logger;

import models.QueryJob;

import com.avaje.ebean.Ebean;

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

	public void test(Long jobId) throws Exception {
		Logger.info("Test Execution of Job: " + jobId);
		QueryResult result = executeQuery(getQueryJobById(jobId));
		ResultSet rs = result.getResult();
		while (rs.next()) {
			Logger.info("Column 1: " + rs.getObject(1));
		}
		result.close();
	}

	// XXX add batching support for multiple results
	protected QueryResult executeQuery(QueryJob job) throws Exception {
		Connection conn = connService.getConnection(job.getConnection());
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(job.getQuery());
		return new QueryResult(conn, stmt, rs);
	}
}
