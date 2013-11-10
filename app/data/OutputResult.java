package data;

import java.io.InputStream;

public class OutputResult {

	final String contentType;
	final InputStream content;
	final boolean attachment;
	final QueryResult result;

	public OutputResult(QueryResult result, String contentType,
			InputStream content, boolean attachment) {
		this.result = result;
		this.contentType = contentType;
		this.content = content;
		this.attachment = attachment;
	}

	public String getName() {
		return result.getName();
	}

	public String getContentType() {
		return contentType;
	}

	public InputStream getContent() {
		return content;
	}

	public boolean isAttachment() {
		return attachment;
	}

	public QueryResult getResult() {
		return result;
	}

}
