package resources.noascii;

import java.sql.Connection;
import java.sql.SQLException;

public class NonASCIIJavaExample {


	private Connection connection;
	
	private String nonASCIIdataña;

	
	public void example (String badValue) throws SQLException {
		String query = " update table set value="+
				"'"+((badValue      ==null || badValue.trim().equals("")) ? "" : badValue.replaceAll("'", "''"))+"',"  +nonASCIIdataña;
		this.connection.prepareStatement(query);
	}
	
	public void methodTamaño (String badValue) throws SQLException {
		String query = " update table set value="+
				"'"+((badValue      ==null || badValue.trim().equals("")) ? "" : badValue.replaceAll("'", "''"))+"',"  +nonASCIIdataña;
		this.connection.prepareStatement(query);
		
	}
}
