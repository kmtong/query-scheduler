package services.bean;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;

import models.QueryJob;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class MailOutputProcessor implements Processor {

	final QueryJob queryJob;

	public MailOutputProcessor(QueryJob job) {
		this.queryJob = job;
	}

	@Override
	public void process(Exchange xchg) throws Exception {
		xchg.getIn().addAttachment(
				"attachment.txt",
				new DataHandler(new ByteArrayDataSource("Hello World"
						.getBytes(), "text/plain")));
	}

}
