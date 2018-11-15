package resources.cwe89sqlinjection;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliMethodParametersAreTainted {

	public void execute(Connection con, String tainted) throws SQLException {
		con.prepareStatement("select * from users where id = " + tainted);
	}
}
