package com.excilys.computerdatabase.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.exceptions.PersistenceException;

/**
 * Manage the connection to the MySQL database
 */
public enum ConnectionManager {

	INSTANCE;
	
	/**
	 * Name of the jdbc driver
	 */
	private static final String COM_MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
	/**
	 * Url of the database
	 */
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/computer-database-db?zeroDateTimeBehavior=convertToNull";
	/**
	 * Username for the database connection
	 */
	private static final String USER = "admincdb";
	/**
	 * Password for the database connection
	 */
	private static final String PASSWORD = "qwerty1234";
	private Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
	
	/**
	 * Constructor.
	 * Load the MySQL JDBC Driver
	 */
	private ConnectionManager() {
		try {
			Class.forName(COM_MYSQL_JDBC_DRIVER);
		} catch (final ClassNotFoundException e) {
			logger.error("MySQL JDBC driver not found");
			throw new PersistenceException("Failed to load " + COM_MYSQL_JDBC_DRIVER);
		}
	}
	
	
	/**
	 * @return the Instance of ConnectionManager
	 */
	public static ConnectionManager getInstance() {
		return INSTANCE;
	}
	
	/**
	 * @return A connection to the database
	 */
	public Connection getConnection() {
		try {
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (final SQLException e) {
			logger.error("Couldn't connect to the database");
			throw new PersistenceException();
		}
	}
	
	/**
	 * Close the connection
	 * @param conn
	 */
	public void close(final Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (final SQLException e) {
				logger.warn("Couldn't close the connection to the database");
				throw new PersistenceException();
			}
		}
	}
	
}
