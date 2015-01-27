package com.excilys.computerdatabase.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.computerdatabase.service.ComputerDBService;

@WebServlet("/delete")
public class DeleteComputerController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ComputerDBService computerDBService;
	
	private static final Pattern PATTERN = Pattern.compile("\\d{1,19}");
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteComputerController.class);
	
	@Override
	public void init() throws ServletException {
		super.init();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {

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
		
		
		resp.sendRedirect("dashboard");
	}

	
	
}
