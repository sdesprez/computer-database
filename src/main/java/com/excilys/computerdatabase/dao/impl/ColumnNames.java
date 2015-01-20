package com.excilys.computerdatabase.dao.impl;

public enum ColumnNames {
	ID("c.id"),
	NAME("c.name"),
	INTRODUCED("c.introduced"),
	DISCONTINUED("c.discontinued"),
	COMPANY_ID("company.id"),
	COMPANY_NAME("company.name");
	

	private String name;
	
	private ColumnNames(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static ColumnNames getInstance(String name) {
		if (name != null) {
			switch(name) {
			case "id":
				return ID;
			case "name":
				return NAME;
			case "introduced":
				return INTRODUCED;
			case "discontinued":
				return DISCONTINUED;
			case "company_id":
				return COMPANY_ID;
			case "company_name":
				return COMPANY_NAME;
			default:
				break;
			}
		}
		return null;
	}
	
}
