package com.excilys.computerdatabase.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.dto.ComputerDTO;
import com.excilys.computerdatabase.dto.ComputerDTOConverter;
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.service.ComputerDBService;
import com.excilys.computerdatabase.service.impl.CompanyDBServiceImpl;
import com.excilys.computerdatabase.service.impl.ComputerDBServiceImpl;
import com.excilys.computerdatabase.utils.Validator;

@WebServlet("/add-computer")
public class AddComputerController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ComputerDBService computerDBService = ComputerDBServiceImpl.INSTANCE;
	private CompanyDBService companyDBService = CompanyDBServiceImpl.INSTANCE;
	
	private static final String COMPANIES = "companies";
	private static final String COMPUTER_NAME = "computerName";
	private static final String INTRODUCED_DATE = "introducedDate";
	private static final String DISCONTINUED_DATE = "discontinuedDate";
	private static final String COMPANY_ID = "companyId";
	private static final String ERROR = "error";
	
	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		
		final List<Company> companies = companyDBService.getAll();
		req.setAttribute(COMPANIES, companies);
		
		// Get the JSP dispatcher
		final RequestDispatcher dispatcher = req.getRequestDispatcher("WEB-INF/views/addComputer.jsp");
		// Forward the request
		dispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		final Map<String, String> error = new HashMap<String, String>();
		final ComputerDTO.Builder builder = ComputerDTO.builder().name(req.getParameter(COMPUTER_NAME))
																.introduced(req.getParameter(INTRODUCED_DATE))
																.discontinued(req.getParameter(DISCONTINUED_DATE));
		
		if (Validator.isPositiveLong(req.getParameter(COMPANY_ID))) {
			builder.company(Long.valueOf(req.getParameter(COMPANY_ID)));
		} else {
			error.put(COMPANY_ID, "Incorrect Company identifier");
		}
		
		final ComputerDTO dto = builder.build();
		
		ComputerDTOConverter.validate(dto, error);
		
		if (error.isEmpty()) {
			computerDBService.create(ComputerDTOConverter.fromDTO(dto));
			resp.sendRedirect("dashboard");
		} else {
			req.setAttribute(ERROR, error);
			doGet(req, resp);
		}
	}
}
