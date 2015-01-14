package com.excilys.computerdatabase;

import com.excilys.computerdatabase.cli.CLI;

public class Main {

	public static void main(String[] args) {
		System.out.println("Welcome to the Computer Database");
		CLI cli = new CLI();
		cli.mainMenu();
	}
}
