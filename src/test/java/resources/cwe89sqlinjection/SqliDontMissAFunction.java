package resources.cwe89sqlinjection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class SqliDontMissAFunction {
	

	@SuppressWarnings("resource")
	public void executeUnsafe(Connection con, String unsafe) throws SQLException {
		Statement stmt = con.createStatement();
		con.nativeSQL(unsafe);		
		con.prepareCall(unsafe);
		con.prepareCall(unsafe, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		con.prepareCall(unsafe, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		con.prepareStatement(unsafe);
		con.prepareStatement(unsafe, 0);
		con.prepareStatement(unsafe, new int[]{0});
		con.prepareStatement(unsafe, new String[]{"a", "b"});
		con.prepareStatement(unsafe, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		con.prepareStatement(unsafe, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		stmt.addBatch(unsafe);
		stmt.execute(unsafe);
		stmt.execute(unsafe, 1);
		stmt.execute(unsafe, new int[]{0});
		stmt.execute(unsafe, new String[]{"a", "b"});
		stmt.executeQuery(unsafe);
		stmt.executeUpdate(unsafe);
		stmt.executeUpdate(unsafe, 1);
		stmt.executeUpdate(unsafe, new int[]{0});
		stmt.executeUpdate(unsafe, new String[]{"a", "b"});
		
		
		
	}
}
