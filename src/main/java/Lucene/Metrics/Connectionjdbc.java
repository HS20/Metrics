package Lucene.Metrics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Connectionjdbc {
	
	public static String jdbcUrl = "jdbc:oracle:thin:@siad01:1529:INFOPAT";
	public static  String USER = "i2b2demodata";
	public static  String PASS = "i2b2demodata_ucaim";
	private static int i =0;
	private static int l =0;
	private static int k =0;
	public static  Connection connection = null;
	public static Statement stmt = null;
	// public static ResultSet rs;
	// Database credentials
	public Connectionjdbc() {
		// TODO Auto-generated constructor stub
	}
	public static  boolean jdbcConnect() {
		try {
			
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(jdbcUrl, USER , PASS);

			// Do something with the Connection

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;
		}
		
		if (connection != null) {
          System.out.println("You made it, take control your database now!");
     } else {
          System.out.println("Failed to make connection!");
     }
return true;
}
}
