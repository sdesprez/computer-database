package com.excilys.computerdatabase.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.repositories.CompanyRepository;
import com.excilys.computerdatabase.repositories.ComputerRepository;
import com.excilys.computerdatabase.service.CompanyDBService;

/**
 * Spring Data JPA implementation for the CompanyDBService
 */
@Service
public class CompanyDBServiceImpl implements CompanyDBService {
	
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
	@Transactional
	public void delete(final long id) {
		//Delete the Computers of the Company
		computerRepository.deleteByCompanyId(id);
		//Delete the Company
		companyRepository.delete(id);
	}
}
