package com.excilys.computerdatabase;

import java.sql.SQLException;

import com.excilys.computerdatabase.cli.CLI;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		System.out.println("Welcome to the Computer Database");
		CLI cli = new CLI();
		cli.mainMenu();
	}
}
