package com.excilys.computerdatabase.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.computerdatabase.dao.ComputerDAO;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
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


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Computer> getAll() {
		return computerDAO.getAll();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Computer getById(final long id) {
		return computerDAO.getById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Computer> getByCompanyId(final long id) {
		return computerDAO.getByCompanyId(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(final Computer computer) {
		computerDAO.create(computer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(final Computer computer) {
		computerDAO.update(computer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final long id) {
		computerDAO.delete(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final List<Long> list) {
		computerDAO.delete(list);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<Computer> getPagedList(final Page<Computer> page) {
		return computerDAO.getPagedList(page);
	}
}
