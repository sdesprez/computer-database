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

	/**
	 * Get the List of all the companies in the database
	 * @return List of all the companies in the database
	 */
	@WebMethod ListWrapper<Company> getCompanies();
	
	/**
	 * Get the company in the database corresponding to the id in parameter
	 * @param id : id of the company in the database
	 * @return the company that was found or a Company with id = 0 if none was found
	 */
	@WebMethod Company getById(long l);
	
	/**
	 * Delete a Company from the database and all associated Computers
	 * @param id : Id of the Company to delete
	 */
	@WebMethod void delete(long l);
}
