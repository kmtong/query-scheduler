package services.bean;

import models.QueryJob;
import services.QueryJobService;
import data.QueryResult;

public class QueryJobExecution {

	final QueryJob queryJob;
	final QueryJobService service;

	public QueryJobExecution(QueryJobService service, QueryJob job) {
		this.service = service;
		this.queryJob = job;
	}

	public QueryResult execute() throws Exception {
		return service.executeQuery(queryJob);
	}
}
