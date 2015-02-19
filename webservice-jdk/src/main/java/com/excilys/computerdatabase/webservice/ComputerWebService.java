package com.excilys.computerdatabase.webservice;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.excilys.computerdatabase.dto.ComputerDTO;
import com.excilys.computerdatabase.wrapper.ListWrapper;
import com.excilys.computerdatabase.wrapper.PageWrapper;

@WebService
@SOAPBinding(style = Style.RPC)
public interface ComputerWebService {
	
	@WebMethod ComputerDTO getById(long id);
	@WebMethod ListWrapper<ComputerDTO> getAll();
	@WebMethod ListWrapper<String> create(ComputerDTO computerDTO);
	@WebMethod ListWrapper<String> update(ComputerDTO computerDTO);
	@WebMethod void delete(long id);
	@WebMethod PageWrapper<ComputerDTO> getPagedList(int page, int size);
}
