package services.bean;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;

import models.QueryJob;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import data.OutputContext;
import data.OutputResult;
import data.QueryResult;

public class MailOutputProcessor implements Processor {

	final QueryJob queryJob;
	final IConverter converter;

	public MailOutputProcessor(QueryJob job) {
		this.queryJob = job;
		if ("html".equals(job.getOutputFormat())) {
			// html output
			converter = new HtmlResultConverter();
		} else {
			// excel output
			converter = new ExcelResultConverter();
		}
	}

	@Override
	public void process(Exchange xchg) throws Exception {
		QueryResult result = (QueryResult) xchg.getIn().getBody();
		OutputContext context = new OutputContext(queryJob);
		OutputResult output = converter.extractResult(context, result);
		// attachment processing
		if (output.isAttachment()) {
			xchg.getIn().addAttachment(
					output.getName(),
					new DataHandler(new ByteArrayDataSource(
							output.getContent(), output.getContentType())));
		}
		// render body by context
		xchg.getIn().setBody(context.composeBody(output));
	}

	public String getContentType() {
		// XXX hard-code to use HTML as output format
		return "text/html";
	}
}
