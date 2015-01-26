package com.excilys.computerdatabase.dao;

import java.sql.Connection;
import java.util.List;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;

public interface CompanyDAOI {

	/**
	 * Get the List of all the companies in the database
	 * @return List of all the companies in the database
	 */
	List<Company> getAll();
	
	/**
	 * Get the company in the database corresponding to the id in parameter
	 * @param id : id of the company in the database
	 * @return the company that was found or null if there is no company for this id
	 */
	Company getById(long id);
	
	/**
	 * Get a Page of companies in the database.
	 * @param Page : A page containing the pageNumber and the max number of results
	 * @return A Page containing the list of companies 
	 */
	Page<Company> getPagedList(Page<Company> page);
	
	/**
	 * Delete a Company from the database
	 * @param id Id of the company to delete
	 * @param connection Connection to the database to use
	 */
	void delete(long id, Connection connection);
}
