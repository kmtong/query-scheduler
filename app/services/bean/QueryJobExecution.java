package services.bean;

import inject.InjectorFactory;
import models.QueryJob;
import services.QueryJobService;
import data.QueryResult;

public class QueryJobExecution {

	final QueryJob queryJob;

	public QueryJobExecution(QueryJob job) {
		this.queryJob = job;
	}

	public QueryResult execute() throws Exception {
		QueryJobService service = InjectorFactory.getInstance().getInstance(
				QueryJobService.class);
		return service.executeQuery(queryJob);
	}
}
