package com.excilys.computerdatabase.cli;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.service.CompanyDBService;
import com.excilys.computerdatabase.service.ComputerDBService;

public class CLI {

	private static Scanner sc = new Scanner(System.in);
	private static ComputerDBService computerDBService = ComputerDBService
			.getInstance();
	private static CompanyDBService companyDBService = CompanyDBService
			.getInstance();

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		System.out.println("Welcome to the Computer Database");
		CLI cli = new CLI();
		cli.createComputer();
	}

	public void listing() throws ClassNotFoundException, SQLException {
		List<Computer> computers;
		List<Company> companies;

		System.out.println("0:List computers \n1:List companies");
		switch (sc.nextInt()) {
		case 0:
			computers = computerDBService.getAll();
			computers.forEach(System.out::println);
			break;
		case 1:
			companies = companyDBService.getAll();
			companies.forEach(System.out::println);
			break;
		default:
			System.out.println("Invalid Input");
			break;
		}
	}

	public void createComputer() throws ClassNotFoundException, SQLException {
		Computer computer = new Computer();
		
		System.out.println("Enter computer name");
		String name = sc.nextLine();
		while (name == null || name.trim().isEmpty()) {
			System.out.println("Computer name can't be empty or spaces only");
			System.out.println("Enter computer name");
			name = sc.nextLine();
		}
		computer.setName(name);
		
		System.out.println("Enter introduction date (yyyy-mm-dd)");
		String iDate = sc.nextLine();
		iDateTest : if (iDate != null) {
			LocalDate date = LocalDate.parse(iDate);
			while (date.toEpochDay() < 1) {
				System.out.println("Wrong date, enter a new one");
				iDate = sc.nextLine();
				if (iDate == null || iDate.trim().isEmpty()) {
					break iDateTest;
				}
			}
			computer.setIntroducedDate(date);
		}
		
		
		computerDBService.create(computer);	
	}

}
