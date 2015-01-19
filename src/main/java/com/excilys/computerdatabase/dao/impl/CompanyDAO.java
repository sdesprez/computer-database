package com.excilys.computerdatabase.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.dao.CompanyDAOI;
import com.excilys.computerdatabase.dao.ConnectionManager;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exceptions.PersistenceException;

/**
 * Data Access Object for the Computer
 * Singleton
 */
public enum CompanyDAO implements CompanyDAOI {

	/**
	 * Instance of ComputerDAO
	 */
	INSTANCE;

	private ConnectionManager cm = ConnectionManager.getInstance();
	private static Logger logger = LoggerFactory.getLogger(CompanyDAO.class);
	
	private static final String SELECT_QUERY = "SELECT * FROM company";
	private static final String COUNT_QUERY = "SELECT COUNT(id) AS total FROM company";
	
	/**
	 * Return the instance of the CompanyDAO
	 * @return Instance of the CompanyDAO
	 */
	public static CompanyDAO getInstance() {
		return INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Company> getAll() {
		Connection conn = null;
		final List<Company> companies = new ArrayList<Company>();
		Statement stmt = null;
		Company company;
		try {
			//Get the connection
			conn = cm.getConnection();
			
			//Create & execute the query
			stmt = conn.createStatement();
			final ResultSet results = stmt.executeQuery(SELECT_QUERY);
			//Create companies with the results
			while (results.next()) {
				company = new Company();
				company.setId(results.getLong("id"));
				company.setName(results.getString("name"));
				companies.add(company);
			}
			return companies;
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
	public Company getById(final long id) {
		Connection conn = null;
		Company company = null;
		Statement stmt = null;
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			
			//Create & execute the query
			stmt = conn.createStatement();
			final ResultSet results = stmt.executeQuery(SELECT_QUERY + " WHERE company.id=" + id + ";");
			//Create a company if there is a result
			if (results.next()) {
				company = createCompany(results);
			}
			return company;
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
	public Page<Company> getPagedList(final Page<Company> page) {
		Connection conn = null;
		Statement countStmt = null;
		PreparedStatement stmt = null;
		final List<Company> companies = new ArrayList<Company>();
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			
			//Create & execute the counting query
			countStmt = conn.createStatement();
			final ResultSet countResult = countStmt.executeQuery(COUNT_QUERY);
			//Set the number of results of the page with the result
			countResult.next();
			page.setNbResults(countResult.getInt("total"));
			
			page.refreshNbPages();
			
			//Create the SELECT query
			stmt = conn.prepareStatement(SELECT_QUERY + " LIMIT ? OFFSET ?;");
			stmt.setInt(1, page.getNbResultsPerPage());
			stmt.setInt(2, (page.getPageNumber() - 1) * page.getNbResultsPerPage());
			//Execute the SELECT query
			final ResultSet results = stmt.executeQuery();
			//Create the computers with the results
			while (results.next()) {
				companies.add(createCompany(results));
			}
			page.setList(companies);
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
	private static Company createCompany(final ResultSet rs) throws SQLException {
		return Company.builder().id(rs.getLong("id")).name(rs.getString("name")).build();
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
