package com.excilys.computerdatabase.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.excilys.computerdatabase.dao.CompanyDAOI;
import com.excilys.computerdatabase.dao.test.mock.CompanyDAOMock;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;


public class CompanyDAOTest {

	CompanyDAOI companyDAO;
	List<Company> list;
	
	@Before
	public void init() throws SQLException {
		companyDAO = CompanyDAOMock.getInstance();
		list = new ArrayList<Company>();
		list.add(new Company(1L, "Apple Inc."));
		final ConnectionManagerTest cm = ConnectionManagerTest.getInstance();
		final Connection connection = cm.getConnection();
		
		final Statement stmt = connection.createStatement();
		stmt.execute("Truncate computer");
		
		stmt.execute("insert into computer (id,name,introduced,discontinued,company_id) values (  1,'MacBook Pro 15.4 inch',null,null,1);");
		stmt.execute("insert into computer (id,name,introduced,discontinued,company_id) values (  2,'MacBook Pro','2006-01-10',null,1);");
		
		cm.close(connection);
	}
	
	@Test
	public void getAll() {
		
		assertEquals(list, companyDAO.getAll());
	}
	
	@Test
	public void getById() {
		assertEquals(new Company(1L, "Apple Inc."), companyDAO.getById(1L));
		assertNull(companyDAO.getById(2L));
		assertNull(companyDAO.getById(-1L));
	}
	
	@Test
	public void getPagedList() {
		final Page<Company> page = new Page<Company>();
		page.setNbResultsPerPage(20);
		page.setPageNumber(1);
		
		final Page<Company> pageReturned = new Page<Company>();
		pageReturned.setNbResultsPerPage(20);
		pageReturned.setPageNumber(1);
		pageReturned.setNbResults(1);
		pageReturned.setNbPages(1);
		pageReturned.setList(list);
		assertEquals(pageReturned, companyDAO.getPagedList(page));
	}
}
