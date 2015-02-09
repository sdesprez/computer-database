package com.excilys.computerdatabase.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.excilys.computerdatabase.dto.ComputerDTO;
import com.excilys.computerdatabase.dto.ComputerDTOConverter;
import com.excilys.computerdatabase.dto.ComputerDTOValidator;
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.service.ComputerDBService;

@Controller
public class AddComputerController {


	@Autowired
	private ComputerDBService computerDBService;
	@Autowired
	private CompanyDBService companyDBService;
	
	private static final String COMPANIES = "companies";
	
	@InitBinder("computerDTO")
	protected void initComputerDTOBinder(final WebDataBinder binder) {
		binder.setValidator(new ComputerDTOValidator());
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	protected String getAdd(final Model model) {
		model.addAttribute("computerDTO", new ComputerDTO());
		model.addAttribute(COMPANIES, companyDBService.getAll());
		
		return "addComputer";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	protected String createComputer(final Model model, @Valid final ComputerDTO computerDTO, final BindingResult result) {		
		if (!result.hasErrors()) {
			computerDBService.create(ComputerDTOConverter.fromDTO(computerDTO, companyDBService));
			return "redirect:/dashboard";
		} else {
			model.addAttribute(COMPANIES, companyDBService.getAll());
			return "addComputer";
		}
	}
}
