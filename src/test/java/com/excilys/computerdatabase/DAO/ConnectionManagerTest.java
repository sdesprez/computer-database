package com.excilys.computerdatabase.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.exceptions.PersistenceException;

public enum ConnectionManagerTest {

	INSTANCE;
	
	
	private static final String COM_MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";

	private static final String URL = "jdbc:mysql://127.0.0.1:3306/computer-database-test-db?zeroDateTimeBehavior=convertToNull";

	private static final String USER = "admincdb";

	private static final String PASSWORD = "qwerty1234";
	private Logger logger = LoggerFactory.getLogger("com.excilys.computerdatabase.dao.ConnectionManager");
	

	private ConnectionManagerTest() {
		try {
			Class.forName(COM_MYSQL_JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			logger.error("MySQL JDBC driver not found");
			throw new PersistenceException("Failed to load " + COM_MYSQL_JDBC_DRIVER);
		}
	}
	
	
	/**
	 * @return the Instance of ConnectionManager
	 */
	public static ConnectionManagerTest getInstance() {
		return INSTANCE;
	}
	
	/**
	 * @return A connection to the database
	 */
	public Connection getConnection() {
		try {
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			logger.error("Couldn't connect to the database");
			throw new PersistenceException();
		}
	}
	
	/**
	 * Close the connection
	 * @param conn
	 */
	public void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.warn("Couldn't close the connection to the database");
				throw new PersistenceException();
			}
		}
	}
}
