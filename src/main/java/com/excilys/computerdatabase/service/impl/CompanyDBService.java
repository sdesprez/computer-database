package com.excilys.computerdatabase.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.dao.CompanyDAOI;
import com.excilys.computerdatabase.dao.ComputerDAOI;
import com.excilys.computerdatabase.dao.ConnectionManager;
import com.excilys.computerdatabase.dao.impl.CompanyDAO;
import com.excilys.computerdatabase.dao.impl.ComputerDAO;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exceptions.PersistenceException;
import com.excilys.computerdatabase.service.CompanyDBServiceI;

/**
 * Database Service for the Company
 * Singleton
 */
public enum CompanyDBService implements CompanyDBServiceI {

	/**
	 * Instance of CompanyDBService
	 */
	INSTANCE;

	/**
	 * Instance of the CompanyDAOI
	 */
	private CompanyDAOI companyDAO = CompanyDAO.INSTANCE;

	private ComputerDAOI computerDAO = ComputerDAO.INSTANCE;
	private static final ConnectionManager CM = ConnectionManager.INSTANCE;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDBService.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Company> getAll() {
		return companyDAO.getAll();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Company getById(final long id) {
		return companyDAO.getById(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<Company> getPagedList(final Page<Company> page) {
		return companyDAO.getPagedList(page);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final long id) {
		Connection connection = null;
		try {
			//Get a connection and set it to transactional mode
			connection = CM.getConnection();
			connection.setAutoCommit(false);
			
			//Delete the Computers of the Company
			computerDAO.deleteByCompanyId(id, connection);
			//Delete the Company
			companyDAO.delete(id, connection);
			//Commit the transaction
			connection.commit();
		} catch (final PersistenceException | SQLException e) {
			LOGGER.error("Error while deleting a company");
			//If there was an error, rollback
			CM.rollback(connection);
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			CM.close(connection);
		}
	}
}
