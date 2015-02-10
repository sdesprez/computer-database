package com.excilys.computerdatabase.service;

import java.util.List;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;

public interface ComputerDBService {

	/**
	 * Get the List of all the computers in the database
	 * @return List of all the computers in the database
	 */
	List<Computer> getAll();

	
	
	/**
	 * Get the computer in the database corresponding to the id in parameter
	 * @param id : id of the computer in the database
	 * @return the computer that was found or null if there is no computer for this id
	 */
	Computer getById(long id);

	/**
	 * Create a new computer in the database
	 * @param computer : computer to add in the database
	 */
	void create(Computer computer);

	/**
	 * Update a computer of the database
	 * @param computer : computer to update in the database
	 */
	void update(Computer computer);

	/**
	 * Delete a computer in the database
	 * @param id : id of the computer to delete
	 */
	void delete(long id);
	
	/**
	 * Delete a List of Computer in the database
	 * @param id : List if the IDs of the Computers to delete
	 */
	void delete(List<Long> list);
	
	/**
	 * Get a Page of computers in the database.
	 * @param Page : A page containing the pageNumber and the max number of results
	 * @return A Page containing the list of computers 
	 */
	Page<Computer> getPagedList(Page<Computer> page);
}
