package com.excilys.computerdatabase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerdatabase.dao.CompanyDAO;
import com.excilys.computerdatabase.dao.ComputerDAO;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.CompanyDBService;

/**
 * Database Service for the Company
 * Singleton
 */
@Service
public class CompanyDBServiceImpl implements CompanyDBService {


	/**
	 * Instance of the CompanyDAOI
	 */
	@Autowired
	private CompanyDAO companyDAO;

	@Autowired
	private ComputerDAO computerDAO;
	

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
	@Transactional
	public void delete(final long id) {
		//Delete the Computers of the Company
		computerDAO.deleteByCompanyId(id);
		//Delete the Company
		companyDAO.delete(id);
	}
}
