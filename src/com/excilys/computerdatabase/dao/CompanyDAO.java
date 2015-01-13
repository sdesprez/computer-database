package com.excilys.computerdatabase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exceptions.PersistenceException;

/**
 * Data Access Object for the Computer
 * Singleton
 * @author Sylvain DESPREZ
 *
 */
public enum CompanyDAO {

	/**
	 * Instance of ComputerDAO
	 */
	INSTANCE;

	private ConnectionManager cm = ConnectionManager.getInstance();
	
	/**
	 * Return the instance of the CompanyDAO
	 * @return Instance of the CompanyDAO
	 */
	public static CompanyDAO getInstance() {
		return INSTANCE;
	}

	/**
	 * Get the List of all the companies in the database
	 * @return List of all the companies in the database
	 */
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
			throw new PersistenceException();
		} finally {
			//Close the connection
			cm.close(conn);
		}
	}

	/**
	 * Get the company in the database corresponding to the id in parameter
	 * @param id : id of the company in the database
	 * @return the company that was found or null if there is no company for this id
	 */
	public Company getCompany(long id) {
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
				company = new Company();
				company.setId(results.getLong("id"));
				company.setName(results.getString("name"));
			}
			return company;
		} catch(SQLException e) {
			throw new PersistenceException();
		} finally {
			//Close the connection
			cm.close(conn);
		}
	}
	
	/**
	 * Get a Page of companies in the database.
	 * @param Page : A page containing the pageNumber and the max number of results
	 * @return A Page containing the list of companies 
	 */
	public Page<Company> getPagedList(Page<Company> page) {
		Connection conn = null;
		List<Company> companies = new ArrayList<Company>();
		Company company;
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
				company = new Company();
				company.setId(results.getLong("id"));
				company.setName(results.getString("name"));
				companies.add(company);
			}
			page.setList(companies);
			return page;
			
		} catch (SQLException e) {
			throw new PersistenceException();
		} finally {
			//Close the connection
			cm.close(conn);
		}
	}
}
