package com.excilys.computerdatabase.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.service.impl.CompanyDBServiceImpl;
import com.excilys.computerdatabase.service.impl.ComputerDBServiceImpl;
import com.excilys.computerdatabase.utils.Validator;

public class ComputerHttpService {

	private static ComputerDBService computerDBService = ComputerDBServiceImpl.INSTANCE;
	private static CompanyDBService companyDBService = CompanyDBServiceImpl.INSTANCE;	
	
	private static final Pattern PATTERN = Pattern.compile("\\d{1,19}");
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerHttpService.class);
	
	
	/**
	 * Return a computer created from the informations contained in a HttpServletRequest
	 * If there was an error, set into the request a Map "errorMsgMap" who contain errors
	 * messages 
	 * @param req Request containing the informations of the computer
	 * @return A Computer if there was no errors 
	 * 			null if there was an error
	 */
	public static Computer create(final HttpServletRequest req) {
		final Computer.Builder builder = Computer.builder();
		final String name = req.getParameter("computerName");
		final String iDate = req.getParameter("introducedDate");
		final String dDate = req.getParameter("discontinuedDate");
		final String companyId = req.getParameter("companyId");
		
		final Map<String, String> errorMsgMap = new HashMap<String, String>();
		
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
				errorMsgMap.put("iDate", "The date is not valid, valid format is yyyy-MM-dd and between 1970-01-01 and 2038-01-18. You can also leave this field emtpy");
			}
		}
		
		//Check if the discontinued date is valid
		if (dDate != null && !dDate.trim().isEmpty()) {
			if (Validator.isDate(dDate)) {
				builder.discontinuedDate(LocalDate.parse(dDate, DateTimeFormatter.ISO_LOCAL_DATE));
			} else {
				errorMsgMap.put("dDate", "The date is not valid, valid format is yyyy-MM-dd and between 1970-01-01 and 2038-01-18. You can also leave this field emtpy");
			}
		}
		
		//Check if the company id is valid
		if (companyId != null && !companyId.trim().isEmpty()) {
			if (Validator.isPositiveLong(companyId)) {
				final Company company = companyDBService.getById(Long.valueOf(companyId));
				
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
	
	
	/**
	 * Return a computer with its data updated from the informations of a HttpServletRequest
	 * If there was an error, set into the request a Map "errorMsgMap" who contain errors
	 * messages 
	 * @param req Request containing the informations of the computer
	 * @return A Computer if there was no errors 
	 * 			null if there was an error
	 */
	public static Computer update(final HttpServletRequest req) {		
		final Map<String, String> errorMsgMap = new HashMap<String, String>();
		
		//Get the id
		final String idString = req.getParameter("id");
	
		//Check if the idString is a valid id
		if (!Validator.isPositiveLong(idString)) {
			errorMsgMap.put("id", "Incorrect id : an id should be a long");
			req.setAttribute("error", errorMsgMap);
			return null;
		}

		final Long id = Long.valueOf(idString);
		
		//Check if a computer with this id exist in the database
		if (computerDBService.getById(id) == null) {
			errorMsgMap.put("id", "No computer with this id was found");
			req.setAttribute("error", errorMsgMap);
			return null;
		}
		
		//Create a computer with the informations in the request
		final Computer computer = create(req);
		
		//Check if the computer was created
		if (computer == null) {
			return null;
		}
		
		computer.setId(id);
		return computer;
	}
	
	/**
	 * 
	 * @param req request containing the Ids of the computers to delete
	 */
	public static void delete(final HttpServletRequest req) {
		//Get the String containing the Ids of the computers to delete
		final String selection = req.getParameter("selection");
		
		//Create a matcher to find the positives longs in the String
		final Matcher m = PATTERN.matcher(selection);
		
		//For each long found, delete the computer
		final List<Long> list = new ArrayList<Long>();
		while (m.find()) {
			list.add(Long.valueOf(m.group()));
		}
		computerDBService.delete(list);
		LOGGER.info("Deletion of Computer with ids =" + list);
	}
}
