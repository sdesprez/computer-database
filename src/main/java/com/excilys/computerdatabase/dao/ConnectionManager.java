package com.excilys.computerdatabase.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.excilys.computerdatabase.exceptions.PersistenceException;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * Manage the connection to the MySQL database
 */
@Component
public class ConnectionManager {

	private Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
	private BoneCP connectionPool = null;
	private ThreadLocal<Connection> connection;
	
	@Value("${DB_DRIVER_CLASS}")
	private String driverClass;
	
	@Value("${DB_URL}")
	private String url;
	
	@Value("${DB_USERNAME}")
	private String username;
	
	@Value("${DB_PASSWORD}")
	private String password;
	
	@Value("${DB_MIN_CONNECTION_PER_PART}")
	private int minConnection;
	
	@Value("${DB_MAX_CONNECTION_PER_PART}")
	private int maxConnection;
	
	@Value("${DB_PARTITION_COUNT}")
	private int nbPartition;
	
	/**
	 * Create the BoneCP with data from the database.properties file after it was created by Spring
	 */
	@PostConstruct
	public void init() {
		try {
			final BoneCPConfig config = new BoneCPConfig();

			// Load the Driver class
			Class.forName(driverClass);

			//Configure the Pool
			config.setJdbcUrl(url);
			config.setUser(username);
			config.setPassword(password);
			config.setMinConnectionsPerPartition(minConnection);
			config.setMaxConnectionsPerPartition(maxConnection);
			config.setPartitionCount(nbPartition);

			connectionPool = new BoneCP(config);
		} catch (final SQLException e) {
			logger.error("Error while creating the connection pool");
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
	 * @return A transactional connection to the database
	 */
	public void startTransactionalConnection() {
		try {
			final Connection connection = connectionPool.getConnection();
			connection.setAutoCommit(false);
			this.connection = new ThreadLocal<Connection>();
			this.connection.set(connection);
		} catch (final SQLException e) {
			logger.error("Couldn't connect to the database");
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	public Connection getTransactionnalConnection() {
		if (connection != null) {
			return connection.get();
		}
		return null;
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
	
	/**
	 * Commit the connection
	 */
	public void commit() {
		if (connection != null) {
			try {
				connection.get().commit();
			} catch (final SQLException e) {
				logger.warn("Couldn't Commit the connection");
				throw new PersistenceException(e.getMessage(), e);
			}
		}
	}
	
	public void closeConnection() {
		if (connection != null) {
			try {
				connection.get().close();
				connection.remove();
			} catch (final SQLException e) {
				logger.warn("Couldn't Commit the connection");
				throw new PersistenceException(e.getMessage(), e);
			}
		}
	}

}
