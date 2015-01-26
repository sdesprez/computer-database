package com.excilys.computerdatabase.service.impl;

import java.util.List;

import com.excilys.computerdatabase.dao.CompanyDAO;
import com.excilys.computerdatabase.dao.ComputerDAO;
import com.excilys.computerdatabase.dao.ConnectionManager;
import com.excilys.computerdatabase.dao.impl.CompanyDAOImpl;
import com.excilys.computerdatabase.dao.impl.ComputerDAOImpl;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.CompanyDBService;

/**
 * Database Service for the Company
 * Singleton
 */
public enum CompanyDBServiceImpl implements CompanyDBService {

	/**
	 * Instance of CompanyDBService
	 */
	INSTANCE;

	/**
	 * Instance of the CompanyDAOI
	 */
	private CompanyDAO companyDAO = CompanyDAOImpl.INSTANCE;

	private ComputerDAO computerDAO = ComputerDAOImpl.INSTANCE;
	private static final ConnectionManager CM = ConnectionManager.INSTANCE;
	

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
		try {
			//Get a transactional connection
			CM.startTransactionalConnection();
			
			//Delete the Computers of the Company
			computerDAO.deleteByCompanyId(id);
			//Delete the Company
			companyDAO.delete(id);
			//Commit the transaction
			CM.commit();
		} finally {
			CM.closeConnection();
		}
	}
}
