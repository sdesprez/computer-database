package com.excilys.computerdatabase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exceptions.PersistenceException;

/**
 * Data Access Object for the Computer
 * Singleton
 * @author Sylvain DESPREZ
 *
 */
public enum ComputerDAO {

	/**
	 * Instance of ComputerDAO
	 */
	INSTANCE;

	private ConnectionManager cm = ConnectionManager.getInstance();
	public static final String SELECT_QUERY = "SELECT c.id, c.name, c.introduced, c.discontinued, company_id, company.name as company FROM computer c LEFT JOIN company ON company.id=c.company_id";
	
	
	/**
	 * Return the instance of the ComputerDAO
	 * @return Instance of the ComputerDAO
	 */
	public static ComputerDAO getInstance() {
		return INSTANCE;
	}


	/**
	 * Get the List of all the computers in the database
	 * @return List of all the computers in the database
	 */
	public List<Computer> getAll() {
		Connection conn = null;
		List<Computer> computers = new ArrayList<Computer>();
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			
			//Query the database to get all the computers
			ResultSet results;
			Statement stmt = conn.createStatement();
			results = stmt.executeQuery(SELECT_QUERY);
			//Create computers and put them in the computers list with the result
			while (results.next()) {
				computers.add(createComputer(results));
			}
			return computers;
		} catch (SQLException e) {
			throw new PersistenceException();
		} finally {
			//Close the connection
			cm.close(conn);
		}
	}
	
	/**
	 * Get the computer in the database corresponding to the id in parameter
	 * @param id : id of the computer in the database
	 * @return the computer that was found or null if there is no computer for this id
	 */
	public Computer getComputer(long id) {
		Connection conn = null;
		Computer computer = null;
		try {
			//Get the connection to the database
			conn = cm.getConnection();
			
			//Query the database
			String query = SELECT_QUERY + " WHERE c.id=" + id;
			ResultSet results;
			Statement stmt = conn.createStatement();
			results = stmt.executeQuery(query);
			
			//Create a computer if there is a result
			if (results.next()) {
				computer = createComputer(results);
			}
			return computer;
		} catch (SQLException e) {
			throw new PersistenceException();
		} finally {
			//Close the connection
			cm.close(conn);
		}
	}

	/**
	 * Get the list of computers corresponding to the id of a company
	 * @param id : id of the company in the database
	 * @return the list of computers
	 */
	public List<Computer> getByCompanyId(long id) {
		Connection conn = null;
		List<Computer> computers = new ArrayList<Computer>();
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			
			//Create the query to get the computers of a Company
			String query = SELECT_QUERY + " WHERE company_id =" + id;
			ResultSet results;
			Statement stmt = conn.createStatement();
			
			//Query the database
			results = stmt.executeQuery(query);
			//Create computers with the result
			while (results.next()) {
				computers.add(createComputer(results));
			}
			return computers;
		} catch (SQLException e) {
			throw new PersistenceException();
		} finally {
			//Close the connection
			cm.close(conn);
		}
	}

	/**
	 * Create a new computer in the database
	 * @param computer : computer to add in the database
	 */
	public void create(Computer computer) {
		Connection conn = null;
		Company company = computer.getCompany();
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			
			//Create the query
			String insertSQL = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?)";
			PreparedStatement stmt = conn.prepareStatement(insertSQL);
			stmt.setString(1, computer.getName());
			LocalDate introduced = computer.getIntroducedDate();
			LocalDate discontinued = computer.getDiscontinuedDate();
			if (introduced != null) {
				stmt.setTimestamp(2, Timestamp.valueOf(introduced.atStartOfDay()));
			} else {
				stmt.setNull(2, java.sql.Types.TIMESTAMP);
			}
			if (discontinued != null) {
				stmt.setTimestamp(3, Timestamp.valueOf(discontinued.atStartOfDay()));
			} else {
				stmt.setNull(3, java.sql.Types.TIMESTAMP);
			}
			//If the company is null, put company_id to null
			if (company == null) {
				stmt.setNull(4, java.sql.Types.BIGINT);
			} else {
				stmt.setLong(4, company.getId());
			}
			
			//Execute the query
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new PersistenceException();
		} finally {
			//Close the connection
			cm.close(conn);
		}
	}

	/**
	 * Update a computer of the database
	 * @param computer : computer to update in the database
	 */
	public void update(Computer computer) {
		Connection conn = null;
		Company company = computer.getCompany();
		try {
			//Get a connection to the database
			conn = cm.getConnection();
			conn.setAutoCommit(false);
			
			//Create the query
			PreparedStatement stmt = conn
					.prepareStatement("UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id  =? WHERE id = ?");
			stmt.setString(1, computer.getName());
			LocalDate introduced = computer.getIntroducedDate();
			LocalDate discontinued = computer.getDiscontinuedDate();
			if (introduced != null) {
				stmt.setTimestamp(2, Timestamp.valueOf(introduced.atStartOfDay()));
			} else {
				stmt.setNull(2, java.sql.Types.TIMESTAMP);
			}
			if (discontinued != null) {
				stmt.setTimestamp(3, Timestamp.valueOf(discontinued.atStartOfDay()));
			} else {
				stmt.setNull(3, java.sql.Types.TIMESTAMP);
			}
			
			//If the company is null, put company_id to null
			if (company != null) {
				stmt.setLong(4, company.getId());
			} else {
				stmt.setNull(4, java.sql.Types.BIGINT);
			}
			stmt.setLong(5, computer.getId());
			//Execute the query
			stmt.executeUpdate();
			//Commit the query
			conn.commit();
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					throw new PersistenceException();
				}
			}
			throw new PersistenceException();
		} finally {
			//Close the connection
			cm.close(conn);
		}
	}

	/**
	 * Delete a computer in the database
	 * @param id : id of the computer to delete
	 */
	public void delete(Long id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			//Get a connection
			conn = cm.getConnection();
			conn.setAutoCommit(false);
			
			//Create the query
			stmt = conn.prepareStatement("DELETE computer FROM computer WHERE id = ?");
			stmt.setLong(1, id);
			
			//Execute the query
			stmt.executeUpdate();
			
			//Commit the query
			conn.commit();
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					throw new PersistenceException();
				}
			}
			throw new PersistenceException();
		} finally {
			//Close the connection
			cm.close(conn);
		}
	}
	
	/**
	 * Get a Page of computers in the database.
	 * @param Page : A page containing the pageNumber and the max number of results
	 * @return A Page containing the list of computers 
	 */
	public Page<Computer> getPagedList(Page<Computer> page) {
		Connection conn = null;
		List<Computer> computers = new ArrayList<Computer>();
		try {
			//Get a connection
			conn = cm.getConnection();
			
			//Create the counting query
			String countQuery = "SELECT COUNT(id) AS total FROM computer";
			Statement countStmt = conn.createStatement();
			
			//Execute the counting query
			ResultSet countResult;
			countResult = countStmt.executeQuery(countQuery);
			//Set the number of results of the page with the result
			countResult.next();
			page.setNbResults(countResult.getInt("total"));

			//Create the SELECT query
			String query = SELECT_QUERY + " LIMIT ? OFFSET ? ;";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, page.getNbResultsPerPage());
			stmt.setInt(2, (page.getPageNumber() - 1) * page.getNbResultsPerPage());
			
			//Execute the SELECT query
			ResultSet results = stmt.executeQuery();
			//Create the computers with the results
			while (results.next()) {
				computers.add(createComputer(results));
			}
			page.setList(computers);
			return page;
			
		} catch (SQLException e) {
			throw new PersistenceException();
		} finally {
			//Close the connection
			cm.close(conn);
		}
	}
	
	/**
	 * Create a computer based on the columns of a row of a ResultSet
	 * @param rs : ResultSet on a row containing a computer
	 * @return the computer contained in the row
	 * @throws SQLException 
	 */
	private Computer createComputer(ResultSet rs) throws SQLException {
		Computer computer = new Computer();
		Timestamp introduced;
		Timestamp discontinued;
		Company company = null;
		computer.setId(rs.getLong("id"));
		computer.setName(rs.getString("name"));
		introduced = rs.getTimestamp("introduced");
		discontinued = rs.getTimestamp("discontinued");
		if (introduced != null) {
			computer.setIntroducedDate(introduced.toLocalDateTime().toLocalDate());
		}
		if (discontinued != null) {
			computer.setDiscontinuedDate(discontinued.toLocalDateTime().toLocalDate());
		}
		Long companyId= rs.getLong("company_Id");
		if (companyId != null) {
			company = new Company();
			company.setId(companyId);
			company.setName(rs.getString("company"));
			
		}
		computer.setCompany(company);
		
		return computer;
	}
}
