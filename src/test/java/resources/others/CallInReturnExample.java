package resources.others;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CallInReturnExample {
	
	@SuppressWarnings("resource")
	public int example(Connection con, String badString) throws SQLException {
		
		Statement stmt = con.createStatement();
		return stmt.executeUpdate("delete from table where columnValue="+badString);
	}

	
}
