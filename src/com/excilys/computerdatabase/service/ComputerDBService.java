package com.excilys.computerdatabase.service;

import java.sql.SQLException;
import java.util.List;

import com.excilys.computerdatabase.dao.ComputerDAO;
import com.excilys.computerdatabase.domain.Computer;

public enum ComputerDBService {

	INSTANCE;

	private ComputerDAO computerDAO = ComputerDAO.getInstance();

	public static ComputerDBService getInstance() {
		return INSTANCE;
	}

	public List<Computer> getAll() throws ClassNotFoundException, SQLException {
		return computerDAO.getAll();
	}

	public Computer getComputer(long id) throws ClassNotFoundException,
			SQLException {
		return computerDAO.getComputer(id);
	}

	public List<Computer> getByCompanyId(long id)
			throws ClassNotFoundException, SQLException {
		return computerDAO.getByCompanyId(id);
	}

	public void create(Computer computer) throws ClassNotFoundException,
			SQLException {
		computerDAO.create(computer);
	}

	public void update(Computer computer) throws ClassNotFoundException,
			SQLException {
		computerDAO.update(computer);
	}

	public void delete(Long id) throws ClassNotFoundException, SQLException {
		computerDAO.delete(id);
	}
}
