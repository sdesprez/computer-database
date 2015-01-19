package com.excilys.computerdatabase.dao.test.mock;

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
import com.excilys.computerdatabase.dao.test.ConnectionManagerTest;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exceptions.PersistenceException;

public enum ComputerDAOMock implements ComputerDAOI {

	INSTANCE;

	private ConnectionManagerTest cm = ConnectionManagerTest.getInstance();
	

	private static final String SELECT_QUERY = "SELECT c.id, c.name, c.introduced, c.discontinued, company_id, company.name as company FROM computer c LEFT JOIN company ON company.id=c.company_id";
	private static final String INSERT_QUERY = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?)";
	private static final String UPDATE_QUERY = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id  =? WHERE id = ?";
	private static final String DELETE_QUERY = "DELETE computer FROM computer WHERE id = ?";
	private static final String COUNT_QUERY = "SELECT COUNT(id) AS total FROM computer";
	private Logger logger = LoggerFactory.getLogger("com.excilys.computerdatabase.dao.computerDAO");
	

	public static ComputerDAOMock getInstance() {
		return INSTANCE;
	}


	@Override
	public List<Computer> getAll() {
		Connection conn = null;
		final List<Computer> computers = new ArrayList<Computer>();
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			
			//Query the database to get all the computers
			ResultSet results;
			final Statement stmt = conn.createStatement();
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
			//Close the connection
			cm.close(conn);
		}
	}
	
	@Override
	public Computer getById(final long id) {
		Connection conn = null;
		Computer computer = null;
		try {
			//Get the connection to the database
			conn = cm.getConnection();
			
			//Query the database
			final String query = SELECT_QUERY + " WHERE c.id=" + id;
			ResultSet results;
			final Statement stmt = conn.createStatement();
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
			//Close the connection
			cm.close(conn);
		}
	}

	@Override
	public List<Computer> getByCompanyId(final long id) {
		Connection conn = null;
		final List<Computer> computers = new ArrayList<Computer>();
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			
			//Create the query to get the computers of a Company
			final String query = SELECT_QUERY + " WHERE company_id =" + id;
			ResultSet results;
			final Statement stmt = conn.createStatement();
			
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
			//Close the connection
			cm.close(conn);
		}
	}

	@Override
	public void create(final Computer computer) {
		Connection conn = null;
		final Company company = computer.getCompany();
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			
			//Create the query
			final PreparedStatement stmt = conn.prepareStatement(INSERT_QUERY);
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
			//Close the connection
			cm.close(conn);
		}
	}

	@Override
	public void update(final Computer computer) {
		Connection conn = null;
		final Company company = computer.getCompany();
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			conn.setAutoCommit(false);
			
			//Create the query
			final PreparedStatement stmt = conn.prepareStatement(UPDATE_QUERY);
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
			//Close the connection
			cm.close(conn);
		}
	}

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
			//Close the connection
			cm.close(conn);
		}
	}
	
	@Override
	public Page<Computer> getPagedList(final Page<Computer> page) {
		Connection conn = null;
		final List<Computer> computers = new ArrayList<Computer>();
		try {
			//Get a connection
			conn = cm.getConnection();
			
			//Execute the counting query
			ResultSet countResult;
			final Statement countStmt = conn.createStatement();
			countResult = countStmt.executeQuery(COUNT_QUERY);
			//Set the number of results of the page with the result
			countResult.next();
			page.setNbResults(countResult.getInt("total"));

			page.refreshNbPages();
			
			//Create the SELECT query
			final String query = SELECT_QUERY + " LIMIT ? OFFSET ? ;";
			final PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, page.getNbResultsPerPage());
			stmt.setInt(2, (page.getPageNumber() - 1) * page.getNbResultsPerPage());
			
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
			//Close the connection
			cm.close(conn);
		}
	}
	
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
}
