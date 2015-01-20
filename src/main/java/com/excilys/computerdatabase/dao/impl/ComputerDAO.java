package com.excilys.computerdatabase.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.dao.ComputerDAOI;
import com.excilys.computerdatabase.dao.ConnectionManager;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exceptions.PersistenceException;

/**
 * Data Access Object for the Computer
 * Singleton
 */
public enum ComputerDAO implements ComputerDAOI {

	/**
	 * Instance of ComputerDAO
	 */
	INSTANCE;

	private ConnectionManager cm = ConnectionManager.getInstance();
	
	/**
	 * Base Query for all the Select queries
	 */
	private static final String SELECT_QUERY = "SELECT c.id, c.name, c.introduced, c.discontinued, company_id, company.name as company FROM computer c LEFT JOIN company ON company.id=c.company_id";
	private static final String INSERT_QUERY = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?)";
	private static final String UPDATE_QUERY = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id  =? WHERE id = ?";
	private static final String DELETE_QUERY = "DELETE computer FROM computer WHERE id = ?";
	private static final String COUNT_QUERY = "SELECT COUNT(c.id) AS total FROM computer c LEFT JOIN company ON company.id=c.company_id WHERE c.name LIKE ? OR company.name LIKE ?;";
	private static Logger logger = LoggerFactory.getLogger(ComputerDAO.class);
	
	/**
	 * Return the instance of the ComputerDAO
	 * @return Instance of the ComputerDAO
	 */
	public static ComputerDAO getInstance() {
		return INSTANCE;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Computer> getAll() {
		Connection conn = null;
		Statement stmt = null;
		final List<Computer> computers = new ArrayList<Computer>();
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			
			//Query the database to get all the computers
			ResultSet results;
			stmt = conn.createStatement();
			results = stmt.executeQuery(SELECT_QUERY);
			//Create computers and put them in the computers list with the result
			while (results.next()) {
				computers.add(createComputer(results));
			}
			return computers;
		} catch (final SQLException e) {
			logger.error("SQLError in getAll()");
			throw new PersistenceException();
		} finally {
			closeStatement(stmt);
			//Close the connection
			cm.close(conn);
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
		try {
			//Get the connection to the database
			conn = cm.getConnection();
			
			//Query the database
			final String query = SELECT_QUERY + " WHERE c.id=" + id;
			ResultSet results;
			stmt = conn.createStatement();
			results = stmt.executeQuery(query);
			
			//Create a computer if there is a result
			if (results.next()) {
				computer = createComputer(results);
			}
			return computer;
		} catch (final SQLException e) {
			logger.error("SQLError in getById() with id = " + id);
			throw new PersistenceException();
		} finally {
			closeStatement(stmt);
			//Close the connection
			cm.close(conn);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Computer> getByCompanyId(final long id) {
		Connection conn = null;
		Statement stmt = null;
		final List<Computer> computers = new ArrayList<Computer>();
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			
			//Create the query to get the computers of a Company
			final String query = SELECT_QUERY + " WHERE company_id =" + id;
			ResultSet results;
			stmt = conn.createStatement();
			
			//Query the database
			results = stmt.executeQuery(query);
			//Create computers with the result
			while (results.next()) {
				computers.add(createComputer(results));
			}
			return computers;
		} catch (final SQLException e) {
			logger.error("SQLError in getByCompanyId() with company_id = " + id);
			throw new PersistenceException();
		} finally {
			closeStatement(stmt);
			//Close the connection
			cm.close(conn);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(final Computer computer) {
		Connection conn = null;
		PreparedStatement stmt = null;
		final Company company = computer.getCompany();
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			
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
			logger.error("SQLError in create() with " + computer);
			throw new PersistenceException();
		} finally {
			closeStatement(stmt);
			//Close the connection
			cm.close(conn);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(final Computer computer) {
		Connection conn = null;
		PreparedStatement stmt = null;
		final Company company = computer.getCompany();
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			conn.setAutoCommit(false);
			
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
			//Commit the query
			conn.commit();
		} catch (final SQLException e) {
			logger.error("SQLError in update() with " + computer);
			if (conn != null) {
				try {
					conn.rollback();
				} catch (final SQLException e1) {
					logger.error("SQLError in update() while doing rollback");
					throw new PersistenceException();
				}
			}
			
			throw new PersistenceException();
		} finally {
			closeStatement(stmt);
			//Close the connection
			cm.close(conn);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final Long id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			//Get a connection
			conn = cm.getConnection();
			conn.setAutoCommit(false);
			
			//Create the query
			stmt = conn.prepareStatement(DELETE_QUERY);
			stmt.setLong(1, id);
			
			//Execute the query
			stmt.executeUpdate();
			
			//Commit the query
			conn.commit();
		} catch (final SQLException e) {
			logger.error("SQLError in delete() with id = " + id);
			if (conn != null) {
				try {
					conn.rollback();
				} catch (final SQLException e1) {
					logger.error("SQLError in delete() while doing rollback");
					throw new PersistenceException();
				}
			}
			throw new PersistenceException();
		} finally {
			closeStatement(stmt);
			//Close the connection
			cm.close(conn);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<Computer> getPagedList(final Page<Computer> page) {
		Connection conn = null;
		PreparedStatement countStmt = null;
		PreparedStatement stmt = null;
		final List<Computer> computers = new ArrayList<Computer>();
		final String search = "%" + page.getSearch() + "%";
		try {
			//Get a connection
			conn = cm.getConnection();
			
			//Execute the counting query
			ResultSet countResult;
			countStmt = conn.prepareStatement(COUNT_QUERY);
			countStmt.setString(1, search);
			countStmt.setString(2, search);
			countResult = countStmt.executeQuery();
			//Set the number of results of the page with the result
			countResult.next();
			page.setNbResults(countResult.getInt("total"));
			
			page.refreshNbPages();

			//Create the SELECT query
			final String query = SELECT_QUERY + " WHERE c.name LIKE ? OR company.name LIKE ? ORDER BY " + page.getOrder().getName() + " ASC LIMIT ? OFFSET ?;";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, search);
			stmt.setString(2, search);
			stmt.setInt(3, page.getNbResultsPerPage());
			stmt.setInt(4, (page.getPageNumber() - 1) * page.getNbResultsPerPage());
			
			//Execute the SELECT query
			final ResultSet results = stmt.executeQuery();
			//Create the computers with the results
			while (results.next()) {
				computers.add(createComputer(results));
			}
			page.setList(computers);
			return page;
			
		} catch (final SQLException e) {
			logger.error("SQLError in getCompany() with " + page);
			throw new PersistenceException();
		} finally {
			closeStatement(countStmt);
			closeStatement(stmt);
			//Close the connection
			cm.close(conn);
		}
	}
	
	/**
	 * Create a computer based on the columns of a row of a ResultSet
	 * @param rs : ResultSet on a row containing a computer
	 * @return the computer contained in the row
	 * @throws SQLException 
	 */
	private static Computer createComputer(final ResultSet rs) throws SQLException {
		final Computer.Builder builder = Computer.builder().id(rs.getLong("id")).name(rs.getString("name"));
		final Timestamp introduced = rs.getTimestamp("introduced");
		final Timestamp discontinued = rs.getTimestamp("discontinued");
		if (introduced != null) {
			builder.introducedDate(introduced.toLocalDateTime().toLocalDate());
		}
		if (discontinued != null) {
			builder.discontinuedDate(discontinued.toLocalDateTime().toLocalDate());
		}
		final Long companyId = rs.getLong("company_Id");
		if (companyId != null) {
			builder.company(Company.builder().id(companyId).name(rs.getString("company")).build());
		}
		
		return builder.build();
	}
	
	private static void closeStatement(final Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (final SQLException e) {
				logger.error("Couldn't close Statement");
			}
		}
	}
}
