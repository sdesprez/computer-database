package com.excilys.computerdatabase.dao.impl;

public enum ColumnNames {
	ID("c.id", "id"),
	NAME("c.name", "name"),
	INTRODUCED("c.introduced", "introduced"),
	DISCONTINUED("c.discontinued", "discontinued"),
	COMPANY_ID("company.id", "company_id"),
	COMPANY_NAME("company.name", "company_name");
	

	private String name;
	private String id;
	
	private ColumnNames(final String name, final String id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getId() {
		return id;
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
