package com.excilys.computerdatabase.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.exceptions.PersistenceException;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * Manage the connection to the MySQL database
 */
public enum ConnectionManager {

	INSTANCE;

	private Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
	private BoneCP connectionPool = null;

	/**
	 * Constructor. Load the MySQL JDBC Driver
	 */
	private ConnectionManager() {
		final Properties properties = new Properties();
		InputStream input = null;
		try {
			// Get the database.properties file
			input = ConnectionManager.class.getClassLoader()
					.getResourceAsStream("database.properties");
			properties.load(input);

			final BoneCPConfig config = new BoneCPConfig();

			// Load the Driver class
			Class.forName(properties.getProperty("DB_DRIVER_CLASS"));

			config.setJdbcUrl(properties.getProperty("DB_URL"));
			config.setUser(properties.getProperty("DB_USERNAME"));
			config.setPassword(properties.getProperty("DB_PASSWORD"));
			config.setMinConnectionsPerPartition(5);
			config.setMaxConnectionsPerPartition(10);
			config.setPartitionCount(1);

			connectionPool = new BoneCP(config);
		} catch (final SQLException e) {
			logger.error("Error while creating the connection pool");
			throw new PersistenceException(e.getMessage(), e);
		} catch (final IOException e) {
			logger.error("Couldn't load the database.properties file");
			throw new PersistenceException(e.getMessage(), e);
		} catch (final ClassNotFoundException e) {
			logger.error("MySQL JDBC driver not found");
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	/**
	 * @return A connection to the database
	 */
	public Connection getConnection() {
		try {
			return connectionPool.getConnection();
		} catch (final SQLException e) {
			logger.error("Couldn't connect to the database");
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
