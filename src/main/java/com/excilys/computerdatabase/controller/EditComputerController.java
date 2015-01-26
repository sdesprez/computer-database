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
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.service.ComputerDBService;
import com.excilys.computerdatabase.service.ComputerHttpService;
import com.excilys.computerdatabase.service.impl.CompanyDBServiceImpl;
import com.excilys.computerdatabase.service.impl.ComputerDBServiceImpl;
import com.excilys.computerdatabase.utils.Validator;

@WebServlet("/edit-computer")
public class EditComputerController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private ComputerDBService computerDBService = ComputerDBServiceImpl.INSTANCE;
	private CompanyDBService companyDBService = CompanyDBServiceImpl.INSTANCE;
	private Logger logger = LoggerFactory.getLogger(EditComputerController.class);
	
	private static final String ID = "id";
	private static final String COMPUTER = "computer";
	private static final String COMPANIES = "companies";

	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		long id = 0;
		final String idString = req.getParameter(ID);
		if (Validator.isPositiveLong(idString)) {
			id = Long.valueOf(idString);
			
			final Computer computer = computerDBService.getById(id);		
			req.setAttribute(COMPUTER, computer);
		}
		
		final List<Company> companies = companyDBService.getAll();
		req.setAttribute(COMPANIES, companies);
		
		// Get the JSP dispatcher
		final RequestDispatcher dispatcher = req.getRequestDispatcher("WEB-INF/views/editComputer.jsp");
		// Forward the request
		dispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {		
		final Computer computer = ComputerHttpService.update(req);
		
		if (computer != null) {
			computerDBService.update(computer);
			logger.info(computer + " was updated");
			resp.sendRedirect("dashboard");
		} else {
			doGet(req, resp);
		}
	}
	
	

}
