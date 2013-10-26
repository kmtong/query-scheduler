package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class QueryJob {

	@Id
	long id;

	String name;

	String cron;
	String receipients;

	@ManyToOne
	DBConnection connection;

	@Lob
	String query;
	String outputFormat;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getReceipients() {
		return receipients;
	}

	public void setReceipients(String receipients) {
		this.receipients = receipients;
	}

	public DBConnection getConnection() {
		return connection;
	}

	public void setConnection(DBConnection connection) {
		this.connection = connection;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

}
