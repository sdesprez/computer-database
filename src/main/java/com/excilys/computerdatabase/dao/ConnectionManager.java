package com.excilys.computerdatabase.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.exceptions.PersistenceException;

/**
 * Manage the connection to the MySQL database
 */
public enum ConnectionManager {

	INSTANCE;


	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);
	
	
	/**
	* Url of the database
	*/
	private static final String URL;
	/**
	* Username for the database connection
	*/
	private static final String USERNAME;
	/**
	* Password for the database connection
	*/
	private static final String PASSWORD;
	
	static {
		final Properties properties = new Properties();
		InputStream input = null;
		try {
			// Get the database.properties file
			input = ConnectionManager.class.getClassLoader().getResourceAsStream("database.properties");
			properties.load(input);
			
			//Load the Driver class
			Class.forName(properties.getProperty("DB_DRIVER_CLASS"));
			URL = properties.getProperty("DB_URL");
			USERNAME = properties.getProperty("DB_USERNAME");
			PASSWORD = properties.getProperty("DB_PASSWORD");
		} catch (final IOException e) {
			LOGGER.error("Couldn't load the database.properties file");
			throw new PersistenceException(e.getMessage(), e);
		} catch (final ClassNotFoundException e) {
			LOGGER.error("MySQL JDBC driver not found");
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	/**
	 * Constructor. Load the MySQL JDBC Driver
	 */
	private ConnectionManager() {
		
	}

	/**
	 * @return A connection to the database
	 */
	public Connection getConnection() {
		try {
			return DriverManager.getConnection(URL,	USERNAME, PASSWORD);
		} catch (final SQLException e) {
			LOGGER.error("Couldn't connect to the database");
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	/**
	 * Close the connection
	 * 
	 * @param connection
	 */
	public void close(final Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (final SQLException e) {
				LOGGER.warn("Couldn't close the connection to the database");
				throw new PersistenceException(e.getMessage(), e);
			}
		}
	}

	public void close(final Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (final SQLException e) {
				LOGGER.warn("Couldn't close the statement");
				throw new PersistenceException(e.getMessage(), e);
			}
		}
	}

	public void close(final ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (final SQLException e) {
				LOGGER.warn("Couldn't close the ResultSet");
				throw new PersistenceException(e.getMessage(), e);
			}
		}
	}

	public void rollback(final Connection connection) {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (final SQLException e) {
				LOGGER.warn("Couldn't Rollback the connection");
				throw new PersistenceException(e.getMessage(), e);
			}
		}
	}

}
