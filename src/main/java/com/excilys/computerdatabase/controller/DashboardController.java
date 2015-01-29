package com.excilys.computerdatabase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.dao.impl.ColumnNames;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.ComputerDBService;

@Controller
public class DashboardController {

	
	private static final String PAGE = "page";
	private static final String NB_RESULTS = "nbResults";
	private static final String SORT = "sort";
	private static final String SEARCH = "search";
	private static final String ORDER = "order";
	private static final String ASC = "ASC";
	private static final String DESC = "DESC";
	
	@Autowired
	private ComputerDBService computerDBService;
	

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	protected String doGet(final Model model, 
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

	
	
}
