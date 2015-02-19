package com.excilys.computerdatabase.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.dto.ComputerDTO;
import com.excilys.computerdatabase.dto.ComputerDTOConverter;
import com.excilys.computerdatabase.dto.ComputerDTOValidator;
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.service.ComputerDBService;

@RestController
@RequestMapping("/computer")
public class ComputerController {

	@Autowired
	private ComputerDBService computerDBService;
	
	@Autowired
	private CompanyDBService companyDBService;
	
	@Autowired
	private ComputerDTOValidator computerDTOValidator;
	
	@InitBinder("computerDTO")
	protected void initComputerDTOBinder(final WebDataBinder binder) {
		binder.setValidator(computerDTOValidator);
	}
	
	
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	@RequestMapping(value = "/page/{page}/size/{size}", method = RequestMethod.GET)
	public Page<ComputerDTO> getComputers(@PathVariable final int page, 
											@PathVariable final int size) {
		Pageable pageable = new PageRequest(page, size);
		Page<Computer> p = computerDBService.getPagedList("", pageable);
		return new PageImpl<ComputerDTO>(ComputerDTOConverter.toDTO(p.getContent(), DATE_FORMAT), pageable, p.getTotalElements());
	}
	
	@RequestMapping(value = "/page/{page}/size/{size}/search/{search}", method = RequestMethod.GET)
	public Page<ComputerDTO> searchComputers(@PathVariable final int page, 
											@PathVariable final int size, 
											@PathVariable final String search ) {
		Pageable pageable = new PageRequest(page, size);
		Page<Computer> p = computerDBService.getPagedList(search, pageable);
		return new PageImpl<ComputerDTO>(ComputerDTOConverter.toDTO(p.getContent(), DATE_FORMAT), pageable, p.getTotalElements());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ComputerDTO getComputerById(@PathVariable final int id) {
		return ComputerDTOConverter.toDTO(computerDBService.getById(id), DATE_FORMAT);
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public String createComputer(@RequestBody @Valid final ComputerDTO computerDTO, final BindingResult results) {
		if (!results.hasErrors()) {
			computerDBService.create(ComputerDTOConverter.fromDTO(computerDTO, companyDBService, DATE_FORMAT));
			return "Computer added";
		}
		return "Error in the computer";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String updateComputer(@RequestBody @Valid final ComputerDTO computerDTO, final BindingResult results) {
		if (!results.hasErrors()) {
			computerDBService.update(ComputerDTOConverter.fromDTO(computerDTO, companyDBService, DATE_FORMAT));
			return "Computer updated";
		}
		return "Error in the computer";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String deleteComputer(@PathVariable final long id) {
		computerDBService.delete(id);
		return "delete";
	}
}
