package com.excilys.computerdatabase.service.impl;


import java.util.List;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.computerdatabase.dao.ComputerDAO;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.repositories.ComputerRepository;
import com.excilys.computerdatabase.service.ComputerDBService;

/**
 * Database Service for the Computer
 * Singleton
 */
@Service
public class ComputerDBServiceImpl implements ComputerDBService {


	/**
	 * Instance of the ComputerDAOI
	 */
	@Autowired
	private ComputerDAO computerDAO;

	@Autowired
	private ComputerRepository computerRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Computer> getAll() {
		return computerRepository.findAll();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Computer getById(final long id) {
		return computerRepository.findOne(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(final Computer computer) {
		if (computer != null && !GenericValidator.isBlankOrNull(computer.getName())) {
			computerRepository.save(computer);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(final Computer computer) {
		if (computer != null && !GenericValidator.isBlankOrNull(computer.getName())) {
			computerRepository.save(computer);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final long id) {
		computerRepository.delete(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final List<Long> list) {
		list.forEach(id -> computerRepository.delete(id));		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<Computer> getPagedList(final Page<Computer> page) {
		if (page != null) {
			return computerDAO.getPagedList(page);
		}
		return null;
	}
}
