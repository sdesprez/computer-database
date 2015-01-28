package com.excilys.computerdatabase.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.dto.ComputerDTO;
import com.excilys.computerdatabase.dto.ComputerDTOConverter;
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.service.ComputerDBService;
import com.excilys.computerdatabase.utils.Validator;

@Controller
public class EditComputerController {

	
	@Autowired
	private ComputerDBService computerDBService;
	@Autowired
	private CompanyDBService companyDBService;
	
	private static final String ID = "id";
	private static final String COMPUTER = "computer";
	private static final String COMPANIES = "companies";
	private static final String COMPUTER_NAME = "computerName";
	private static final String INTRODUCED_DATE = "introducedDate";
	private static final String DISCONTINUED_DATE = "discontinuedDate";
	private static final String COMPANY_ID = "companyId";
	private static final String ERROR = "error";

	
	@RequestMapping(value = "/edit-computer", method = RequestMethod.GET)
	protected String doGet(final HttpServletRequest req) {
		long id = 0;
		final String idString = req.getParameter(ID);
		if (Validator.isPositiveLong(idString)) {
			id = Long.valueOf(idString);
			
			final Computer computer = computerDBService.getById(id);		
			req.setAttribute(COMPUTER, computer);
		}
		
		final List<Company> companies = companyDBService.getAll();
		req.setAttribute(COMPANIES, companies);
		
		return "editComputer";
	}

	@RequestMapping(value = "/edit-computer", method = RequestMethod.POST)
	protected String doPost(final HttpServletRequest req) {		
		final Map<String, String> error = new HashMap<String, String>();
		final ComputerDTO.Builder builder = ComputerDTO.builder().name(req.getParameter(COMPUTER_NAME))
																.introduced(req.getParameter(INTRODUCED_DATE))
																.discontinued(req.getParameter(DISCONTINUED_DATE));
		
		if (Validator.isPositiveLong(req.getParameter(ID))) {
			builder.id(Long.valueOf(req.getParameter(ID)));
		} else {
			error.put(ID, "Incorrect id : an id should be a long");
		}
		
		if (Validator.isPositiveLong(req.getParameter(COMPANY_ID))) {
			builder.company(Long.valueOf(req.getParameter(COMPANY_ID)));
		} else {
			error.put(COMPANY_ID, "Incorrect Company identifier");
		}
		
		final ComputerDTO dto = builder.build();
		
		ComputerDTOConverter.validate(dto, error);
		
		if (error.isEmpty()) {
			computerDBService.update(ComputerDTOConverter.fromDTO(dto, companyDBService));
			return "redirect:/dashboard";
		} else {
			req.setAttribute(ERROR, error);
			return "editComputer";
		}
	}
	
	

}
