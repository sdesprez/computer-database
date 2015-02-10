package com.excilys.computerdatabase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerdatabase.dao.CompanyDAO;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.repositories.CompanyRepository;
import com.excilys.computerdatabase.repositories.ComputerRepository;
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
	private CompanyRepository companyRepository;
	
	@Autowired
	private ComputerRepository computerRepository;
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Company> getAll() {
		return companyRepository.findAll();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Company getById(final long id) {
		return companyRepository.findOne(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<Company> getPagedList(final Page<Company> page) {
		if (page == null) {
			return null;
		}
		return companyDAO.getPagedList(page);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void delete(final long id) {
		//Delete the Computers of the Company
		computerRepository.deleteByCompanyId(id);
		//Delete the Company
		companyRepository.delete(id);
	}
}
