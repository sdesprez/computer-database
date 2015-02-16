package com.excilys.computerdatabase.webservice.impl;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.webservice.CompanyWebService;
import com.excilys.computerdatabase.wrapper.ListWrapper;

@WebService(endpointInterface="com.excilys.computerdatabase.webservice.CompanyWebService")
public class CompanyWebServiceImpl implements CompanyWebService {

	private CompanyDBService companyDBService;
	
	
	public CompanyWebServiceImpl() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("service-context.xml");
		this.companyDBService = (CompanyDBService) context.getBean(CompanyDBService.class);
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
