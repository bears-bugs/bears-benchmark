package resources.cwe89sqlinjection;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

public class SqliConditional {
	
	
	public static void testConditionalString(Connection con, String badParam) throws SQLException {
		String sql = "select field from table " ;
		
		if (!StringUtils.isBlank(badParam)){
			sql += " where field2 = " + badParam;
		}
		con.prepareStatement(sql);
	}
	
	public static void testConditionalStringBuilder(Connection con, String badParam) throws SQLException {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select field from table ") ;
		
		if (!StringUtils.isBlank(badParam)){
			sql.append(" where field2 = ").append(badParam);
		}
		con.prepareStatement(sql.toString());
	}

}
