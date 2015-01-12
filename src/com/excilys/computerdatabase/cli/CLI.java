package com.excilys.computerdatabase.cli;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.service.ComputerDBService;

public class CLI {

	private static Scanner sc;
	private static ComputerDBService computerDBService = ComputerDBService
			.getInstance();
	private static CompanyDBService companyDBService = CompanyDBService
			.getInstance();

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		System.out.println("Welcome to the Computer Database");
		CLI cli = new CLI();
		cli.mainMenu();
	}

	public void mainMenu() throws ClassNotFoundException, SQLException {
		sc = new Scanner(System.in);
		System.out.println("0:Close");
		System.out.println("1:List computers");
		System.out.println("2:List companies");
		System.out.println("3:Show computer details");
		System.out.println("4:Add a computer");
		System.out.println("5:Update a computer");
		System.out.println("6:Delete a computer");
		
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
	}
	
	
	public void listComputers() throws ClassNotFoundException, SQLException {
		List<Computer> computers = computerDBService.getAll();
		computers.forEach(System.out::println);
	}
	
	public void listCompanies() throws ClassNotFoundException, SQLException {
		List<Company> companies = companyDBService.getAll();
		companies.forEach(System.out::println);
	}
	
	public void computerDetails() throws ClassNotFoundException, SQLException {
		System.out.println("Enter the computer id");
		Long id = sc.nextLong();
		
		Computer computer = computerDBService.getComputer(id);
		System.out.println("Name : " + computer.getName());
		System.out.println("Introduced date : " + computer.getIntroducedDate());
		System.out.println("Discontinued date : " + computer.getDiscontinuedDate());
		System.out.println("Company id : " + computer.getCompanyId());
	}

	public void createComputer() throws ClassNotFoundException, SQLException {
		Computer computer = new Computer();
		
		System.out.println("Enter computer name");
		computer.setName(enterName());

		System.out.println("Enter introduction date (yyyy-mm-dd) or nothing if you don't know");
		computer.setIntroducedDate(enterDate());

		System.out.println("Enter discontinued date (yyyy-mm-dd) or nothing if you don't know");
		computer.setDiscontinuedDate(enterDate());

		System.out.println("Enter company id (0 if you don't have one");
		Long id = sc.nextLong();
		if (id == 0){
			computer.setCompanyId(null);
		}
		else {
			computer.setCompanyId(id);
		}
		computerDBService.create(computer);
		
	}

	
	public void updateComputer() throws ClassNotFoundException, SQLException {
		System.out.println("Enter the id of the computer you wish to update");
		Computer computer = computerDBService.getComputer(sc.nextLong());
		sc.nextLine();
		if (computer != null) {
			System.out.println("Current name : " + computer.getName());
			System.out.println("Do you wish to change it?(y,n)");
			if (sc.nextLine().toLowerCase().compareTo("y") == 0) {
				System.out.println("Enter the new name");
				computer.setName(enterName());
			}
			
			System.out.println("Current introduced date : " + computer.getIntroducedDate());
			System.out.println("Do you wish to change it?(y,n)");
			if (sc.nextLine().toLowerCase().compareTo("y") == 0) {
				System.out.println("Enter the new introduced Date");
				computer.setIntroducedDate(enterDate());
			}
			
			System.out.println("Current discontinued date : " + computer.getDiscontinuedDate());
			System.out.println("Do you wish to change it?(y,n)");
			if (sc.nextLine().toLowerCase().compareTo("y") == 0) {
				System.out.println("Enter the new dicontinued date");
				computer.setDiscontinuedDate(enterDate());
			}
			
			System.out.println("Current company id : " + computer.getCompanyId());
			System.out.println("Do you wish to change it?(y,n)");
			if (sc.nextLine().toLowerCase().compareTo("y") == 0) {
				System.out.println("Enter the new company id (0 if you don't have one");
				Long id = sc.nextLong();
				if (id == 0){
					computer.setCompanyId(null);
				}
				else {
					computer.setCompanyId(id);
				}
			}
			
			computerDBService.update(computer);
			System.out.println("The computer was updated");
		} else {
			System.out.println("Incorrect id");
		}
	}
	
	public void deleteComputer() throws ClassNotFoundException, SQLException {
		Long id;
		System.out.println("Enter the id of the computer you wish to delete");
		id = sc.nextLong();
		computerDBService.delete(id);
	}
	
	
	private String enterName(){
		String name = sc.nextLine();
		while (name == null || name.trim().isEmpty()) {
			System.out.println("Computer name can't be empty or spaces only");
			System.out.println("Enter computer name");
			name = sc.nextLine();
		}
		return name;
	}
	
	private LocalDate enterDate() {
		String iDate = null;
		iDate = sc.nextLine();
		LocalDate date = null;
		while (iDate != null && !iDate.trim().isEmpty()) {
			try {
				date = LocalDate.parse(iDate);
				return date;
			} catch (DateTimeParseException e) {
				System.out.println("Wrong date format");
				System.out.println("Enter introduction date (yyyy-mm-dd) or nothing if you don't know");
				iDate = sc.nextLine();
			}
		}
		return null;
	}
}
