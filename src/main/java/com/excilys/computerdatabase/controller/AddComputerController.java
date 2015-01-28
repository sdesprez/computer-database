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
import com.excilys.computerdatabase.dto.ComputerDTO;
import com.excilys.computerdatabase.dto.ComputerDTOConverter;
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.service.ComputerDBService;
import com.excilys.computerdatabase.utils.Validator;

@Controller
public class AddComputerController {


	@Autowired
	private ComputerDBService computerDBService;
	@Autowired
	private CompanyDBService companyDBService;
	
	private static final String COMPANIES = "companies";
	private static final String COMPUTER_NAME = "computerName";
	private static final String INTRODUCED_DATE = "introducedDate";
	private static final String DISCONTINUED_DATE = "discontinuedDate";
	private static final String COMPANY_ID = "companyId";
	private static final String ERROR = "error";
	
	
	@RequestMapping(value = "/add-computer", method = RequestMethod.GET)
	protected String doGet(final HttpServletRequest req) {
		
		final List<Company> companies = companyDBService.getAll();
		req.setAttribute(COMPANIES, companies);
		
		return "addComputer";
	}

	@RequestMapping(value = "/add-computer", method = RequestMethod.POST)
	protected String doPost(final HttpServletRequest req) {
		final Map<String, String> error = new HashMap<String, String>();
		final ComputerDTO.Builder builder = ComputerDTO.builder().name(req.getParameter(COMPUTER_NAME))
																.introduced(req.getParameter(INTRODUCED_DATE))
																.discontinued(req.getParameter(DISCONTINUED_DATE));
		
		if (Validator.isPositiveLong(req.getParameter(COMPANY_ID))) {
			builder.company(Long.valueOf(req.getParameter(COMPANY_ID)));
		} else {
			error.put(COMPANY_ID, "Incorrect Company identifier");
		}
		
		final ComputerDTO dto = builder.build();
		
		ComputerDTOConverter.validate(dto, error);
		
		if (error.isEmpty()) {
			computerDBService.create(ComputerDTOConverter.fromDTO(dto, companyDBService));
			return "redirect:/dashboard";
		} else {
			req.setAttribute(ERROR, error);
			return "addComputer";
		}
	}
}
