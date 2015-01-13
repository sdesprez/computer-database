package com.excilys.computerdatabase.service;

import java.sql.SQLException;
import java.util.List;

import com.excilys.computerdatabase.dao.CompanyDAO;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;

/**
 * Database Service for the Company
 * Singleton
 * @author Sylvain DESPREZ
 *
 */
public enum CompanyDBService {

	/**
	 * Instance of CompanyDBService
	 */
	INSTANCE;

	/**
	 * Instance of the CompanyDAO
	 */
	private CompanyDAO companyDAO = CompanyDAO.getInstance();

	/**
	 * Return the instance of the CompanyDBService
	 * @return Instance of the CompanyDBService
	 */
	public static CompanyDBService getInstance() {
		return INSTANCE;
	}

	/**
	 * Get the List of all the companies in the database
	 * @return List of all the companies in the database
	 * @throws ClassNotFoundException : the jdbc driver was not found
	 * @throws SQLException 
	 */
	public List<Company> getAll() throws ClassNotFoundException, SQLException {
		return companyDAO.getAll();
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
		return companyDAO.getCompany(id);
	}
	
	/**
	 * Get a Page of companies in the database.
	 * @param Page : A page containing the pageNumber and the max number of results
	 * @return A Page containing the list of companies 
	 * @throws ClassNotFoundException : the jdbc driver was not found
	 * @throws SQLException
	 */
	public Page<Company> getPagedList(Page<Company> page) throws ClassNotFoundException, SQLException {
		return companyDAO.getPagedList(page);
	}
}
