package forms;

import play.data.validation.Constraints.Required;

public class QueryJob {

	@Required
	public String name;

	@Required
	public String cron;

	@Required
	public String recipients;

	@Required
	public String mailSubject;

	@Required
	public Long connectionId;

	@Required
	public String query;

	public String template;

	@Required
	public String outputFormat;
}
