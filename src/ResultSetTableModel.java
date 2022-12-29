// Author: YICHIN HO

import java.sql.*;
import javax.swing.table.*;

public class ResultSetTableModel extends AbstractTableModel {

	final Connection connection;
	final Statement statement;
	ResultSet resultSet;
	ResultSetMetaData metaData;
	int numOfRows;
	boolean connectedToDatabase = false;

	public ResultSetTableModel(String url, String username, String password, String query) throws SQLException {
		connection = DriverManager.getConnection(url, username, password);
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		connectedToDatabase = true;
		setQuery(query);
	}

	public Class getColumnClass(int column) throws IllegalStateException {
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database");
		try {
			String className = metaData.getColumnClassName(2);	// get column2(name) in database
			return Class.forName(className);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Object.class;
	}

	public int getColumnCount() throws IllegalStateException {
		return 1;	// only 1 column
	}

	public String getColumnName(int column) throws IllegalStateException {
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database");
		try {
			return metaData.getColumnName(2);	// get column2(name) in database
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public int getRowCount() throws IllegalStateException {
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database");
		return numOfRows;
	}

	public Object getValueAt(int column, int row) throws IllegalStateException {
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database");
		try {
			resultSet.absolute(column + 1);
			return resultSet.getObject(2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void setQuery(String query) throws SQLException, IllegalStateException {
		if (!connectedToDatabase)
			throw new IllegalStateException("Not connected to database");
		resultSet = statement.executeQuery(query);
		metaData = resultSet.getMetaData();
		resultSet.last();
		numOfRows = resultSet.getRow();
		fireTableStructureChanged();
	}

	public void disconnectFromDatabase() {
		if (connectedToDatabase) {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				connectedToDatabase = false;
			}
		}
	}

}
