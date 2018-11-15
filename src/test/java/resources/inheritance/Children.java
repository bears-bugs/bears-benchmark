package resources.inheritance;

import java.sql.Connection;
import java.sql.SQLException;

public class Children extends Parent {
	
	public Children(Connection con) {
		super(con);
	}
	
	@SuppressWarnings("unqualified-field-access")
	public void badCode(String parameter) throws SQLException {
		String query = "select * from table where param=" + parameter;
		con.prepareStatement(query);
	}
	public void badCode2(String parameter) throws SQLException {
		String query = "select * from table where param=" + parameter;
		this.con.prepareStatement(query);
	}

}
