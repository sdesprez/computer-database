package com.excilys.computerdatabase.webservice.impl;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.webservice.CompanyWebService;
import com.excilys.computerdatabase.wrapper.ListWrapper;

@WebService(endpointInterface="com.excilys.computerdatabase.webservice.CompanyWebService")
@Service
public class CompanyWebServiceImpl implements CompanyWebService {

	@Autowired
	private CompanyDBService companyDBService;
	
	
	@Override
	@WebMethod
	public ListWrapper<Company> getCompanies() {
		ListWrapper<Company> wrapper = new ListWrapper<Company>();
		wrapper.setItems(companyDBService.getAll());
		return wrapper;
	}
	
	@Override
	@WebMethod
	public Company getById(long id) {
		Company company = companyDBService.getById(id);
		if (company == null ) {
			company = new Company();
		}
		return company;
	}
	
	@Override
	@WebMethod
	public void delete(long l) {
		companyDBService.delete(l);
	}	

}
