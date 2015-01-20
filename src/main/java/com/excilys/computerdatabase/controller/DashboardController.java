package com.excilys.computerdatabase.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.computerdatabase.dao.impl.ColumnNames;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.ComputerDBServiceI;
import com.excilys.computerdatabase.service.impl.ComputerDBService;
import com.excilys.computerdatabase.utils.Validator;

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	
	private ComputerDBServiceI computerDBService = ComputerDBService.getInstance();

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {		
		Page<Computer> page = new Page<Computer>();
		
		final String intString = req.getParameter("page");
		int pageNumber = 0;
		if (Validator.isPositiveInt(intString)) {
			pageNumber = Integer.valueOf(intString);
		}
		if (pageNumber < 1) {
			page.setPageNumber(1);
		} else {
			page.setPageNumber(pageNumber);
		}
		
		final String nbResultsString = req.getParameter("nbResults");
		int nbResults = 0;
		if (Validator.isPositiveInt(nbResultsString)) {
			nbResults = Integer.valueOf(nbResultsString);
		}
		if (nbResults < 10) {
			page.setNbResultsPerPage(10);
		} else {
			page.setNbResultsPerPage(nbResults);
		}
		
		final String search = req.getParameter("search");
		if (search == null) {
			page.setSearch("");
		} else {
			page.setSearch(search.trim());
		}
		
		final String order = req.getParameter("order");
		ColumnNames cName = ColumnNames.getInstance(order);
		
		if (cName == null) {
			cName = ColumnNames.ID;
		}
		page.setOrder(cName);
		page = computerDBService.getPagedList(page);
		
		req.setAttribute("page", page);
		
		// Get the JSP dispatcher
		final RequestDispatcher dispatcher = req.getRequestDispatcher("WEB-INF/views/dashboard.jsp");
		// Forward the request
		dispatcher.forward(req, resp);
	}

	
	
}
