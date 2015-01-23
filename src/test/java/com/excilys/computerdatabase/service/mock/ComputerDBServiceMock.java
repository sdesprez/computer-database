package com.excilys.computerdatabase.service.mock;

import java.util.List;

import com.excilys.computerdatabase.dao.ComputerDAOI;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.ComputerDBServiceI;

public class ComputerDBServiceMock implements ComputerDBServiceI {

	private ComputerDAOI computerDAO;
	
	
	public ComputerDBServiceMock(final ComputerDAOI computerDAO) {
		this.computerDAO = computerDAO;
	}
	
	@Override
	public List<Computer> getAll() {
		return computerDAO.getAll();
	}
	
	
	@Override
	public Computer getById(final long id) {
		return computerDAO.getById(id);
	}

	
	@Override
	public List<Computer> getByCompanyId(final long id) {
		return computerDAO.getByCompanyId(id);
	}

	
	@Override
	public void create(final Computer computer) {
		computerDAO.create(computer);
	}

	
	@Override
	public void update(final Computer computer) {
		computerDAO.update(computer);
	}

	
	@Override
	public void delete(final Long id) {
		computerDAO.delete(id);
	}
	
	
	@Override
	public Page<Computer> getPagedList(final Page<Computer> page) {
		return computerDAO.getPagedList(page);
	}
}
