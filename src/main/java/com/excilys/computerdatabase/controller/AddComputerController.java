package com.excilys.computerdatabase.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.service.CompanyDBServiceI;
import com.excilys.computerdatabase.service.ComputerDBServiceI;
import com.excilys.computerdatabase.service.ComputerHttpService;
import com.excilys.computerdatabase.service.impl.CompanyDBService;
import com.excilys.computerdatabase.service.impl.ComputerDBService;

@WebServlet("/addComputer")
public class AddComputerController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ComputerDBServiceI computerDBService = ComputerDBService.INSTANCE;
	private CompanyDBServiceI companyDBService = CompanyDBService.INSTANCE;
	private Logger logger = LoggerFactory.getLogger(AddComputerController.class);
	
	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		
		final List<Company> companies = companyDBService.getAll();
		req.setAttribute("companies", companies);
		
		// Get the JSP dispatcher
		final RequestDispatcher dispatcher = req.getRequestDispatcher("WEB-INF/views/addComputer.jsp");
		// Forward the request
		dispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		
		final Computer computer = ComputerHttpService.populate(req);
		
		if (computer != null) {
			computerDBService.create(computer);
			logger.info(computer + " Added to database");
			resp.sendRedirect("dashboard");
		} else {
			doGet(req, resp);
		}
	}
}
