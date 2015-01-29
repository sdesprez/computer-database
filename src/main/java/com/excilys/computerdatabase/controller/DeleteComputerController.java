package com.excilys.computerdatabase.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.excilys.computerdatabase.service.ComputerDBService;

@Controller
public class DeleteComputerController {

	@Autowired
	private ComputerDBService computerDBService;
	
	private static final Pattern PATTERN = Pattern.compile("\\d{1,19}");
	private static final String SELECT = "selection";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteComputerController.class);
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	protected String deleteComputers(@RequestParam(SELECT) final String selection) {
		//Create a matcher to find the positives longs in the String
		final Matcher m = PATTERN.matcher(selection);
		
		//For each long found, delete the computer
		final List<Long> list = new ArrayList<Long>();
		while (m.find()) {
			list.add(Long.valueOf(m.group()));
		}
		if (list.size() != 0) {
			computerDBService.delete(list);
			LOGGER.info("Deletion of Computer with ids ={}", list);
		}
		
		return "redirect:/dashboard";
	}

	
	
}
