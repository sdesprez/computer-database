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
 * @author Sylvain DESPREZ
 *
 */
public enum CompanyDAO implements CompanyDAOI {

	/**
	 * Instance of ComputerDAO
	 */
	INSTANCE;

	private ConnectionManager cm = ConnectionManager.getInstance();
	private Logger logger = LoggerFactory.getLogger("com.excilys.computerdatabase.dao.CompanyDAO");
	
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
		List<Company> companies = new ArrayList<Company>();
		Company company;
		try {
			//Get the connection
			conn = cm.getConnection();
			
			//Create the query
			String query = "SELECT * FROM company;";
			Statement stmt = conn.createStatement();
			
			//Execute the query
			ResultSet results = stmt.executeQuery(query);
			//Create companies with the results
			while (results.next()) {
				company = new Company();
				company.setId(results.getLong("id"));
				company.setName(results.getString("name"));
				companies.add(company);
			}
			return companies;
		} catch (SQLException e) {
			logger.error("SQLError in getAll()");
			throw new PersistenceException();
		} finally {
			//Close the connection
			cm.close(conn);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Company getById(long id) {
		Connection conn = null;
		Company company = null;
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			
			//Create the query
			String query = "SELECT * FROM company WHERE company.id=" + id + ";";
			Statement stmt = conn.createStatement();
			
			//Execute the query
			ResultSet results = stmt.executeQuery(query);
			//Create a company if there is a result
			if (results.next()) {
				company = createCompany(results);
			}
			return company;
		} catch (SQLException e) {
			logger.error("SQLError in getById() with id = " + id);
			throw new PersistenceException();
		} finally {
			//Close the connection
			cm.close(conn);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<Company> getPagedList(Page<Company> page) {
		Connection conn = null;
		List<Company> companies = new ArrayList<Company>();
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			
			//Create the counting query
			String countQuery = "SELECT COUNT(id) AS total FROM company";
			Statement countStmt = conn.createStatement();
			//Execute the counting query
			ResultSet countResult = countStmt.executeQuery(countQuery);
			//Set the number of results of the page with the result
			countResult.next();
			page.setNbResults(countResult.getInt("total"));
			
			//Create the SELECT query
			String query = "SELECT * FROM company LIMIT ? OFFSET ?;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, page.getNbResultsPerPage());
			stmt.setInt(2, (page.getPageNumber() - 1) * page.getNbResultsPerPage());
			//Execute the SELECT query
			ResultSet results = stmt.executeQuery();
			//Create the computers with the results
			while (results.next()) {
				companies.add(createCompany(results));
			}
			page.setList(companies);
			return page;
			
		} catch (SQLException e) {
			logger.error("SQLError in getCompany() with " + page);
			throw new PersistenceException();
		} finally {
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
	private static Company createCompany(ResultSet rs) throws SQLException {
		return Company.builder().id(rs.getLong("id")).name(rs.getString("name")).build();
	}
}
