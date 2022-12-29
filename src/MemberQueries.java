// Author: YICHIN HO

import java.sql.*;
import java.util.*;

public class MemberQueries {
	
	private static final String URL = "jdbc:mysql://localhost:3306/member?autoReconnect=true&useSSL=false";
	private static final String USERNAME = "java";
	private static final String PASSWORD = "java";
	private static final String DEFAULT_QUERY = "SELECT * FROM people";
	private Connection connection;
	private PreparedStatement selectAllMem;
	private PreparedStatement updateMem;
	
	public MemberQueries() {
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			selectAllMem = connection.prepareStatement(DEFAULT_QUERY);
			updateMem = connection.prepareStatement("UPDATE people SET Class = ? WHERE "+"(MemberID = ?)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	
	public void updateMember(int MemberID, String Class) {
		try {
			updateMem.setString(1, Class);
			updateMem.setInt(2, MemberID);
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
	
	public void setGroup() {
		getAllMember().stream().forEach(m -> {
			updateMember(m.getMemberID(), "classmate");
			System.out.println("update member " + m.getMemberID());
		});
	}
	
}
