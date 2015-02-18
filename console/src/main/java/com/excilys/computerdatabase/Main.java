package com.excilys.computerdatabase;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import com.excilys.computerdatabase.cli.CLI;
import com.excilys.computerdatabase.webservice.CompanyWebService;
import com.excilys.computerdatabase.webservice.ComputerWebService;

public class Main {
	
	private static final String COMPUTER_WSDL_URL = "http://localhost:8888/computer-database/webservice/computer?wsdl";
	private static final String COMPANY_WSDL_URL = "http://localhost:8888/computer-database/webservice/company?wsdl";
	private static final String QNAME_URL = "http://impl.webservice.computerdatabase.excilys.com/";

	public static void main(final String[] args) throws MalformedURLException {
		URL computerUrl = new URL(COMPUTER_WSDL_URL);
		URL companyUrl = new URL(COMPANY_WSDL_URL);
		
		//Get the CompanyWebService proxy
		QName qname = new QName(QNAME_URL, "CompanyWebServiceImplService");
		Service service = Service.create(companyUrl, qname);
		CompanyWebService companyWebService = service.getPort(CompanyWebService.class);
		
		//Get the ComputerWebService proxy
		qname = new QName(QNAME_URL, "ComputerWebServiceImplService");
		service = Service.create(computerUrl, qname);
		ComputerWebService computerWebService = service.getPort(ComputerWebService.class);
		
		//Create and launch the CLI
		final CLI cli = new CLI(computerWebService, companyWebService);
		cli.mainMenu();
		
	}
}
