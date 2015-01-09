package com.excilys.computerdatabase.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerdatabase.domain.Computer;

public enum ComputerDAO {

	INSTANCE;

	private static final String COM_MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/computer-database-db?zeroDateTimeBehavior=convertToNull";
	private static final String USER = "admincdb";
	private static final String PASSWORD = "qwerty1234";

	public static ComputerDAO getInstance() {
		return INSTANCE;
	}

	private Connection getConnection() throws ClassNotFoundException,
			SQLException {
		Class.forName(COM_MYSQL_JDBC_DRIVER);
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	public List<Computer> getAll() throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Computer computer = null;
		List<Computer> computers = new ArrayList<Computer>();
		Timestamp introduced = null;
		Timestamp discontinued = null;
		try {
			conn = getConnection();
			String query = "SELECT * FROM computer;";
			ResultSet results;
			Statement stmt = conn.createStatement();
			results = stmt.executeQuery(query);
			while (results.next()) {
				computer = new Computer();
				computer.setId(results.getLong("id"));
				computer.setName(results.getString("name"));
				introduced = results.getTimestamp("introduced");
				discontinued = results.getTimestamp("discontinued");
				if (introduced != null) {
					computer.setIntroducedDate(introduced);
				}
				if (discontinued != null) {
					computer.setDiscontinuedDate(discontinued);
				}
				computers.add(computer);
			}
			return computers;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public Computer getComputer(long id) throws ClassNotFoundException,
			SQLException {
		Connection conn = null;
		Computer computer = null;
		Timestamp introduced = null;
		Timestamp discontinued = null;
		try {
			conn = getConnection();
			String query = "SELECT * FROM computer WHERE id=" + id + ";";
			ResultSet results;
			Statement stmt = conn.createStatement();
			results = stmt.executeQuery(query);
			if (results.next()) {
				computer = new Computer();
				computer.setId(results.getLong("id"));
				computer.setName(results.getString("name"));
				introduced = results.getTimestamp("introduced");
				discontinued = results.getTimestamp("discontinued");
				if (introduced != null) {
					computer.setIntroducedDate(introduced);
				}
				if (discontinued != null) {
					computer.setDiscontinuedDate(discontinued);
				}
			}
			return computer;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public List<Computer> getByCompanyId(long id)
			throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Computer computer = null;
		List<Computer> computers = new ArrayList<Computer>();
		Timestamp introduced = null;
		Timestamp discontinued = null;
		try {
			conn = getConnection();
			String query = "SELECT * FROM computer where company_id =" + id
					+ ";";
			ResultSet results;
			Statement stmt = conn.createStatement();
			results = stmt.executeQuery(query);
			while (results.next()) {
				computer = new Computer();
				computer.setId(results.getLong("id"));
				computer.setName(results.getString("name"));
				introduced = results.getTimestamp("introduced");
				discontinued = results.getTimestamp("discontinued");
				if (introduced != null) {
					computer.setIntroducedDate(introduced);
				}
				if (discontinued != null) {
					computer.setDiscontinuedDate(discontinued);
				}
				computers.add(computer);
			}
			return computers;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public void create(Computer computer) throws ClassNotFoundException,
			SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		String insertSQL = "INSERT INTO computer"
				+ "(name, introduced, discontinued) VALUES" + "(?,?,?)";
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(insertSQL);
			stmt.setString(1, computer.getName());
			stmt.setTimestamp(2, computer.getIntroducedTimestamp());
			stmt.setTimestamp(3, computer.getDiscontinuedTimestamp());
			stmt.executeUpdate();
		} finally {

			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	public void update(Computer computer) throws ClassNotFoundException,
			SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			stmt = conn
					.prepareStatement("UPDATE computer SET name = ?, introduced = ?, discontinued = ? WHERE id = ?");
			stmt.setString(1, computer.getName());
			stmt.setTimestamp(2, computer.getIntroducedTimestamp());
			stmt.setTimestamp(3, computer.getDiscontinuedTimestamp());
			stmt.setLong(4, computer.getId());
			stmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	public void delete(Long id) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			stmt = conn
					.prepareStatement("DELETE computer FROM computer WHERE id = ?");
			stmt.setLong(1, id);
			stmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			if (conn != null) {
				conn.rollback();
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
}
