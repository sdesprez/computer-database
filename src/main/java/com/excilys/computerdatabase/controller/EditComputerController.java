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
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.dto.ComputerDTO;
import com.excilys.computerdatabase.dto.ComputerDTOConverter;
import com.excilys.computerdatabase.dto.ComputerDTOValidator;
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.service.ComputerDBService;

@Controller
public class EditComputerController {

	
	@Autowired
	private ComputerDBService computerDBService;
	@Autowired
	private CompanyDBService companyDBService;
	
	private static final String ID = "id";
	private static final String COMPANIES = "companies";

	@InitBinder("computerDTO")
	protected void initComputerDTOBinder(final WebDataBinder binder) {
		binder.setValidator(new ComputerDTOValidator());
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	protected String getEdit(final Model model, @RequestParam(ID) final long id) {
		model.addAttribute(COMPANIES, companyDBService.getAll());
		model.addAttribute("computerDTO", ComputerDTOConverter.toDTO(computerDBService.getById(id)));
		return "editComputer";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	protected String updateComputer(final Model model, @Valid final ComputerDTO computerDTO, final BindingResult result) {		
		if (!result.hasErrors()) {
			computerDBService.update(ComputerDTOConverter.fromDTO(computerDTO, companyDBService));
			return "redirect:/dashboard";
		} else {
			model.addAttribute(COMPANIES, companyDBService.getAll());
			return "editComputer";
		}
	}
	
	

}
