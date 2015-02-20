package com.excilys.computerdatabase.endpoint;

import javax.xml.ws.Endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.computerdatabase.webservice.CompanyWebService;
import com.excilys.computerdatabase.webservice.ComputerWebService;

/**
 * Entry point to public the EndPoints of the webServices
 */
public class DeployWebService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeployWebService.class);
	private static final String COMPANY_ENDPOINT_URL = "http://localhost:8888/computer-database/webservice/company";
	private static final String COMPUTER_ENDPOINT_URL = "http://localhost:8888/computer-database/webservice/computer";
	
	
	public static void main(String[] args) {
		LOGGER.info("Serveur en cours de déploiement...");
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("webservice-context.xml");
		
		Endpoint.publish(COMPUTER_ENDPOINT_URL, (ComputerWebService) context.getBean(ComputerWebService.class));
		Endpoint.publish(COMPANY_ENDPOINT_URL, (CompanyWebService) context.getBean(CompanyWebService.class));
		
		LOGGER.info("Serveur en ligne");
	}
}