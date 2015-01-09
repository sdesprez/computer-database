package com.excilys.computerdatabase.service;

import java.sql.SQLException;
import java.util.List;

import com.excilys.computerdatabase.dao.CompanyDAO;
import com.excilys.computerdatabase.domain.Company;

public enum CompanyDBService {

	INSTANCE;

	private CompanyDAO companyDAO = CompanyDAO.getInstance();

	public static CompanyDBService getInstance() {
		return INSTANCE;
	}

	public List<Company> getAll() throws ClassNotFoundException, SQLException {
		return companyDAO.getAll();
	}

	public Company getCompany(long id) throws ClassNotFoundException,
			SQLException {
		return companyDAO.getCompany(id);
	}
}
