package resources.others;

import java.sql.Connection;
import java.sql.SQLException;

public class TernaryInStringConcatenation {

	public void function1(Connection con, String email) throws SQLException {
		String sql = 
				"select count(1) from users where active = 1 " + 
				("".equals(email) ? "" : " and email = ?");
		
		con.prepareStatement(sql); // it's ok
		
	}
	public void function2(Connection con, String email) throws SQLException {
		String sql = "select count(1) from users where active = 1 ";
		sql += email != null ? "" : " and email = ?";
		con.prepareStatement(sql); // it's ok
		
	}
	
	public void function3(Connection con, String email) throws SQLException {
		String sql = "select count(1) from users where active = 1 ";
		sql += (email != null ? "" : " and email = ?");
		con.prepareStatement(sql); // it's ok
		
	}
}
