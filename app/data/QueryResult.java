package data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryResult {

	final Connection conn;
	final Statement statement;
	final ResultSet result;

	public QueryResult(Connection connection, Statement statement,
			ResultSet result) {
		this.conn = connection;
		this.statement = statement;
		this.result = result;
	}

	public Connection getConn() {
		return conn;
	}

	public Statement getStatement() {
		return statement;
	}

	public ResultSet getResult() {
		return result;
	}

	public void close() {
		try {
			this.result.close();
		} catch (SQLException e) {
		}
		try {
			this.statement.close();
		} catch (SQLException e) {
		}
		try {
			this.conn.close();
		} catch (SQLException e) {
		}
	}

	@Override
	protected void finalize() throws Throwable {
		this.close();
		super.finalize();
	}

}
