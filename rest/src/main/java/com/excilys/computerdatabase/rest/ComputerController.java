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

/**
 * Rest controller for the Computer functions
 */
@RestController
@RequestMapping("/computer")
public class ComputerController {

	@Autowired
	private ComputerDBService computerDBService;
	
	@Autowired
	private CompanyDBService companyDBService;
	
	@Autowired
	private ComputerDTOValidator computerDTOValidator;
	
	/**
	 * Set the ComputerDTO validator to use an instance of ComputerDTOValidator
	 * @param binder : 
	 */
	@InitBinder("computerDTO")
	protected void initComputerDTOBinder(final WebDataBinder binder) {
		binder.setValidator(computerDTOValidator);
	}
	
	
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	
	/**
	 * Get a Page of ComputerDTO
	 * @param pageNumber the page number (0 based)
	 * @param size the max number of elements of a page
	 * @return A Page of ComputerDTO
	 */
	@RequestMapping(value = "/page/{page}/size/{size}", method = RequestMethod.GET)
	public Page<ComputerDTO> getComputers(@PathVariable final int pageNumber, 
											@PathVariable final int size) {
		return searchComputers(pageNumber, size, "");
	}
	
	/**
	 * Get a Page of ComputerDTO with a search parameter
	 * @param pageNumber the page number (0 based)
	 * @param size the max number of elements of a page
	 * @param search the search parameter
	 * @return A Page of ComputerDTO
	 */
	@RequestMapping(value = "/page/{page}/size/{size}/search/{search}", method = RequestMethod.GET)
	public Page<ComputerDTO> searchComputers(@PathVariable final int pageNumber, 
											@PathVariable final int size, 
											@PathVariable final String search ) {
		//Create a Pageable from the request
		Pageable pageable = new PageRequest(pageNumber, size);
		//Get a result Page of Computers from the service
		Page<Computer> page = computerDBService.getPagedList(search, pageable);
		//Convert the Page<Computer> to a Page<ComputerDTO> before returning it
		return new PageImpl<ComputerDTO>(ComputerDTOConverter.toDTO(page.getContent(), DATE_FORMAT), pageable, page.getTotalElements());
	}
	
	/**
	 * Get a Computer from its id
	 * @param id Id of the computer
	 * @return the Computer or null if none was found
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ComputerDTO getComputerById(@PathVariable final int id) {
		//Get the computer from the service, convert it to a Dto then return it
		return ComputerDTOConverter.toDTO(computerDBService.getById(id), DATE_FORMAT);
	}
	
	/**
	 * Add the computerDTO to the database after having check if its a valid ComputerDTO
	 * @param computerDTO ComputerDTO to add to the database
	 * @param results Contains the error codes of the validation
	 * @return A state message
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public String createComputer(@RequestBody @Valid final ComputerDTO computerDTO, final BindingResult results) {
		if (!results.hasErrors()) {
			computerDBService.create(ComputerDTOConverter.fromDTO(computerDTO, DATE_FORMAT));
			return "Computer added";
		}
		return "Error in the computer";
	}
	
	/**
	 * Update the computerDTO to the database after having check if its a valid ComputerDTO
	 * @param computerDTO ComputerDTO to update to the database
	 * @param results Contains the error codes of the validation
	 * @return A state message
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String updateComputer(@RequestBody @Valid final ComputerDTO computerDTO, final BindingResult results) {
		if (!results.hasErrors()) {
			computerDBService.update(ComputerDTOConverter.fromDTO(computerDTO, DATE_FORMAT));
			return "Computer updated";
		}
		return "Error in the computer";
	}
	
	/**
	 * Get a Computer from its id
	 * @param id Id of the computer to delete
	 * @return Notification of deletion
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String deleteComputer(@PathVariable final long id) {
		computerDBService.delete(id);
		return "delete";
	}
}
