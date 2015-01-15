package com.excilys.computerdatabase.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.service.CompanyDBServiceI;
import com.excilys.computerdatabase.service.ComputerDBServiceI;
import com.excilys.computerdatabase.service.impl.CompanyDBService;
import com.excilys.computerdatabase.service.impl.ComputerDBService;
import com.excilys.computerdatabase.utils.Validator;

@WebServlet("/addComputer")
public class AddComputerController extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ComputerDBServiceI computerDBService = ComputerDBService.getInstance();
	private CompanyDBServiceI companyDBService = CompanyDBService.getInstance();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		List<Company> companies = companyDBService.getAll();
		req.setAttribute("companies", companies);
		
		// Get the JSP dispatcher
		RequestDispatcher dispatcher = req.getRequestDispatcher("WEB-INF/views/addComputer.jsp");
		// Forward the request
		dispatcher.forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		Computer computer = populateComputer(req);
		
		if (computer != null) {
			computerDBService.create(computer);
		}
		
		
		doGet(req, resp);
	}

	private Computer populateComputer(HttpServletRequest req) {
		Computer.Builder builder = Computer.builder();
		String name = req.getParameter("computerName");
		String iDate = req.getParameter("introducedDate");
		String dDate = req.getParameter("discontinuedDate");
		String companyId = req.getParameter("companyId");
		
		Map<String,String> errorMsgMap = new HashMap<String,String>();
		
		if (Validator.isName(name)) {
			builder.name(name);
		} else {
			errorMsgMap.put("name", "Incorrect name : a name can't be empty or only spaces");
		}
		
		if (iDate != null && !iDate.trim().isEmpty()) {
			if (Validator.isDate(iDate)) {
				builder.introducedDate(LocalDate.parse(iDate, DateTimeFormatter.ISO_LOCAL_DATE));
			} else {
				errorMsgMap.put("iDate", "Incorrect date : the field must be at the yyyy-MM-dd format or left empty");
			}
		}
		
		if (dDate != null && !dDate.trim().isEmpty()) {
			if (Validator.isDate(dDate)) {
				builder.discontinuedDate(LocalDate.parse(dDate, DateTimeFormatter.ISO_LOCAL_DATE));
			} else {
				errorMsgMap.put("dDate", "Incorrect date : the field must be at the yyyy-MM-dd format or left empty");
			}
		}
		
		if (companyId != null && !companyId.trim().isEmpty()) {
			if (Validator.isLong(companyId)) {
				Company company = companyDBService.getById(Long.valueOf(companyId));
				
				if (company != null) {
					builder.company(company);
				}
				
			} else {
				errorMsgMap.put("companyId", "Incorrect Company identifier");
			}
		}
		
		if (errorMsgMap.isEmpty()) {
			return builder.build();
		}
		req.setAttribute("error", errorMsgMap);
		return null;
	}
}
