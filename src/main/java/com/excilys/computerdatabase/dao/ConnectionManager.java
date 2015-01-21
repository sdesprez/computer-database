package com.excilys.computerdatabase.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
			throw new PersistenceException(e.getMessage(), e);
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
			throw new PersistenceException(e.getMessage(), e);
		}
	}
	
	/**
	 * Close the connection
	 * @param connection
	 */
	public void close(final Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (final SQLException e) {
				logger.warn("Couldn't close the connection to the database");
				throw new PersistenceException(e.getMessage(), e);
			}
		}
	}
	
	public void close(final Statement statement) {
		if (statement != null) {
				try {
					statement.close();
				} catch (final SQLException e) {
					logger.warn("Couldn't close the statement");
					throw new PersistenceException(e.getMessage(), e);
				}
		}
	}
	
	public void close(final ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (final SQLException e) {
				logger.warn("Couldn't close the ResultSet");
				throw new PersistenceException(e.getMessage(), e);
			}
		}
	}
	
	public void rollback(final Connection connection) {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (final SQLException e) {
				logger.warn("Couldn't Rollback the connection");
				throw new PersistenceException(e.getMessage(), e);
			}
		}
	}
	
}
