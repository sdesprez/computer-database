package com.excilys.computerdatabase.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.dao.impl.ColumnNames;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.dto.ComputerDTO;
import com.excilys.computerdatabase.dto.ComputerDTOConverter;
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.service.ComputerDBService;


@Controller
public class ComputerController {

	private static final String ID = "id";
	private static final String PAGE = "page";
	private static final String NB_RESULTS = "nbResults";
	private static final String SORT = "sort";
	private static final String SEARCH = "search";
	private static final String ORDER = "order";
	private static final String ASC = "ASC";
	private static final String DESC = "DESC";
	private static final String COMPANIES = "companies";
	private static final Pattern PATTERN = Pattern.compile("\\d{1,19}");
	private static final String SELECT = "selection";
	
	@Autowired
	private CompanyDBService companyDBService;
	@Autowired
	private ComputerDBService computerDBService;
	
	@Autowired
	private Validator computerDTOValidator;
	
	private MessageSourceAccessor messageSourceAccessor;
	
	@InitBinder("computerDTO")
	protected void initComputerDTOBinder(final WebDataBinder binder) {
		binder.setValidator(computerDTOValidator);
	}
	
	@Autowired
    public void setMessageSource(final MessageSource messageSource) {
        this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
    }
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerController.class);
	

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	protected String dashboard(final Model model, 
							@RequestParam(value = PAGE, required = false, defaultValue = "1") final int pageNumber,
							@RequestParam(value = NB_RESULTS, required = false, defaultValue = "10") final int nbResults,
							@RequestParam(value = SORT, required = false, defaultValue = "1") final String sort,
							@RequestParam(value = SEARCH, required = false, defaultValue = "") final String search,
							@RequestParam(value = ORDER, required = false, defaultValue = "ASC") final String order) {	
		Page<Computer> page = new Page<Computer>();
		

		if (pageNumber < 1) {
			page.setPageNumber(1);
		} else {
			page.setPageNumber(pageNumber);
		}

		if (nbResults < 10) {
			page.setNbResultsPerPage(10);
		} else {
			page.setNbResultsPerPage(nbResults);
		}
		
		page.setSearch(search.trim());
		
		ColumnNames cName = ColumnNames.getInstance(sort);
		if (cName == null) {
			cName = ColumnNames.ID;
		}
		page.setSort(cName);
		
		if (order.compareToIgnoreCase(ASC) == 0 || order.compareToIgnoreCase(DESC) == 0) {
			page.setOrder(order.toUpperCase());
		}
		
		page = computerDBService.getPagedList(page);
		
		model.addAttribute(PAGE, page);
		
		return "dashboard";
	}
	
	
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	protected String getAdd(final Model model) {
		model.addAttribute("computerDTO", new ComputerDTO());
		model.addAttribute(COMPANIES, companyDBService.getAll());
		
		return "addComputer";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	protected String addComputer(final Model model, @Valid final ComputerDTO computerDTO, final BindingResult result) {		
		if (!result.hasErrors()) {
			computerDBService.create(ComputerDTOConverter.fromDTO(computerDTO, companyDBService, messageSourceAccessor.getMessage("dateFormat")));
			return "redirect:/dashboard";
		} else {
			model.addAttribute(COMPANIES, companyDBService.getAll());
			return "addComputer";
		}
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
			computerDBService.update(ComputerDTOConverter.fromDTO(computerDTO, companyDBService, messageSourceAccessor.getMessage("dateFormat")));
			return "redirect:/dashboard";
		} else {
			model.addAttribute(COMPANIES, companyDBService.getAll());
			return "editComputer";
		}
	}
	
	
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	protected String deleteComputers(@RequestParam(SELECT) final String selection) {
		//Create a matcher to find the positives longs in the String
		final Matcher m = PATTERN.matcher(selection);
		
		//For each long found, delete the computer
		final List<Long> list = new ArrayList<Long>();
		while (m.find()) {
			list.add(Long.valueOf(m.group()));
		}
		if (list.size() != 0) {
			computerDBService.delete(list);
			LOGGER.info("Deletion of Computer with ids ={}", list);
		}
		
		return "redirect:/dashboard";
	}
}
