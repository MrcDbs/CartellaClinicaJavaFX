package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DataBase {
	
	 	private static final String URL = "jdbc:postgresql://localhost:5432/CartellaClinica";
	    private static final String USERNAME = "postgres"; 
	    private static final String PASSWORD = "password"; 
	    
	    
	    public Connection connetti() throws SQLException {
	    	Properties properties = new Properties();
	    
	    	try (InputStream input = new FileInputStream("src/config.properties")) {
	            // Load the properties file
	            properties.load(input);
	    	}catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    	try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
		    
	    }
	   
}
