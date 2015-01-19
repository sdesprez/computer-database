package com.excilys.computerdatabase.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.excilys.computerdatabase.dao.ComputerDAOI;
import com.excilys.computerdatabase.dao.test.mock.ComputerDAOMock;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;

public class ComputerDAOTest {

	ComputerDAOI computerDAO;
	List<Computer> list;
	Company company = new Company(1L, "Apple Inc.");
	
	@Before
	public void init() throws SQLException {
		computerDAO = ComputerDAOMock.getInstance();
		list = new ArrayList<Computer>();
		list.add(new Computer(1L, "MacBook Pro 15.4 inch", null, null, company));
		list.add(new Computer(2L, "MacBook Pro", LocalDate.parse("2006-01-10"), null, company));
		
		final ConnectionManagerTest cm = ConnectionManagerTest.getInstance();
		Connection connection = cm.getConnection();
		
		Statement stmt = connection.createStatement();
		stmt.execute("Truncate computer");
		
		stmt.execute("insert into computer (id,name,introduced,discontinued,company_id) values (  1,'MacBook Pro 15.4 inch',null,null,1);");
		stmt.execute("insert into computer (id,name,introduced,discontinued,company_id) values (  2,'MacBook Pro','2006-01-10',null,1);");
		cm.close(connection);
		
	}
	
	@Test
	public void getAll() {
		assertEquals(list, computerDAO.getAll());
	}
	
	@Test
	public void getById() {
		assertEquals(list.get(0), computerDAO.getById(1L));
		assertNull(computerDAO.getById(3L));
		assertNull(computerDAO.getById(0L));
	}
	
	@Test
	public void getByCompanyId() {
		assertEquals(list, computerDAO.getByCompanyId(1L));
	}
	
	@Test
	public void getPagedList() {
		Page<Computer> page = new Page<Computer>();
		page.setNbResultsPerPage(20);
		page.setPageNumber(1);
		
		Page<Computer> pageReturned = new Page<Computer>();
		pageReturned.setNbResultsPerPage(20);
		pageReturned.setPageNumber(1);
		pageReturned.setNbResults(2);
		pageReturned.setList(list);
		
		assertEquals(pageReturned, computerDAO.getPagedList(page));
	}
	
	@Test
	public void create() {
		Computer computer = Computer.builder().name("test").introducedDate(LocalDate.parse("1993-01-10")).company(company).build();
		
		computerDAO.create(computer);
		computer.setId(3L);
		assertEquals(computer, computerDAO.getById(3L));
	}
	
	@Test
	public void update() {
		Computer computer = Computer.builder().id(2L).name("test").introducedDate(LocalDate.parse("1993-01-12")).build();
		computerDAO.update(computer);
		assertEquals(computer, computerDAO.getById(2L));
	}

	@Test
	public void delete() {
		assertNotNull(computerDAO.getById(2L));
		computerDAO.delete(2L);
		assertNull(computerDAO.getById(2L));
	}
}
