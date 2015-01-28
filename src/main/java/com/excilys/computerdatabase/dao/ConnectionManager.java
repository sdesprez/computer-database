package com.excilys.computerdatabase.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.excilys.computerdatabase.exceptions.PersistenceException;

/**
 * Manage the connection to the MySQL database
 */
@Component
public class ConnectionManager {

	private static Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
	
	
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
				logger.warn("Couldn't close the connection to the database");
				throw new PersistenceException(e.getMessage(), e);
			}
		}
	}

	/**
	 * Close the statement
	 * 
	 * @param connection
	 */
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

	/**
	 * Close the ResultSet
	 * 
	 * @param connection
	 */
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

	/**
	 * Rollback the connection
	 * @param connection
	 */
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
