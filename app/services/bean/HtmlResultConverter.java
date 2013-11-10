package services.bean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringEscapeUtils;

import data.OutputContext;
import data.OutputResult;
import data.QueryResult;
import data.Row;

public class HtmlResultConverter implements IConverter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.bean.IConverter#extractResult(data.QueryResult,
	 * org.apache.camel.Message)
	 */
	@Override
	public OutputResult extractResult(OutputContext context, QueryResult result) {

		if (!context.exists("style")) {
			StringBuffer style = new StringBuffer();
			style.append("<style>\n");
			style.append("html{font-family:sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%}");
			style.append("body{margin:0}");
			style.append("table{border-collapse:collapse;border-spacing:0}");
			style.append("table{max-width:100%;background-color:transparent}th{text-align:left}.table{width:100%;margin-bottom:20px}.table thead>tr>th,.table tbody>tr>th,.table tfoot>tr>th,.table thead>tr>td,.table tbody>tr>td,.table tfoot>tr>td{padding:8px;line-height:1.428571429;vertical-align:top;border-top:1px solid #ddd}.table thead>tr>th{vertical-align:bottom;border-bottom:2px solid #ddd}.table caption+thead tr:first-child th,.table colgroup+thead tr:first-child th,.table thead:first-child tr:first-child th,.table caption+thead tr:first-child td,.table colgroup+thead tr:first-child td,.table thead:first-child tr:first-child td{border-top:0}.table tbody+tbody{border-top:2px solid #ddd}.table .table{background-color:#fff}.table-condensed thead>tr>th,.table-condensed tbody>tr>th,.table-condensed tfoot>tr>th,.table-condensed thead>tr>td,.table-condensed tbody>tr>td,.table-condensed tfoot>tr>td{padding:5px}.table-bordered{border:1px solid #ddd}.table-bordered>thead>tr>th,.table-bordered>tbody>tr>th,.table-bordered>tfoot>tr>th,.table-bordered>thead>tr>td,.table-bordered>tbody>tr>td,.table-bordered>tfoot>tr>td{border:1px solid #ddd}.table-bordered>thead>tr>th,.table-bordered>thead>tr>td{border-bottom-width:2px}.table-striped>tbody>tr:nth-child(odd)>td,.table-striped>tbody>tr:nth-child(odd)>th{background-color:#f9f9f9}.table-hover>tbody>tr:hover>td,.table-hover>tbody>tr:hover>th{background-color:#f5f5f5}table col[class*=\"col-\"]{display:table-column;float:none}table td[class*=\"col-\"],table th[class*=\"col-\"]{display:table-cell;float:none}.table>thead>tr>td.active,.table>tbody>tr>td.active,.table>tfoot>tr>td.active,.table>thead>tr>th.active,.table>tbody>tr>th.active,.table>tfoot>tr>th.active,.table>thead>tr.active>td,.table>tbody>tr.active>td,.table>tfoot>tr.active>td,.table>thead>tr.active>th,.table>tbody>tr.active>th,.table>tfoot>tr.active>th{background-color:#f5f5f5}.table>thead>tr>td.success,.table>tbody>tr>td.success,.table>tfoot>tr>td.success,.table>thead>tr>th.success,.table>tbody>tr>th.success,.table>tfoot>tr>th.success,.table>thead>tr.success>td,.table>tbody>tr.success>td,.table>tfoot>tr.success>td,.table>thead>tr.success>th,.table>tbody>tr.success>th,.table>tfoot>tr.success>th{background-color:#dff0d8;border-color:#d6e9c6}.table-hover>tbody>tr>td.success:hover,.table-hover>tbody>tr>th.success:hover,.table-hover>tbody>tr.success:hover>td{background-color:#d0e9c6;border-color:#c9e2b3}.table>thead>tr>td.danger,.table>tbody>tr>td.danger,.table>tfoot>tr>td.danger,.table>thead>tr>th.danger,.table>tbody>tr>th.danger,.table>tfoot>tr>th.danger,.table>thead>tr.danger>td,.table>tbody>tr.danger>td,.table>tfoot>tr.danger>td,.table>thead>tr.danger>th,.table>tbody>tr.danger>th,.table>tfoot>tr.danger>th{background-color:#f2dede;border-color:#eed3d7}.table-hover>tbody>tr>td.danger:hover,.table-hover>tbody>tr>th.danger:hover,.table-hover>tbody>tr.danger:hover>td{background-color:#ebcccc;border-color:#e6c1c7}.table>thead>tr>td.warning,.table>tbody>tr>td.warning,.table>tfoot>tr>td.warning,.table>thead>tr>th.warning,.table>tbody>tr>th.warning,.table>tfoot>tr>th.warning,.table>thead>tr.warning>td,.table>tbody>tr.warning>td,.table>tfoot>tr.warning>td,.table>thead>tr.warning>th,.table>tbody>tr.warning>th,.table>tfoot>tr.warning>th{background-color:#fcf8e3;border-color:#fbeed5}.table-hover>tbody>tr>td.warning:hover,.table-hover>tbody>tr>th.warning:hover,.table-hover>tbody>tr.warning:hover>td{background-color:#faf2cc;border-color:#f8e5be}");
			style.append("</style>\n");
			context.put("style", style.toString());
		}

		StringBuffer sb = new StringBuffer();
		// styles copied from bootstrap 3 css
		sb.append("<table class='table table-striped table-condensed'>");
		// header
		sb.append("<tr>");
		for (String header : result.getHeaders()) {
			sb.append("<th>" + StringEscapeUtils.escapeHtml4(header) + "</th>");
		}
		sb.append("</tr>");

		// body
		for (Row row : result.getData()) {
			sb.append("<tr>");
			for (Object column : row.getData()) {
				sb.append("<td>"
						+ StringEscapeUtils.escapeHtml4(column != null ? column
								.toString() : "") + "</td>");
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		return new OutputResult(result, "text/html",
				getInputStream(sb.toString()), false);
	}

	public InputStream getInputStream(String output) {
		try {
			return new ByteArrayInputStream(output.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
