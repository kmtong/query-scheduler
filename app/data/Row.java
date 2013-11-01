package data;

import java.util.ArrayList;
import java.util.List;

public class Row {

	final List<Object> columnData;
	final long rowId;

	public Row(long rowId) {
		this.rowId = rowId;
		this.columnData = new ArrayList<Object>();
	}

	public void addColumnData(Object data) {
		columnData.add(data);
	}

	public long getRowId() {
		return rowId;
	}

	public List<Object> getData() {
		return columnData;
	}
}
