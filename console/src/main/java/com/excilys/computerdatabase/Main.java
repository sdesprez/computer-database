package com.excilys.computerdatabase;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.excilys.computerdatabase.cli.CLI;
import com.excilys.computerdatabase.webservice.CompanyWebService;
import com.excilys.computerdatabase.webservice.ComputerWebService;

public class Main {

	public static void main(final String[] args) throws MalformedURLException {
		URL computerUrl = new URL("http://localhost:8888/computer-database/webservice/computer?wsdl");
		URL companyUrl = new URL("http://localhost:8888/computer-database/webservice/company?wsdl");
		
		QName qname = new QName("http://impl.webservice.computerdatabase.excilys.com/", "CompanyWebServiceImplService");
		Service service = Service.create(companyUrl, qname);
		
		CompanyWebService companyWebService = service.getPort(CompanyWebService.class);
		
		qname = new QName("http://impl.webservice.computerdatabase.excilys.com/", "ComputerWebServiceImplService");
		service = Service.create(computerUrl, qname);
		
		ComputerWebService computerWebService = service.getPort(ComputerWebService.class);
		
		final CLI cli = new CLI(computerWebService, companyWebService);
		cli.mainMenu();
		
	}
}
