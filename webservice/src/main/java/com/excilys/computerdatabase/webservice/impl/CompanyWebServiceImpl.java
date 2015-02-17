package com.excilys.computerdatabase.webservice.impl;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.webservice.CompanyWebService;
import com.excilys.computerdatabase.wrapper.ListWrapper;

@WebService(endpointInterface="com.excilys.computerdatabase.webservice.CompanyWebService")
public class CompanyWebServiceImpl implements CompanyWebService {

	private CompanyDBService companyDBService;
	
	
	public CompanyWebServiceImpl(CompanyDBService companyDBService) {
		this.companyDBService = companyDBService;
	}
	
	@Override
	@WebMethod
	public ListWrapper<Company> getCompanies() {
		ListWrapper<Company> wrapper = new ListWrapper<Company>();
		wrapper.setItems(companyDBService.getAll());
		return wrapper;
	}
	
	@Override
	@WebMethod
	public Company getById(long l) {
		return companyDBService.getById(l);
	}
	
	@Override
	@WebMethod
	public void delete(long l) {
		companyDBService.delete(l);
	}	

}
