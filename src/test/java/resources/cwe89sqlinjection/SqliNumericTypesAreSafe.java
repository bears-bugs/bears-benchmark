package resources.cwe89sqlinjection;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliNumericTypesAreSafe {

	private int safeField = 1;
	
	public void execute(Connection con, int safeParameter) throws SQLException {
		con.prepareStatement("select * from users where id = " + safeParameter);
	}
	@SuppressWarnings("unqualified-field-access")
	public void execute(Connection con) throws SQLException {
		con.prepareStatement("select * from users where id = " + safeField);
	}
}
