package com.excilys.computerdatabase.dao.impl;

public enum ColumnNames {
	ID("id"),
	NAME("name"),
	INTRODUCED("introduced"),
	DISCONTINUED("discontinued"),
	COMPANY_ID("company_id"),
	COMPANY_NAME("company_name");
	

	private String id;
	
	private ColumnNames(final String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public static ColumnNames getInstance(final String name) {
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
