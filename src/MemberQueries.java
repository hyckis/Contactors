// Author: YICHIN HO

import java.sql.*;
import java.util.*;

public class MemberQueries {

	private static final String URL = "jdbc:mysql://localhost:3306/member?autoReconnect=true&useSSL=false";
	private static final String USERNAME = "java";
	private static final String PASSWORD = "java";
	private static final String DEFAULT_QUERY = "SELECT * FROM people";

	Connection connection;
	PreparedStatement selectAllMem;
	PreparedStatement insertNewMem;
	PreparedStatement deleteMem;
	PreparedStatement updateMem;

	static ResultSetTableModel tableModel;

	public MemberQueries() {
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			selectAllMem = connection.prepareStatement(DEFAULT_QUERY);
			insertNewMem = connection.prepareStatement("INSERT INTO people " + "(name, type, phone, Class) " + "VALUES(?, ?, ?, ?)");
			deleteMem = connection.prepareStatement("DELETE FROM people WHERE " + "(memberID = ?)");
			updateMem = connection.prepareStatement("UPDATE people SET name = ?, type = ?, phone = ?, Class = ? WHERE "+"(memberID = ?)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSetTableModel getTableModel() {
		try {
			tableModel = new ResultSetTableModel(URL, USERNAME, PASSWORD, DEFAULT_QUERY);
		} catch (SQLException e) {
			e.printStackTrace();
			tableModel.disconnectFromDatabase();
		}
		return tableModel;
	}
	
	public ResultSetTableModel getNameTable(String keywordOfName) {
		String NAME_QUERY = "SELECT * FROM people WHERE name LIKE '%" + keywordOfName + "%'";
		try {
			tableModel = new ResultSetTableModel(URL, USERNAME, PASSWORD, NAME_QUERY);
		} catch (SQLException e) {
			e.printStackTrace();
			tableModel.disconnectFromDatabase();
		}
		return tableModel;
	}
	
	public ResultSetTableModel getPhoneTable(String keywordOfPhone) {
		String PHONE_QUERY = "SELECT * FROM people WHERE phone LIKE '%" + keywordOfPhone + "%'";
		try {
			tableModel = new ResultSetTableModel(URL, USERNAME, PASSWORD, PHONE_QUERY);
		} catch (SQLException e) {
			e.printStackTrace();
			tableModel.disconnectFromDatabase();
		}
		return tableModel;
	}
	
	public ResultSetTableModel getGroupTable(String selectedGroup) {
		String CLASS_QUERY = "SELECT * FROM people WHERE Class = '" + selectedGroup + "'";
		try {
			tableModel = new ResultSetTableModel(URL, USERNAME, PASSWORD, CLASS_QUERY);
		} catch (SQLException e) {
			e.printStackTrace();
			tableModel.disconnectFromDatabase();
		}
		return tableModel;
	}

	public List<Member> getAllMember() {
		ResultSet resultSet = null;
		List<Member> results = new ArrayList<Member>();
		try {
			resultSet = selectAllMem.executeQuery();
			while (resultSet.next()) {
				results.add(new Member(
					resultSet.getInt("MemberID"),
					resultSet.getString("name"),
					resultSet.getString("type"),
					resultSet.getString("phone"),
					resultSet.getString("Class")
				));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
				close();
			}
		}
		return results;
	}

	public void addMember(String name, String type, String phone, String group) {
		try {
			insertNewMem.setString(1, name);
			insertNewMem.setString(2, type);
			insertNewMem.setString(3, phone);
			insertNewMem.setString(4, group);
			int result = insertNewMem.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			close();
		}
	}

	public void deleteMember(int memberID) {
		try {
			deleteMem.setInt(1, memberID);
			int result = deleteMem.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			close();
		}
	}

	public void updateMember(int memberID, String name, String type, String phone, String group) {
		try {
			updateMem.setString(1, name);
			updateMem.setString(2, type);
			updateMem.setString(3, phone);
			updateMem.setString(4, group);
			updateMem.setInt(5, memberID);
			int result = updateMem.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			close();
		}
	}

	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
