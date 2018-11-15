package resources.others;

import java.sql.Connection;
import java.sql.SQLException;

public class UsingThis {

	private Connection con;
	
	public void testError(String badParam) throws SQLException {
		String query ="select * from table where param = " + badParam;
		this.con.prepareStatement(query);				 
	}
}
