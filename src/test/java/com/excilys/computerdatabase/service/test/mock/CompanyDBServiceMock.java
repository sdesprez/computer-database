package com.excilys.computerdatabase.service.test.mock;

import java.util.List;

import com.excilys.computerdatabase.dao.CompanyDAOI;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.CompanyDBServiceI;

public class CompanyDBServiceMock implements CompanyDBServiceI {


	
	private CompanyDAOI companyDAO;
	
	
	
	public CompanyDBServiceMock(CompanyDAOI companyDAO) {
		this.companyDAO = companyDAO;
	}
	
	@Override
	public List<Company> getAll() {
		return companyDAO.getAll();
	}

	@Override
	public Company getById(long id) {
		return companyDAO.getById(id);
	}

	@Override
	public Page<Company> getPagedList(Page<Company> page) {
		return companyDAO.getPagedList(page);
	}

}