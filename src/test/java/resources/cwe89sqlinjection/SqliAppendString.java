package resources.cwe89sqlinjection;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliAppendString {
	
	public void test(Connection con, String bad) throws SQLException {
		String sql = "select field from table " ;
		sql += " where field2 = " + bad;	
		con.prepareStatement(sql);
		
	}

}
