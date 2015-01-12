package com.excilys.computerdatabase.cli;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.service.ComputerDBService;

/**
 * Command Line Interface
 * @author Sylvain DESPREZ
 * 
 */
public class CLI {

	/**
	 * Scanner for the inputs of the user
	 */
	private static Scanner sc;
	/**
	 * Instance of ComputerDBService for the access to the database
	 */
	private static ComputerDBService computerDBService = ComputerDBService.getInstance();
	/**
	 * Instance of CompanyDBService for the access to the database
	 */
	private static CompanyDBService companyDBService = CompanyDBService.getInstance();

	
	/**
	 * Main menu of the CLI
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void mainMenu() throws ClassNotFoundException, SQLException {
		//Instanciation of the scanner
		sc = new Scanner(System.in);
		
		//Show the menu and ask the user for the function he wish to use as long as he wish to continue.
		do {
			System.out.println("0:Close");
			System.out.println("1:List computers");
			System.out.println("2:List companies");
			System.out.println("3:Show computer details");
			System.out.println("4:Add a computer");
			System.out.println("5:Update a computer");
			System.out.println("6:Delete a computer");
			
			//Check the input and send to the corresponding function.
			switch (sc.nextLine()) {
			case "0":
				sc.close();
				return;
			case "1":
				listComputers();
				break;
			case "2":
				listCompanies();
				break;
			case "3":
				computerDetails();
				break;
			case "4":
				createComputer();
				break;
			case "5":
				updateComputer();
				break;
			case "6":
				deleteComputer();
				break;
			default:
				System.out.println("Invalid Input");
				break;
			}
			
			System.out.println("Do you wish to continue? (y/n)");
			
		} while (sc.nextLine().toLowerCase().equals("y"));
		
		//Close the scanner 
		sc.close();
	}
	
	/**
	 * List the computers of the database by pages of 20 computers
	 * You can navigate to the next page or the previous page
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void listComputers() throws ClassNotFoundException, SQLException {
		//Create a Page
		Page page = new Page();
		
		//Get the first Page of computers from the database
		page = computerDBService.getPagedList(page);
		
		//Show the content of the page
		System.out.println("Total : " + page.getNbResults());
		page.getList().forEach(System.out::println);
		
		while (true) {
			System.out.println("e:Exit\np:Previous page\nn:Next page");
			
			switch(sc.nextLine().toLowerCase()) {
			//Exit the listComputer() function
			case "e":
				return;
			//Show the next Page
			case "n":
				if (page.nextPage()) {
					page = computerDBService.getPagedList(page);
				}
				System.out.println("Total : " + page.getNbResults());
				page.getList().forEach(System.out::println);
				break;
			//Show the previous Page
			case "p":
				if (page.previousPage()) {
					page = computerDBService.getPagedList(page);
				}
				System.out.println("Total : " + page.getNbResults());
				page.getList().forEach(System.out::println);
				break;
			default:
				System.out.println("Invalid input");
				break;
			}
		}
	}
	
	/**
	 * List the companies of the database by pages of 20 computers
	 * You can navigate to the next page or the previous page
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void listCompanies() throws ClassNotFoundException, SQLException {
		//Create a Page
		Page page = new Page();
		
		//Get the first Page of companies from the database
		page = companyDBService.getPagedList(page);
		
		//Show the content of the page
		System.out.println("Total : " + page.getNbResults());
		page.getList().forEach(System.out::println);
		
		while (true) {
			System.out.println("e:Exit\np:Previous page\nn:Next page");
			
			switch(sc.nextLine().toLowerCase()) {
			//Exit the listComputer() function
			case "e":
				return;
			//Show the next Page
			case "n":
				if (page.nextPage()) {
					page = companyDBService.getPagedList(page);
				}
				System.out.println("Total : " + page.getNbResults());
				page.getList().forEach(System.out::println);
				break;
			//Show the previous Page
			case "p":
				if (page.previousPage()) {
					page = companyDBService.getPagedList(page);
				}
				System.out.println("Total : " + page.getNbResults());
				page.getList().forEach(System.out::println);
				break;
			default:
				System.out.println("Invalid input");
				break;
			}
		}
	}
	
	/**
	 * Show the detail of a computer
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void computerDetails() throws ClassNotFoundException, SQLException {
		System.out.println("Enter the computer id");
		
		//Get the computer corresponding to the id
		Computer computer = computerDBService.getComputer(inputLong());
		//Show the detail of the computer
		if (computer != null) {
			System.out.println("Name : " + computer.getName());
			System.out.println("Introduced date : " + computer.getIntroducedDate());
			System.out.println("Discontinued date : " + computer.getDiscontinuedDate());
			System.out.println("Company : " + computer.getCompany());
		} else {
			System.out.println("No computer found for this id");
		}
	}

	/**
	 * Interface for creating a new computer
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void createComputer() throws ClassNotFoundException, SQLException {
		Computer computer = new Computer();
		Company company = null;
		
		//Get the name of the computer, it can't be null or empty
		System.out.println("Enter computer name");
		computer.setName(inputName());

		//Get the date of introduction. If the user enter nothing, the date is null
		System.out.println("Enter introduction date (yyyy-mm-dd) or nothing if you don't know");
		computer.setIntroducedDate(inputDate());

		//Get the date of discontinuation. If the user enter nothing, the date is null
		System.out.println("Enter discontinued date (yyyy-mm-dd) or nothing if you don't know");
		computer.setDiscontinuedDate(inputDate());

		//Get the id of the company. If it's 0, then company = null
		companyLabel : while (company == null) {
			System.out.println("Enter company id (0 if you don't have one)");
			Long id = inputLong();
			if (id == 0) {
				break companyLabel;
			}
			company = companyDBService.getCompany(id);
			if (company == null) {
				System.out.println("This id doesn't correspond to any company in the database.");
			}
		}
		computer.setCompany(company);
		
		//Add the computer to the database
		computerDBService.create(computer);
		System.out.println("The computer was added to the database");
		
	}

	/**
	 * Interface for updating an existing computer
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void updateComputer() throws ClassNotFoundException, SQLException {
		System.out.println("Enter the id of the computer you wish to update");
		//Get the computer to update from the database
		Computer computer = computerDBService.getComputer(inputLong());
		Company company = null;
		if (computer != null) {
			//Change the name if the user wants
			System.out.println("Current name : " + computer.getName());
			System.out.println("Do you wish to change it?(y,n)");
			if (sc.nextLine().toLowerCase().compareTo("y") == 0) {
				System.out.println("Enter the new name");
				computer.setName(inputName());
			}
			
			//Change the introducedDate if the user wants
			System.out.println("Current introduced date : " + computer.getIntroducedDate());
			System.out.println("Do you wish to change it?(y,n)");
			if (sc.nextLine().toLowerCase().compareTo("y") == 0) {
				System.out.println("Enter the new introduced Date");
				computer.setIntroducedDate(inputDate());
			}
			
			//Change the discontinuedDate if the user wants
			System.out.println("Current discontinued date : " + computer.getDiscontinuedDate());
			System.out.println("Do you wish to change it?(y,n)");
			if (sc.nextLine().toLowerCase().compareTo("y") == 0) {
				System.out.println("Enter the new dicontinued date");
				computer.setDiscontinuedDate(inputDate());
			}
			
			//Change the company if the user wants
			System.out.println("Current company id : " + computer.getCompany());
			System.out.println("Do you wish to change it?(y,n)");
			if (sc.nextLine().toLowerCase().compareTo("y") == 0) {
				System.out.println("Enter the new company id (0 if you don't have one)");
				Long id = inputLong();
				if (id != 0) {
					company = new Company();
					company.setId(id);
				}
				computer.setCompany(company);
			}
			
			//Update the computer in the database
			computerDBService.update(computer);
			System.out.println("The computer was updated");
		} else {
			System.out.println("Incorrect id");
		}
	}
	
	/**
	 * Interface for deleting a computer
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void deleteComputer() throws ClassNotFoundException, SQLException {
		Long id;
		System.out.println("Enter the id of the computer you wish to delete");
		id = inputLong();
		computerDBService.delete(id);
		System.out.println("The computer has been deleted");
	}
	
	
	/**
	 * Function checking the user input to see if it's a valid computer name.
	 * If the name isn't valid, it ask the user a new input until it's correct.
	 * @return String : a valid computer name
	 */
	private String inputName() {
		//Get the input
		String name = sc.nextLine();
		//Ask a new input while the name is empty or null
		while (name == null || name.trim().isEmpty()) {
			System.out.println("Computer name can't be empty or spaces only");
			System.out.println("Enter computer name");
			name = sc.nextLine();
		}
		return name;
	}
	
	/**
	 * Function checking the user input to see if it's a valid Date name.
	 * If the date isn't valid, it ask the user a new input until it's correct.
	 * If the input is null or an empty String, return null;
	 * @return LocalDate : a valid LocalDate 
	 */
	private LocalDate inputDate() {
		String iDate = null;
		//Get the input
		iDate = sc.nextLine();
		LocalDate date = null;
		//Check if the input is an empty chain or null
		while (iDate != null && !iDate.trim().isEmpty()) {
			try {
				//If the parse works, return the date
				date = LocalDate.parse(iDate);
				return date;
			//If the parse fails, ask a new input
			} catch (DateTimeParseException e) {
				System.out.println("Wrong date format");
				System.out.println("Enter introduction date (yyyy-mm-dd) or nothing if you don't know");
				iDate = sc.nextLine();
			}
		}
		return null;
	}
	
	/**
	 * Function checking the user input to see if it's a long.
	 * If it isn't, the function ask a new input until it's valid.
	 * @return long : a valid long
	 */
	private long inputLong() {
		long id = 0;
		//Get the input
		String stringId = sc.nextLine();
		boolean b = true;
		//Check the input and ask a new one as long as the input isn't a long
		do {
			try {
				id = Long.parseLong(stringId);
				b = false;
			} catch (NumberFormatException e) {
				System.out.println("Input is not a Long, enter a new input :");
				stringId = sc.nextLine();
			}
		} while(b);
		return id;
	}
}
