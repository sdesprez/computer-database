package com.excilys.computerdatabase.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.service.impl.CompanyDBService;
import com.excilys.computerdatabase.service.impl.ComputerDBService;
import com.excilys.computerdatabase.utils.Validator;

public class ComputerHttpService {

	private static ComputerDBServiceI computerDBService = ComputerDBService.getInstance();
	private static CompanyDBServiceI companyDBService = CompanyDBService.getInstance();	
	
	public static Computer populate(HttpServletRequest req) {
		final Computer.Builder builder = Computer.builder();
		String name = req.getParameter("computerName");
		String iDate = req.getParameter("introducedDate");
		String dDate = req.getParameter("discontinuedDate");
		String companyId = req.getParameter("companyId");
		
		Map<String, String> errorMsgMap = new HashMap<String, String>();
		
		//Check if the name is a valid Name
		if (Validator.isName(name)) {
			builder.name(name);
		} else {
			errorMsgMap.put("name", "Incorrect name : a name can't be empty or only spaces");
		}
		
		//Check if the introduced date is valid
		if (iDate != null && !iDate.trim().isEmpty()) {
			if (Validator.isDate(iDate)) {
				builder.introducedDate(LocalDate.parse(iDate, DateTimeFormatter.ISO_LOCAL_DATE));
			} else {
				errorMsgMap.put("iDate", "Incorrect date : the field must be at the yyyy-MM-dd format or left empty");
			}
		}
		
		//Check if the discontinued date is valid
		if (dDate != null && !dDate.trim().isEmpty()) {
			if (Validator.isDate(dDate)) {
				builder.discontinuedDate(LocalDate.parse(dDate, DateTimeFormatter.ISO_LOCAL_DATE));
			} else {
				errorMsgMap.put("dDate", "Incorrect date : the field must be at the yyyy-MM-dd format or left empty");
			}
		}
		
		//Check if the company id is valid
		if (companyId != null && !companyId.trim().isEmpty()) {
			if (Validator.isPositiveLong(companyId)) {
				Company company = companyDBService.getById(Long.valueOf(companyId));
				
				//Check if a company with this id exist in the database 
				if (company != null) {
					builder.company(company);
				}
				
			} else {
				errorMsgMap.put("companyId", "Incorrect Company identifier");
			}
		}
		
		//Return a computer if there was no error
		if (errorMsgMap.isEmpty()) {
			return builder.build();
		}
		
		//Return null if there was an error and set a Map of errors as an Attribute in the request
		req.setAttribute("error", errorMsgMap);
		return null;
	}
	
	
	public static Computer update(HttpServletRequest req) {		
		Map<String, String> errorMsgMap = new HashMap<String, String>();
		
		//Get the id
		String idString = req.getParameter("id");
	
		//Check if the idString is a valid id
		if (!Validator.isPositiveLong(idString)) {
			errorMsgMap.put("id", "Incorrect id : an id should be a long");
			req.setAttribute("error", errorMsgMap);
			return null;
		}

		Long id = Long.valueOf(idString);
		
		//Check if a computer with this id exist in the database
		if (computerDBService.getById(id) == null) {
			errorMsgMap.put("id", "No computer with this id was found");
			req.setAttribute("error", errorMsgMap);
			return null;
		}
		
		//Create a computer with the informations in the request
		Computer computer = populate(req);
		
		//Check if the computer was created
		if (computer == null) {
			return null;
		}
		
		computer.setId(id);
		return computer;
	}
}
