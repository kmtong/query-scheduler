package forms;

import javax.inject.Inject;

import services.ConnectionService;

public class QueryJobFormAdapter implements Adapter<QueryJob, models.QueryJob> {

	final ConnectionService connService;

	@Inject
	public QueryJobFormAdapter(ConnectionService connService) {
		this.connService = connService;
	}

	@Override
	public QueryJob toForm(models.QueryJob entity) {
		QueryJob form = new QueryJob();
		form.connectionId = entity.getConnection() != null ? entity
				.getConnection().getId() : null;
		form.name = entity.getName();
		form.cron = entity.getCron();
		form.query = entity.getQuery();
		form.recipients = entity.getRecipients();
		form.mailSubject = entity.getMailSubject();
		form.outputFormat = entity.getOutputFormat();
		form.template = entity.getTemplate();
		return form;
	}

	@Override
	public models.QueryJob toNewEntity(QueryJob form) throws Exception {
		return toEntity(form, new models.QueryJob());
	}

	@Override
	public models.QueryJob toEntity(QueryJob form, models.QueryJob entity)
			throws Exception {
		entity.setCron(form.cron);
		entity.setName(form.name);
		entity.setOutputFormat(form.outputFormat);
		entity.setQuery(form.query);
		entity.setRecipients(form.recipients);
		entity.setMailSubject(form.mailSubject);
		entity.setTemplate(form.template);
		entity.setConnection(findConnectionById(form.connectionId));
		return entity;
	}

	protected models.DBConnection findConnectionById(Long connectionId) {
		return connService.getConnectionById(connectionId);
	}

}
