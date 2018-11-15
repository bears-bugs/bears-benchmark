package resources.cwe89sqlinjection;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliConstFieldsAreSafe {
	private static final String SAFE = "safe";
	
	public void execute(Connection con) throws SQLException {
		con.prepareStatement("select * from users where id = " + SAFE);
	}
}
