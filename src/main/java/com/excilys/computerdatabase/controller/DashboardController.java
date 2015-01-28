package com.excilys.computerdatabase.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.excilys.computerdatabase.dao.impl.ColumnNames;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.ComputerDBService;
import com.excilys.computerdatabase.utils.Validator;

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
	protected String doGet(final HttpServletRequest req) {		
		Page<Computer> page = new Page<Computer>();
		
		final String intString = req.getParameter(PAGE);
		int pageNumber = 0;
		if (Validator.isPositiveInt(intString)) {
			pageNumber = Integer.valueOf(intString);
		}
		if (pageNumber < 1) {
			page.setPageNumber(1);
		} else {
			page.setPageNumber(pageNumber);
		}
		
		final String nbResultsString = req.getParameter(NB_RESULTS);
		int nbResults = 0;
		if (Validator.isPositiveInt(nbResultsString)) {
			nbResults = Integer.valueOf(nbResultsString);
		}
		if (nbResults < 10) {
			page.setNbResultsPerPage(10);
		} else {
			page.setNbResultsPerPage(nbResults);
		}
		
		final String search = req.getParameter(SEARCH);
		if (search == null) {
			page.setSearch("");
		} else {
			page.setSearch(search.trim());
		}
		
		final String sort = req.getParameter(SORT);
		ColumnNames cName = ColumnNames.getInstance(sort);
		
		if (cName == null) {
			cName = ColumnNames.ID;
		}
		page.setSort(cName);
		
		final String order = req.getParameter(ORDER);
		if (order != null && (order.compareToIgnoreCase(ASC) == 0 || order.compareToIgnoreCase(DESC) == 0)) {
			page.setOrder(order.toUpperCase());
		}
		
		page = computerDBService.getPagedList(page);
		
		req.setAttribute(PAGE, page);
		
		return "dashboard";
	}

	
	
}
