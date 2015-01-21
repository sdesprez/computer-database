package com.excilys.computerdatabase.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.dao.CompanyDAOI;
import com.excilys.computerdatabase.dao.ConnectionManager;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exceptions.PersistenceException;
import com.excilys.computerdatabase.mapper.RowMapper;
import com.excilys.computerdatabase.mapper.impl.CompanyRowMapperImpl;

/**
 * Data Access Object for the Computer
 * Singleton
 */
public enum CompanyDAO implements CompanyDAOI {

	/**
	 * Instance of ComputerDAO
	 */
	INSTANCE;

	private static final ConnectionManager CM = ConnectionManager.getInstance();
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDAO.class);
	
	private static final String SELECT_QUERY = "SELECT * FROM company";
	private static final String COUNT_QUERY = "SELECT COUNT(id) AS total FROM company";
	private RowMapper<Company> companyMapper = new CompanyRowMapperImpl();
	
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
		Statement stmt = null;
		ResultSet results = null;
		try {
			//Get the connection
			conn = CM.getConnection();
			
			//Create & execute the query
			stmt = conn.createStatement();
			results = stmt.executeQuery(SELECT_QUERY);
			//Create companies with the results
			return companyMapper.mapRowList(results);
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
	public Company getById(final long id) {
		Connection conn = null;
		Company company = null;
		Statement stmt = null;
		ResultSet results = null;
		try {
			//Get a connection to the database
			conn = CM.getConnection();
			
			//Create & execute the query
			stmt = conn.createStatement();
			results = stmt.executeQuery(SELECT_QUERY + " WHERE company.id=" + id + ";");
			//Create a company if there is a result
			if (results.next()) {
				company = companyMapper.mapRow(results);
			}
			return company;
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
	public Page<Company> getPagedList(final Page<Company> page) {
		Connection conn = null;
		Statement countStmt = null;
		PreparedStatement stmt = null;
		ResultSet countResult = null;
		ResultSet results = null;
		try {
			//Get a connection to the database
			conn = CM.getConnection();
			
			//Create & execute the counting query
			countStmt = conn.createStatement();
			countResult = countStmt.executeQuery(COUNT_QUERY);
			//Set the number of results of the page with the result
			countResult.next();
			page.setNbResults(countResult.getInt("total"));
			
			page.refreshNbPages();
			
			//Create the SELECT query
			stmt = conn.prepareStatement(SELECT_QUERY + " LIMIT ? OFFSET ?;");
			stmt.setInt(1, page.getNbResultsPerPage());
			stmt.setInt(2, (page.getPageNumber() - 1) * page.getNbResultsPerPage());
			//Execute the SELECT query
			results = stmt.executeQuery();
			//Create the computers with the results
			page.setList(companyMapper.mapRowList(results));
			return page;
			
		} catch (final SQLException e) {
			LOGGER.error("SQLError in getCompany() with " + page);
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			CM.close(countResult);
			CM.close(results);
			CM.close(countStmt);
			CM.close(stmt);
			//Close the connection
			CM.close(conn);
		}
	}
}
