package com.excilys.computerdatabase;

import javax.xml.ws.Endpoint;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.service.ComputerDBService;
import com.excilys.computerdatabase.webservice.impl.CompanyWebServiceImpl;
import com.excilys.computerdatabase.webservice.impl.ComputerWebServiceImpl;


public class DeployWebService {

	
	public static void main(String[] args) {
		System.out.println("Serveur en cours de déploiement...");
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("service-context.xml");
		ComputerDBService computerDBService = (ComputerDBService) context.getBean(ComputerDBService.class);
		CompanyDBService companyDBService = (CompanyDBService) context.getBean(CompanyDBService.class);
		
		Endpoint.publish("http://localhost:8888/computer-database/webservice/company", new CompanyWebServiceImpl(companyDBService));
		Endpoint.publish("http://localhost:8888/computer-database/webservice/computer", new ComputerWebServiceImpl(computerDBService, companyDBService));
		System.out.println("Serveur en ligne");
	}
}
