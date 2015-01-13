package com.excilys.computerdatabase.service;

import java.sql.SQLException;
import java.util.List;

import com.excilys.computerdatabase.dao.ComputerDAO;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;

/**
 * Database Service for the Computer
 * Singleton
 * @author Sylvain DESPREZ
 *
 */
public enum ComputerDBService {

	/**
	 * Instance of ComputerDBService
	 */
	INSTANCE;

	/**
	 * Instance of the ComputerDAO
	 */
	private ComputerDAO computerDAO = ComputerDAO.getInstance();

	/**
	 * Return the instance of the ComputerDBService
	 * @return Instance of the computerDBService
	 */
	public static ComputerDBService getInstance() {
		return INSTANCE;
	}

	/**
	 * Get the List of all the computers in the database
	 * @return List of all the computers in the database
	 * @throws ClassNotFoundException : the jdbc driver was not found
	 * @throws SQLException 
	 */
	public List<Computer> getAll() throws ClassNotFoundException, SQLException {
		return computerDAO.getAll();
	}

	
	
	/**
	 * Get the computer in the database corresponding to the id in parameter
	 * @param id : id of the computer in the database
	 * @return the computer that was found or null if there is no computer for this id
	 * @throws ClassNotFoundException : the jdbc driver was not found
	 * @throws SQLException
	 */
	public Computer getComputer(long id) throws ClassNotFoundException,
			SQLException {
		return computerDAO.getComputer(id);
	}

	/**
	 * Get the list of computers corresponding to the id of a company
	 * @param id : id of the company in the database
	 * @return the list of computers
	 * @throws ClassNotFoundException : the jdbc driver was not found
	 * @throws SQLException
	 */
	public List<Computer> getByCompanyId(long id)
			throws ClassNotFoundException, SQLException {
		return computerDAO.getByCompanyId(id);
	}

	/**
	 * Create a new computer in the database
	 * @param computer : computer to add in the database
	 * @throws ClassNotFoundException : the jdbc driver was not found
	 * @throws SQLException
	 */
	public void create(Computer computer) throws ClassNotFoundException,
			SQLException {
		computerDAO.create(computer);
	}

	/**
	 * Update a computer of the database
	 * @param computer : computer to update in the database
	 * @throws ClassNotFoundException : the jdbc driver was not found
	 * @throws SQLException
	 */
	public void update(Computer computer) throws ClassNotFoundException,
			SQLException {
		computerDAO.update(computer);
	}

	/**
	 * Delete a computer in the database
	 * @param id : id of the computer to delete
	 * @throws ClassNotFoundException : the jdbc driver was not found
	 * @throws SQLException
	 */
	public void delete(Long id) throws ClassNotFoundException, SQLException {
		computerDAO.delete(id);
	}
	
	/**
	 * Get a Page of computers in the database.
	 * @param Page : A page containing the pageNumber and the max number of results
	 * @return A Page containing the list of computers 
	 * @throws ClassNotFoundException : the jdbc driver was not found
	 * @throws SQLException
	 */
	public Page<Computer> getPagedList(Page<Computer> page) throws ClassNotFoundException, SQLException {
		return computerDAO.getPagedList(page);
	}
}
