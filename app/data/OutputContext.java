package data;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import models.QueryJob;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import play.Logger;
import services.bean.MailOutputProcessor;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

/**
 * <p>
 * OutputContext uses mustache template engine for mail output rendering.
 * 
 * <p>
 * An email can have 2 types of components: Body and Attachment. Body is assumed
 * to be an HTML or text content, while attachment can be an Excel file.
 * 
 * <p>
 * For IConverter can generate different <code>OutputResult</code> based on
 * requirement, and the <code>OutputResult</code> can affect how the mail is
 * generated.
 * 
 * <ul>
 * <li>For Attachment-Typed <code>OutputResult</code>, it would be attached
 * separately during output processing.
 * 
 * <li>For Embedded-Typed <code>OutputResult</code>, it is rendered here as the
 * body part as Mustache context variable "body".
 * </ul>
 * 
 * <p>
 * All attributes defined in this <code>OutputContext</code> will be rendered
 * here as Mustache variable "header".
 * 
 * @author KM Tong
 * @see MailOutputProcessor
 */
public class OutputContext {

	final static String DEFAULT_TEMPLATE = "{{{header}}}{{{body}}}";

	Map<String, Object> attributes = new HashMap<String, Object>();
	Mustache mustache;

	public OutputContext(QueryJob job) {
		// setup template for body generation
		MustacheFactory mf = new DefaultMustacheFactory();
		String templateContent = (job == null || StringUtils.isEmpty(job
				.getTemplate())) ? DEFAULT_TEMPLATE : job.getTemplate();
		mustache = mf
				.compile(new StringReader(templateContent), "mailtemplate");
	}

	public boolean exists(String key) {
		return attributes.containsKey(key);
	}

	public Object get(String key) {
		return attributes.get(key);
	}

	public void put(String key, Object value) {
		attributes.put(key, value);
	}

	public String composeBody(OutputResult output) throws Exception {

		HashMap<String, Object> scopes = new HashMap<String, Object>();

		// header result
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Object> e : attributes.entrySet()) {
			sb.append(e.getValue());
		}
		scopes.put("header", sb.toString());

		// merge body template with output result
		if (!output.isAttachment()) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			IOUtils.copy(output.getContent(), os);
			scopes.put("body", os.toString("UTF-8"));
		}

		StringWriter writer = new StringWriter();
		mustache.execute(writer, scopes);
		writer.flush();

		Logger.info("Mustache Output: " + writer.toString());
		return writer.toString();
	}
}
