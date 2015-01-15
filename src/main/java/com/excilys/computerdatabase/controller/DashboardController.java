package com.excilys.computerdatabase.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.ComputerDBServiceI;
import com.excilys.computerdatabase.service.impl.ComputerDBService;
import com.excilys.computerdatabase.utils.Validator;

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private ComputerDBServiceI computerDBService = ComputerDBService.getInstance();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {		
		Page<Computer> page = new Page<Computer>();
		
		String intString = req.getParameter("page");
		int pageNumber = 0;
		if (Validator.isInt(intString)) {
			pageNumber = Integer.valueOf(intString);
		}
		if (pageNumber < 1) {
			page.setPageNumber(1);
		} else {
			page.setPageNumber(pageNumber);
		}
		
		page.setNbResultsPerPage(20);
		page = computerDBService.getPagedList(page);
		
		req.setAttribute("page", page);
		
		// Get the JSP dispatcher
		RequestDispatcher dispatcher = req.getRequestDispatcher("WEB-INF/views/dashboard.jsp");
		// Forward the request
		dispatcher.forward(req, resp);
	}

	
	
}
