package data;

import models.QueryJob;

public class QueryJobChange {

	public static enum Change {
		INSERT, UPDATE, DELETE
	}

	private final QueryJob job;
	private final Change change;

	public QueryJobChange(QueryJob job, Change change) {
		this.job = job;
		this.change = change;
	}

	public QueryJob getJob() {
		return job;
	}

	public Change getChange() {
		return change;
	}

}
