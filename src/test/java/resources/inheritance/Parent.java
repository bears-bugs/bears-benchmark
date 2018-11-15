package resources.inheritance;

import java.sql.Connection;

public class Parent {
	
	protected Connection con;
	
	public Parent(Connection con) {
		this.con = con;
	}

}
