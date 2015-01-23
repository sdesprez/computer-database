package com.excilys.computerdatabase.service.mock;

import java.util.List;

import com.excilys.computerdatabase.dao.CompanyDAOI;
import com.excilys.computerdatabase.dao.ComputerDAOI;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.CompanyDBServiceI;

public class CompanyDBServiceMock implements CompanyDBServiceI {

	private CompanyDAOI companyDAO;
	private ComputerDAOI computerDAO;
	
	public CompanyDBServiceMock(final CompanyDAOI companyDAO, final ComputerDAOI computerDAO) {
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
		computerDAO.deleteByCompanyId(id, null);
		companyDAO.delete(id, null);
	}

}
