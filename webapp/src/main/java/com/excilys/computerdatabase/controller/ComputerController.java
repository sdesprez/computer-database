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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.OrderBy;
import com.excilys.computerdatabase.dto.ComputerDTO;
import com.excilys.computerdatabase.dto.ComputerDTOConverter;
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.service.ComputerDBService;


@Controller
public class ComputerController {

	private static final String ID = "id";
	private static final String PAGE = "page";
	private static final String DIRECTION = "direction";
	private static final String SEARCH = "search";
	private static final String SORT = "sort";
	private static final String COMPANIES = "companies";
	private static final Pattern PATTERN = Pattern.compile("\\d{1,19}");
	private static final String SELECT = "selection";
	
	private static final String[][] COLUMNS = {{"name", "column.name"}, {"introduced", "column.introduced"}, 
												{"discontinued", "column.discontinued"}, {"company.name", "column.company"}}; 
	
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
								@PageableDefault(page = 0, size = 20) final Pageable pageable, 
								@RequestParam(value = SEARCH, required = false, defaultValue = "") final String search) {
		final Page<Computer> page = computerDBService.getPagedList(search, pageable);
		final Page<ComputerDTO> pageDTO = new PageImpl<ComputerDTO>(ComputerDTOConverter.toDTO(page.getContent(), messageSourceAccessor.getMessage("dateFormat")), pageable, page.getTotalElements());
		model.addAttribute(SEARCH, search);
		model.addAttribute(PAGE, pageDTO);
		model.addAttribute("columns", COLUMNS);
		if (page.getSort() != null && OrderBy.getOrderByFromSort(page.getSort()) != null) {
			model.addAttribute(SORT, OrderBy.getOrderByFromSort(page.getSort()).getColName());
			model.addAttribute(DIRECTION, OrderBy.getOrderByFromSort(page.getSort()).getDir());
		}
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
		model.addAttribute("computerDTO", ComputerDTOConverter.toDTO(computerDBService.getById(id), messageSourceAccessor.getMessage("dateFormat")));
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
