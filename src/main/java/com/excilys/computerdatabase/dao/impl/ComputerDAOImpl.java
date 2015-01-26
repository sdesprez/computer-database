package com.excilys.computerdatabase.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.dao.ComputerDAO;
import com.excilys.computerdatabase.dao.ConnectionManager;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exceptions.PersistenceException;
import com.excilys.computerdatabase.mapper.RowMapper;
import com.excilys.computerdatabase.mapper.impl.ComputerRowMapperImpl;

/**
 * Data Access Object for the Computer
 * Singleton
 */
public enum ComputerDAOImpl implements ComputerDAO {

	/**
	 * Instance of ComputerDAO
	 */
	INSTANCE;

	private static final ConnectionManager CM = ConnectionManager.INSTANCE;
	
	/**
	 * Base Query for all the Select queries
	 */
	private static final String SELECT_QUERY = "SELECT c.id, c.name, c.introduced, c.discontinued, company_id, company.name as company_name "
												+ "FROM computer c LEFT JOIN company ON company.id=c.company_id";
	private static final String INSERT_QUERY = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?)";
	private static final String UPDATE_QUERY = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id  =? WHERE id = ?";
	private static final String DELETE_QUERY = "DELETE computer FROM computer WHERE id = ?";
	private static final String DELETE_COMPANY_QUERY = "DELETE computer FROM computer WHERE computer.company_id = ?";
	private static final String COUNT_QUERY = "SELECT COUNT(c.id) AS total "
												+ "FROM computer c "
												+ "LEFT JOIN company ON company.id=c.company_id "
												+ "WHERE c.name LIKE ? OR company.name LIKE ?;";
	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDAOImpl.class);
	
	private RowMapper<Computer> computerMapper = new ComputerRowMapperImpl();
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Computer> getAll() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet results = null;
		try {
			//Get a connection to the database
			conn = CM.getConnection();
			
			//Query the database to get all the computers
			
			stmt = conn.createStatement();
			results = stmt.executeQuery(SELECT_QUERY);
			//Create computers and put them in the computers list with the result
			return computerMapper.mapRowList(results);
		} catch (final SQLException e) {
			LOGGER.error("SQLError in getAll()");
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			CM.close(results);
			CM.close(stmt);
			//Close the connection
			CM.close(conn);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Computer getById(final long id) {
		Connection conn = null;
		Computer computer = null;
		Statement stmt = null;
		ResultSet results = null;
		try {
			//Get the connection to the database
			conn = CM.getConnection();
			
			//Query the database
			final String query = SELECT_QUERY + " WHERE c.id=" + id;
			stmt = conn.createStatement();
			results = stmt.executeQuery(query);
			
			//Create a computer if there is a result
			if (results.next()) {
				computer = computerMapper.mapRow(results);
			}
			return computer;
		} catch (final SQLException e) {
			LOGGER.error("SQLError in getById() with id = " + id);
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			CM.close(results);
			CM.close(stmt);
			//Close the connection
			CM.close(conn);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Computer> getByCompanyId(final long id) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet results = null;
		try {
			//Get a connection to the database
			conn = CM.getConnection();
			
			//Create the query to get the computers of a Company
			final String query = SELECT_QUERY + " WHERE company_id =" + id;
			
			stmt = conn.createStatement();
			
			//Query the database
			results = stmt.executeQuery(query);
			//Create computers with the result
			return computerMapper.mapRowList(results);
		} catch (final SQLException e) {
			LOGGER.error("SQLError in getByCompanyId() with company_id = " + id);
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			CM.close(results);
			CM.close(stmt);
			//Close the connection
			CM.close(conn);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(final Computer computer) {
		if (computer == null || computer.getName() == null || computer.getName().trim().isEmpty()) {
			return;
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		final Company company = computer.getCompany();
		try {
			//Get a connection to the database
			conn = CM.getConnection();
			
			//Create the query
			stmt = conn.prepareStatement(INSERT_QUERY);
			stmt.setString(1, computer.getName());
			final LocalDate introduced = computer.getIntroducedDate();
			final LocalDate discontinued = computer.getDiscontinuedDate();
			if (introduced != null) {
				stmt.setTimestamp(2, Timestamp.valueOf(introduced.atStartOfDay()));
			} else {
				stmt.setNull(2, java.sql.Types.TIMESTAMP);
			}
			if (discontinued != null) {
				stmt.setTimestamp(3, Timestamp.valueOf(discontinued.atStartOfDay()));
			} else {
				stmt.setNull(3, java.sql.Types.TIMESTAMP);
			}
			//If the company is null, put company_id to null
			if (company == null) {
				stmt.setNull(4, java.sql.Types.BIGINT);
			} else {
				stmt.setLong(4, company.getId());
			}
			
			//Execute the query
			stmt.executeUpdate();
		} catch (final SQLException e) {
			LOGGER.error("SQLError in create() with " + computer);
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			CM.close(stmt);
			//Close the connection
			CM.close(conn);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(final Computer computer) {
		if (computer == null) {
			return;
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		final Company company = computer.getCompany();
		try {
			//Get a connection to the database
			conn = CM.getConnection();
			
			//Create the query
			stmt = conn.prepareStatement(UPDATE_QUERY);
			stmt.setString(1, computer.getName());
			final LocalDate introduced = computer.getIntroducedDate();
			final LocalDate discontinued = computer.getDiscontinuedDate();
			if (introduced != null) {
				stmt.setTimestamp(2, Timestamp.valueOf(introduced.atStartOfDay()));
			} else {
				stmt.setNull(2, java.sql.Types.TIMESTAMP);
			}
			if (discontinued != null) {
				stmt.setTimestamp(3, Timestamp.valueOf(discontinued.atStartOfDay()));
			} else {
				stmt.setNull(3, java.sql.Types.TIMESTAMP);
			}
			
			//If the company is null, put company_id to null
			if (company != null) {
				stmt.setLong(4, company.getId());
			} else {
				stmt.setNull(4, java.sql.Types.BIGINT);
			}
			stmt.setLong(5, computer.getId());
			//Execute the query
			stmt.executeUpdate();
		} catch (final SQLException e) {
			LOGGER.error("SQLError in update() with " + computer);
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			CM.close(stmt);
			//Close the connection
			CM.close(conn);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final long id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			//Get a connection
			conn = CM.getConnection();
			
			//Create the query
			stmt = conn.prepareStatement(DELETE_QUERY);
			stmt.setLong(1, id);
			
			//Execute the query
			stmt.executeUpdate();
			
		} catch (final SQLException e) {
			LOGGER.error("SQLError in delete() with id = " + id);
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			CM.close(stmt);
			//Close the connection
			CM.close(conn);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final List<Long> list) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			//Get a connection
			conn = CM.getConnection();
			
			for (long id : list) {
				//Create the query
				stmt = conn.prepareStatement(DELETE_QUERY);
				stmt.setLong(1, id);
				
				//Execute the query
				stmt.executeUpdate();
			}
		} catch (final SQLException e) {
			LOGGER.error("SQLError in delete() with id List = " + list);
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			//Close the Statement and the connection
			CM.close(stmt);
			CM.close(conn);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<Computer> getPagedList(final Page<Computer> page) {
		if (page == null) {
			return null;
		}
		Connection conn = null;
		PreparedStatement countStmt = null;
		PreparedStatement stmt = null;
		ResultSet countResult = null;
		ResultSet results = null;
		final String search = "%" + page.getSearch() + "%";
		try {
			//Get a connection
			conn = CM.getConnection();
			
			//Execute the counting query
			
			countStmt = conn.prepareStatement(COUNT_QUERY);
			countStmt.setString(1, search);
			countStmt.setString(2, search);
			countResult = countStmt.executeQuery();
			//Set the number of results of the page with the result
			countResult.next();
			page.setNbResults(countResult.getInt("total"));
			
			page.refreshNbPages();

			//Create the SELECT query
			final String query = SELECT_QUERY + " WHERE c.name LIKE ? OR company.name LIKE ? ORDER BY " 
						+ page.getSort() + " " + page.getOrder() + " LIMIT ? OFFSET ?;";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, search);
			stmt.setString(2, search);
			stmt.setInt(3, page.getNbResultsPerPage());
			stmt.setInt(4, (page.getPageNumber() - 1) * page.getNbResultsPerPage());
			
			//Execute the SELECT query
			results = stmt.executeQuery();
			//Create the computers with the results
			page.setList(computerMapper.mapRowList(results));
			return page;
			
		} catch (final SQLException e) {
			LOGGER.error("SQLError in getCompany() with " + page);
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			CM.close(countResult);
			CM.close(results);
			CM.close(countStmt);
			CM.close(stmt);
			CM.close(conn);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteByCompanyId(final long id) {
		PreparedStatement statement = null;
		final Connection connection = CM.getTransactionnalConnection();
		try {
			statement = connection.prepareStatement(DELETE_COMPANY_QUERY);
			statement.setLong(1, id);
			statement.executeUpdate();
		} catch (final SQLException e) {
			LOGGER.error("SQLError while deleting Computers of Company " + id);
			CM.rollback(connection);
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			CM.close(statement);
		}
		
	}	
}
