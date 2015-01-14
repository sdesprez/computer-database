package com.excilys.computerdatabase.service.impl;

import java.util.List;

import com.excilys.computerdatabase.dao.CompanyDAOI;
import com.excilys.computerdatabase.dao.impl.CompanyDAO;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
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
	private CompanyDAOI companyDAO = CompanyDAO.getInstance();

	/**
	 * Return the instance of the CompanyDBService
	 * @return Instance of the CompanyDBService
	 */
	public static CompanyDBService getInstance() {
		return INSTANCE;
	}

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
	public Company getById(long id) {
		return companyDAO.getById(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<Company> getPagedList(Page<Company> page) {
		return companyDAO.getPagedList(page);
	}
}
