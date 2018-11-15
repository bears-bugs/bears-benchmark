package resources.others;

import java.sql.Connection;
import java.sql.SQLException;

public class TernaryInStringConcatenationMultipleConditions {

	
	public void function1(Connection con, String param, String param2) throws SQLException {
		
		String query = "Select * from table where 1=1 ";		
		query += ((!"".equals(param) && !" ".equals(param2)) ? " and test=1 ": "");		
		con.prepareStatement(query);	
		
	}
	
	public void function2 (Connection con, String param) throws SQLException {
		
		String query = "Select * from table where 1=1 ";		
		query += (!"".equals(param) && !" ".equals(param)) ? " and test=1 ": "";		
		con.prepareStatement(query);
	}
}
