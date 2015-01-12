package com.excilys.computerdatabase.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;

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

	/**
	 * Name of the jdbc driver
	 */
	private static final String COM_MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
	/**
	 * Url of the database
	 */
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/computer-database-db?zeroDateTimeBehavior=convertToNull";
	/**
	 * Username for the database connection
	 */
	private static final String USER = "admincdb";
	/**
	 * Password for the database connection
	 */
	private static final String PASSWORD = "qwerty1234";

	
	/**
	 * Return the instance of the CompanyDAO
	 * @return Instance of the CompanyDAO
	 */
	public static CompanyDAO getInstance() {
		return INSTANCE;
	}

	/**
	 * Connect to the database
	 * @return A connection to the database
	 * @throws ClassNotFoundException : the jdbc driver was not found
	 * @throws SQLException
	 */
	private Connection getConnection() throws ClassNotFoundException,
			SQLException {
		Class.forName(COM_MYSQL_JDBC_DRIVER);
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	/**
	 * Get the List of all the companies in the database
	 * @return List of all the companies in the database
	 * @throws ClassNotFoundException : the jdbc driver was not found
	 * @throws SQLException 
	 */
	public List<Company> getAll() throws ClassNotFoundException, SQLException {
		Connection conn = null;
		List<Company> companies = new ArrayList<Company>();
		Company company;
		try {
			//Get the connection
			conn = getConnection();
			
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
		} finally {
			//Close the connection
			if (conn != null) {
				conn.close();
			}
		}
	}

	/**
	 * Get the company in the database corresponding to the id in parameter
	 * @param id : id of the company in the database
	 * @return the company that was found or null if there is no company for this id
	 * @throws ClassNotFoundException : the jdbc driver was not found
	 * @throws SQLException
	 */
	public Company getCompany(long id) throws ClassNotFoundException,
			SQLException {
		Connection conn = null;
		Company company = null;
		try {
			//Get a connection to the database
			conn = getConnection();
			
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
		} finally {
			//Close the connection
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	/**
	 * Get a Page of companies in the database.
	 * @param Page : A page containing the pageNumber and the max number of results
	 * @return A Page containing the list of companies 
	 * @throws ClassNotFoundException : the jdbc driver was not found
	 * @throws SQLException
	 */
	public Page getPagedList(Page page) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		List<Company> companies = new ArrayList<Company>();
		Company company;
		try {
			//Get a connection to the database
			conn = getConnection();
			
			//Create the counting query
			String countQuery = "SELECT Count(id) as total FROM company";
			Statement countStmt = conn.createStatement();
			//Execute the counting query
			ResultSet countResult = countStmt.executeQuery(countQuery);
			//Set the number of results of the page with the result
			countResult.next();
			page.setNbResults(countResult.getInt("total"));
			
			//Create the SELECT query
			String query = "SELECT * FROM company limit ? offset ?;";
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
			
		} finally {
			//Close the connection
			if (conn != null) {
				conn.close();
			}
		}
	}
}
