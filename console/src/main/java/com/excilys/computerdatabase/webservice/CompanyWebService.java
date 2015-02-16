package com.excilys.computerdatabase.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.wrapper.ListWrapper;

@WebService
@SOAPBinding(style = Style.RPC)
public interface CompanyWebService {

	@WebMethod ListWrapper<Company> getCompanies();
	@WebMethod Company getById(long l);
	@WebMethod void delete(long l);
}
