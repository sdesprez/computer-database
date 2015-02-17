package com.excilys.computerdatabase.service.impl;


import java.util.List;

import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.repositories.ComputerRepository;
import com.excilys.computerdatabase.service.ComputerDBService;

/**
 * Database Service for the Computer
 * Singleton
 */
@Service
public class ComputerDBServiceImpl implements ComputerDBService {


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
		if (computerRepository.findOne(id) != null) {
			computerRepository.delete(id);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final List<Long> list) {
		list.forEach(id -> {if (computerRepository.findOne(id) != null) {computerRepository.delete(id); }});		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<Computer> getPagedList(final String search, final Pageable pageable) {
		if (pageable != null) {
			return computerRepository.findByNameContainingOrCompanyNameContaining(search, search,pageable);
		}
		return null;
	}
}
