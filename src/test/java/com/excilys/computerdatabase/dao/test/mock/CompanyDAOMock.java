package com.excilys.computerdatabase.dao.test.mock;

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
import com.excilys.computerdatabase.dao.test.ConnectionManagerTest;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exceptions.PersistenceException;

public enum CompanyDAOMock implements CompanyDAOI {

	INSTANCE;

	private ConnectionManagerTest cm = ConnectionManagerTest.getInstance();
	private Logger logger = LoggerFactory.getLogger("com.excilys.computerdatabase.dao.CompanyDAO");
	
	private static final String SELECT_QUERY = "SELECT * FROM company";
	private static final String COUNT_QUERY = "SELECT COUNT(id) AS total FROM company";
	
	/**
	 * Return the instance of the CompanyDAO
	 * @return Instance of the CompanyDAO
	 */
	public static CompanyDAOMock getInstance() {
		return INSTANCE;
	}

	@Override
	public List<Company> getAll() {
		Connection conn = null;
		List<Company> companies = new ArrayList<Company>();
		Company company;
		try {
			//Get the connection
			conn = cm.getConnection();
			
			//Create & execute the query
			Statement stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(SELECT_QUERY);
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

	
	@Override
	public Company getById(long id) {
		Connection conn = null;
		Company company = null;
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			
			//Create & execute the query
			Statement stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery(SELECT_QUERY + " WHERE company.id=" + id + ";");
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
	
	@Override
	public Page<Company> getPagedList(Page<Company> page) {
		Connection conn = null;
		List<Company> companies = new ArrayList<Company>();
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			
			//Create & execute the counting query
			Statement countStmt = conn.createStatement();
			ResultSet countResult = countStmt.executeQuery(COUNT_QUERY);
			//Set the number of results of the page with the result
			countResult.next();
			page.setNbResults(countResult.getInt("total"));
			
			page.refreshNbPages();
			
			//Create the SELECT query
			PreparedStatement stmt = conn.prepareStatement(SELECT_QUERY + " LIMIT ? OFFSET ?;");
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
	
	private static Company createCompany(ResultSet rs) throws SQLException {
		return Company.builder().id(rs.getLong("id")).name(rs.getString("name")).build();
	}
}
