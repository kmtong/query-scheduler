package services.bean;

import models.QueryJob;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import data.QueryResult;

public class MailOutputProcessor implements Processor {

	final QueryJob queryJob;

	public MailOutputProcessor(QueryJob job) {
		this.queryJob = job;
	}

	@Override
	public void process(Exchange xchg) throws Exception {
		QueryResult result = (QueryResult) xchg.getIn().getBody();

		// XXX hard-code to use HTML as output format
		HtmlResultConverter converter = new HtmlResultConverter();
		xchg.getIn().setBody(converter.getResult(result));
	}

	public String getContentType() {
		// XXX hard-code to use HTML as output format
		return "text/html";
	}
}
