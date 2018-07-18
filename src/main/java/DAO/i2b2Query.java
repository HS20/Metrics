package DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;

import Lucene.Metrics.Concept_cd;



public class i2b2Query {
	public static String jdbcUrl = "jdbc:oracle:thin:@siad01:1529:INFOPAT";
	public static  String USER = "i2b2demodata";
	public static  String PASS = "i2b2demodata_ucaim";
	private static int i =0;
	
	public static  Connection connection = null;
	public static Statement stmt = null;
	public i2b2Query() {
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
	public static Set<Concept_cd> QueryConceptCd () throws SQLException {
		
		// Connection à la base
		jdbcConnect();
        // Requête concept_cd
       
        stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
        ResultSet.CONCUR_READ_ONLY);	
        String ConceptCD = "select CONCEPT_CD, NAME_CHAR from i2b2demodata.concept_dimension f ";	
        ResultSet res = stmt.executeQuery(ConceptCD);
        Set <Concept_cd> ConceptCds = new HashSet <Concept_cd> ();
//        stmt.setMaxRows(200);
        while (res.next()) {
			
        	Concept_cd Conceptcd = new Concept_cd();
        	Conceptcd.setID(i);
        	Conceptcd.setConceptCdlabel(res.getString("Concept_cd"));
//        	System.out.println(res.getString("NAME_CHAR"));
        	Conceptcd.setConceptCdLib(res.getString("NAME_CHAR"));
        	ConceptCds.add(Conceptcd);
//        	System.out.println("libelle" + Conceptcd.getConceptCdLib());
        	i=i+1;
	}
        return ConceptCds;
}
	
	
	
}
