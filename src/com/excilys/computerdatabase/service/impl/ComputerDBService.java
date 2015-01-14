package com.excilys.computerdatabase.service.impl;


import java.util.List;

import com.excilys.computerdatabase.dao.ComputerDAOI;
import com.excilys.computerdatabase.dao.impl.ComputerDAO;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.ComputerDBServiceI;

/**
 * Database Service for the Computer
 * Singleton
 * @author Sylvain DESPREZ
 *
 */
public enum ComputerDBService implements ComputerDBServiceI {

	/**
	 * Instance of ComputerDBService
	 */
	INSTANCE;

	/**
	 * Instance of the ComputerDAOI
	 */
	private ComputerDAOI computerDAO = ComputerDAO.getInstance();

	/**
	 * Return the instance of the ComputerDBService
	 * @return Instance of the computerDBService
	 */
	public static ComputerDBService getInstance() {
		return INSTANCE;
	}

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
	public Computer getById(long id) {
		return computerDAO.getById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Computer> getByCompanyId(long id) {
		return computerDAO.getByCompanyId(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(Computer computer) {
		computerDAO.create(computer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(Computer computer) {
		computerDAO.update(computer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(Long id) {
		computerDAO.delete(id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<Computer> getPagedList(Page<Computer> page) {
		return computerDAO.getPagedList(page);
	}
}
