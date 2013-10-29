package services.bean;

import models.QueryJob;
import play.Logger;
import data.QueryResult;

public class ResultTransformation {

	final QueryJob queryJob;

	public ResultTransformation(QueryJob job) {
		this.queryJob = job;
	}

	public Object transform(QueryResult result) {
		Logger.info("Result: " + result.getResult());
		return result;
	}
}
