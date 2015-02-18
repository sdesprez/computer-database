package com.excilys.computerdatabase.endpoint;

import javax.xml.ws.Endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.service.ComputerDBService;
import com.excilys.computerdatabase.webservice.impl.CompanyWebServiceImpl;
import com.excilys.computerdatabase.webservice.impl.ComputerWebServiceImpl;

public class DeployWebService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeployWebService.class);
	private static final String COMPUTER_ENDPOINT_URL = "http://localhost:8888/computer-database/webservice/company";
	private static final String COMPANY_ENDPOINT_URL = "http://localhost:8888/computer-database/webservice/computer";
	
	
	public static void main(String[] args) {
		LOGGER.info("Serveur en cours de déploiement...");
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("service-context.xml");
		
		ComputerDBService computerDBService = (ComputerDBService) context.getBean(ComputerDBService.class);
		CompanyDBService companyDBService = (CompanyDBService) context.getBean(CompanyDBService.class);
		
		Endpoint.publish(COMPUTER_ENDPOINT_URL, new CompanyWebServiceImpl(companyDBService));
		Endpoint.publish(COMPANY_ENDPOINT_URL, new ComputerWebServiceImpl(computerDBService, companyDBService));
		
		LOGGER.info("Serveur en ligne");
	}
}