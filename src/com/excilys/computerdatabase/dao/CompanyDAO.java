package com.excilys.computerdatabase.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerdatabase.domain.Company;

public enum CompanyDAO {

	INSTANCE;

	private static final String COM_MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/computer-database-db?zeroDateTimeBehavior=convertToNull";
	private static final String USER = "admincdb";
	private static final String PASSWORD = "qwerty1234";

	public static CompanyDAO getInstance() {
		return INSTANCE;
	}

	private Connection getConnection() throws ClassNotFoundException,
			SQLException {
		Class.forName(COM_MYSQL_JDBC_DRIVER);
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	public List<Company> getAll() throws ClassNotFoundException, SQLException {
		Connection conn = null;
		List<Company> companies = new ArrayList<Company>();
		Company company;
		try {
			conn = getConnection();
			String query = "SELECT * FROM company;";
			ResultSet results;
			Statement stmt = conn.createStatement();
			results = stmt.executeQuery(query);
			while (results.next()) {
				company = new Company();
				company.setId(results.getLong("id"));
				company.setName(results.getString("name"));
				companies.add(company);
			}
			return companies;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public Company getCompany(long id) throws ClassNotFoundException,
			SQLException {
		Connection conn = null;
		Company company = null;
		try {
			conn = getConnection();
			String query = "SELECT * FROM company WHERE company.id=" + id + ";";
			ResultSet results;
			Statement stmt = conn.createStatement();
			results = stmt.executeQuery(query);
			if (results.next()) {
				company = new Company();
				company.setId(results.getLong("id"));
				company.setName(results.getString("name"));
			}
			return company;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}
}
