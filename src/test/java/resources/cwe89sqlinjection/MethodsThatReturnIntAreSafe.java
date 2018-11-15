package resources.cwe89sqlinjection;

import java.sql.Connection;
import java.sql.SQLException;

public class MethodsThatReturnIntAreSafe {

	
	public void executeSafe(Connection con, String param) throws SQLException {
		con.prepareStatement("select * from users where id = " + Integer.parseInt(param));
	}
	
	public void executeSafe2(Connection con, String param) throws SQLException {
		int safeParam = Integer.parseInt(param);
		con.prepareStatement("select * from users where id = " + safeParam);
	}
}
