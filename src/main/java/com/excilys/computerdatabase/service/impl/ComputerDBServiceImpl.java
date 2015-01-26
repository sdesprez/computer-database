package com.excilys.computerdatabase.service.impl;


import java.util.List;

import com.excilys.computerdatabase.dao.ComputerDAO;
import com.excilys.computerdatabase.dao.impl.ComputerDAOImpl;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.ComputerDBService;

/**
 * Database Service for the Computer
 * Singleton
 */
public enum ComputerDBServiceImpl implements ComputerDBService {

	/**
	 * Instance of ComputerDBService
	 */
	INSTANCE;

	/**
	 * Instance of the ComputerDAOI
	 */
	private ComputerDAO computerDAO = ComputerDAOImpl.INSTANCE;


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
