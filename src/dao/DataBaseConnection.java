package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseConnection {
	
//	 private static final String URL = "jdbc:postgresql://localhost:5432/CartellaClinica";
//	    private static final String USERNAME = "postgres"; // Your DB username
//	    private static final String PASSWORD = "password"; // Your DB password

//	    public Connection getConnection() throws SQLException {
//	    	Properties properties = new Properties();
//	    
//	    	try (InputStream input = new FileInputStream("src/config.properties")) {
//	            // Load the properties file
//	            properties.load(input);
//	    	}catch (IOException ex) {
//	            ex.printStackTrace();
//	        }
//	    	try {
//				Class.forName("org.postgresql.Driver");
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	        return DriverManager.getConnection(properties.getProperty("database.url"), properties.getProperty("database.username"), properties.getProperty("database.password"));
//	    }
	    
	    public Connection getConnection() throws SQLException{
	    	Properties properties = new Properties();

	        // Load properties and establish connection
	        try (InputStream input = new FileInputStream("src/config.properties")) {
	            // Load the properties file
	            properties.load(input);

	            // Load the database driver
	            Class.forName(properties.getProperty("database.driver"));

	            // Return the connection using the loaded properties
	            return DriverManager.getConnection(
	                    properties.getProperty("database.url"),
	                    properties.getProperty("database.username"),
	                    properties.getProperty("database.password")
	            );

	        } catch (IOException e) {
	            // Log or rethrow as appropriate
	            throw new SQLException("Errore nel caricamento della configurarione del database.", e);
	        } catch (ClassNotFoundException e) {
	            // Log or rethrow as appropriate
	            throw new SQLException("Database driver not found.", e);
	        }
	    
	    }

}
