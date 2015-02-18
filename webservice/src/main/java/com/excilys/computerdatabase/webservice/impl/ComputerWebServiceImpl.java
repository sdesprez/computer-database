package com.excilys.computerdatabase.webservice.impl;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.dto.ComputerDTO;
import com.excilys.computerdatabase.dto.ComputerDTOConverter;
import com.excilys.computerdatabase.dto.ComputerDTOValidator;
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.service.ComputerDBService;
import com.excilys.computerdatabase.webservice.ComputerWebService;
import com.excilys.computerdatabase.wrapper.ListWrapper;
import com.excilys.computerdatabase.wrapper.PageWrapper;

@WebService(endpointInterface="com.excilys.computerdatabase.webservice.ComputerWebService")
public class ComputerWebServiceImpl implements ComputerWebService {

	private ComputerDBService computerDBService;
	private CompanyDBService companyDBService;
	
	private ComputerDTOValidator computerDTOValidator;
	
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	public ComputerWebServiceImpl(ComputerDBService computerDBService, CompanyDBService companyDBService) {
		this.computerDBService = computerDBService;
		this.companyDBService = companyDBService;
		this.computerDTOValidator = new ComputerDTOValidator();
	}

	@Override
	public ComputerDTO getById(long id) {
		Computer computer = computerDBService.getById(id);
		if (computer == null) {
			return new ComputerDTO();
		}
		return ComputerDTOConverter.toDTO(computer, DATE_FORMAT);
	}
	
	@Override
	public @WebMethod ListWrapper<ComputerDTO> getAll() {
		ListWrapper<ComputerDTO> list = new ListWrapper<ComputerDTO>();
		list.setItems(ComputerDTOConverter.toDTO(computerDBService.getAll(), DATE_FORMAT));
		return list;
	}

	@Override
	public ListWrapper<String> create(ComputerDTO computerDTO) {
		List<String> errors = computerDTOValidator.validate(computerDTO);
		
		if (computerDTO.getCompany() != 0 && companyDBService.getById(computerDTO.getCompany()) == null) {
			errors.add("The company " + computerDTO.getCompany() + " doesn't exist");
		}
		
		if (errors.isEmpty()) {
			Computer computer = ComputerDTOConverter.fromDTO(computerDTO, companyDBService, DATE_FORMAT);
			computerDBService.create(computer);
		}
		
		ListWrapper<String> wrapper = new ListWrapper<String>();
		wrapper.setItems(errors);
		return wrapper;
	}

	@Override
	public ListWrapper<String> update(ComputerDTO computerDTO) {
		List<String> errors = computerDTOValidator.validate(computerDTO);
		
		if (computerDTO.getCompany() != 0 && companyDBService.getById(computerDTO.getCompany()) == null) {
			errors.add("The company " + computerDTO.getCompany() + " doesn't exist");
		}
		
		if (errors.isEmpty()) {
			Computer computer = ComputerDTOConverter.fromDTO(computerDTO, companyDBService, DATE_FORMAT);
			computerDBService.update(computer);
		}
		
		ListWrapper<String> wrapper = new ListWrapper<String>();
		wrapper.setItems(errors);
		return wrapper;
	}

	@Override
	public PageWrapper<ComputerDTO> getPagedList(int page, int size) {
		Pageable pageable = new PageRequest(page, size);
		Page<Computer> p = computerDBService.getPagedList("", pageable);
		
		PageWrapper<ComputerDTO> pageWrapper = new PageWrapper<ComputerDTO>();
		pageWrapper.setContent(ComputerDTOConverter.toDTO(p.getContent(), DATE_FORMAT));
		pageWrapper.setPage(p.getNumber());
		pageWrapper.setSize(p.getSize());
		pageWrapper.setTotalElements(p.getTotalElements());
		return pageWrapper;
	}

	@Override
	public void delete(long id) {
		computerDBService.delete(id);
	}
	
}
