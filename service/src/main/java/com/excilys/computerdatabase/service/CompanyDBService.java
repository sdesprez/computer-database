package com.excilys.computerdatabase.service;

import java.util.List;

import com.excilys.computerdatabase.domain.Company;

public interface CompanyDBService {

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
	 * Delete a Company from the database and all associated Computers
	 * @param id Id of the Company to delete
	 */
	void delete(long id);
}
