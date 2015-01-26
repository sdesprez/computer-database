package com.excilys.computerdatabase.service.mock;

import java.util.List;

import com.excilys.computerdatabase.dao.CompanyDAO;
import com.excilys.computerdatabase.dao.ComputerDAO;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.CompanyDBService;

public class CompanyDBServiceMock implements CompanyDBService {

	private CompanyDAO companyDAO;
	private ComputerDAO computerDAO;
	
	public CompanyDBServiceMock(final CompanyDAO companyDAO, final ComputerDAO computerDAO) {
		this.companyDAO = companyDAO;
		this.computerDAO = computerDAO;
	}
	
	
	
	
	
	@Override
	public List<Company> getAll() {
		return companyDAO.getAll();
	}

	@Override
	public Company getById(final long id) {
		return companyDAO.getById(id);
	}
	
	@Override
	public Page<Company> getPagedList(final Page<Company> page) {
		return companyDAO.getPagedList(page);
	}
	
	
	@Override
	public void delete(final long id) {
		computerDAO.deleteByCompanyId(id);
		companyDAO.delete(id);
	}

}
