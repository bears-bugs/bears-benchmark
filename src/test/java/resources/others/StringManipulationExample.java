package resources.others;

import java.sql.Connection;
import java.sql.SQLException;

public class StringManipulationExample {

	private Connection connection;

	
	public void example (String badValue) throws SQLException {
		String query = " update table set value="+
				"'"+((badValue      ==null || badValue.trim().equals("")) ? "" : badValue.replaceAll("'", "''"))+"'," ;
		this.connection.prepareStatement(query);
	}
	
	public void example2 (String badValue) throws SQLException {
		String query = " update table set value="+ 
		"'"+(badValue      ==null  ? "" : badValue.replaceAll("'", "''"))+"' " ;
		this.connection.prepareStatement(query);
	}
	
	public void example3 (String badValue) throws SQLException {
		String query = " update table set value='"+  badValue.replaceAll("'", "''")+"' " ;
		this.connection.prepareStatement(query);
	}

}
