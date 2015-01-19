package com.excilys.computerdatabase;

import com.excilys.computerdatabase.cli.CLI;

public class Main {

	public static void main(final String[] args) {
		System.out.println("Welcome to the Computer Database");
		final CLI cli = new CLI();
		cli.mainMenu();
	}
}
