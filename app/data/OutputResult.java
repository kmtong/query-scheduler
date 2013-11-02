package data;

import java.io.InputStream;

public class OutputResult {

	final String name;
	final String contentType;
	final InputStream content;
	final boolean attachment;

	public OutputResult(String name, String contentType, InputStream content,
			boolean attachment) {
		this.name = name;
		this.contentType = contentType;
		this.content = content;
		this.attachment = attachment;
	}

	public String getName() {
		return name;
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

}
