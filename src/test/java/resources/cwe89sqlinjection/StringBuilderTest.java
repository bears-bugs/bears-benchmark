package resources.cwe89sqlinjection;

import java.sql.Connection;
import java.sql.SQLException;

public class StringBuilderTest {

	

	public void testStringBuilder(Connection con, String param) throws SQLException {
		
		StringBuilder query = new StringBuilder();
		query.append("select name from users ");
		query.append("where id = ").append(param);
		
		
		con.prepareStatement(query.toString());
	}
	
	public void testStringBuffer(Connection con, String param) throws SQLException {
		StringBuffer query = new StringBuffer ();
		query.append("select name from users ");
		query.append("where id = ").append(param);
		
		
		con.prepareStatement(query.toString());
	}
}
