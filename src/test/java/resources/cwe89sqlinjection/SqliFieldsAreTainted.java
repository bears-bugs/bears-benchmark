package resources.cwe89sqlinjection;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliFieldsAreTainted {
	private String tainted;
	
	@SuppressWarnings("unqualified-field-access")
	public void execute2(Connection con) throws SQLException {
		con.prepareStatement("select * from users where id = " + tainted);
	}
	
	public void execute(Connection con) throws SQLException {
		con.prepareStatement("select * from users where id = " + this.tainted);
	}
	

}
