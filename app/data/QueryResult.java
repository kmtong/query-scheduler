package data;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedList;
import java.util.List;

public class QueryResult {

	List<String> headers;
	List<Row> data;
	String name;

	public QueryResult(String name, ResultSet result) throws Exception {
		this.name = name;
		ResultSetMetaData meta = result.getMetaData();
		headers = new LinkedList<String>();
		data = new LinkedList<Row>();
		int columns = meta.getColumnCount();
		for (int i = 1; i <= columns; i++) {
			headers.add(meta.getColumnLabel(i));
		}

		long rowId = 0L;
		while (result.next()) {
			Row row = new Row(rowId++);
			for (int i = 1; i <= columns; i++) {
				row.addColumnData(result.getObject(i));
			}
			data.add(row);
		}
	}

	public List<String> getHeaders() {
		return headers;
	}

	public List<Row> getData() {
		return data;
	}

	public String getName() {
		return name;
	}
}
