package com.excilys.computerdatabase.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context-test.xml"})
public class CompanyDAOTest {

	@Autowired
	CompanyDAO companyDAO;
	List<Company> list;
	@Autowired
	DataSource dataSource;
	
	@Before
	public void init() throws SQLException {
		list = new ArrayList<Company>();
		list.add(new Company(1L, "Apple Inc."));
		list.add(new Company(2L, "Thinking Machines"));

		final Connection connection = DataSourceUtils.getConnection(dataSource);
		
		final Statement stmt = connection.createStatement();
		stmt.execute("drop table if exists computer;");  
		stmt.execute("drop table if exists company;");
		stmt.execute("create table company (id bigint not null auto_increment, name varchar(255), "
				+ "constraint pk_company primary key (id));");
		stmt.execute("create table computer (id bigint not null auto_increment,name varchar(255), "
				+ "introduced timestamp NULL, discontinued timestamp NULL,"
				+ "company_id bigint default NULL,"
				+ "constraint pk_computer primary key (id));");
		stmt.execute("alter table computer add constraint fk_computer_company_1 foreign key (company_id)"
				+ " references company (id) on delete restrict on update restrict;");
		stmt.execute("create index ix_computer_company_1 on computer (company_id);");
		
		stmt.execute("insert into company (id,name) values (  1,'Apple Inc.');");
		stmt.execute("insert into company (id,name) values (  2,'Thinking Machines');");
		
		stmt.execute("insert into computer (id,name,introduced,discontinued,company_id) values (  1,'MacBook Pro 15.4 inch',null,null,1);");
		stmt.execute("insert into computer (id,name,introduced,discontinued,company_id) values (  2,'MacBook Pro','2006-01-10',null,1);");
		connection.close();
	}
	
	
	/*
	 * Tests of the getAll function
	 */
	@Test
	public void getAll() {
		assertEquals(list, companyDAO.getAll());
	}
	
	
	/*
	 * Tests of the getById function
	 */
	@Test
	public void getById() {
		assertEquals(new Company(1L, "Apple Inc."), companyDAO.getById(1L));
	}
	
	@Test
	public void getByIdInvalid() {
		assertNull(companyDAO.getById(3L));
		assertNull(companyDAO.getById(-1L));
	}
	
	
	
	
	/*
	 * Tests of the getPagedList function
	 */
	@Test
	public void getPagedList() {
		final Page<Company> page = new Page<Company>();
		page.setNbResultsPerPage(20);
		page.setPageNumber(1);
		
		final Page<Company> pageReturned = new Page<Company>();
		pageReturned.setNbResultsPerPage(20);
		pageReturned.setPageNumber(1);
		pageReturned.setNbResults(2);
		pageReturned.setNbPages(1);
		pageReturned.setList(list);
		assertEquals(pageReturned, companyDAO.getPagedList(page));
	}
	
	@Test(expected = NullPointerException.class)
	public void getPagedListNull() {
		companyDAO.getPagedList(null);
	}
	
	@Test(expected = BadSqlGrammarException.class)
	public void invalidPageNumber() {
		final Page<Company> page = new Page<Company>();
		page.setPageNumber(-1);
		companyDAO.getPagedList(page);
	}
	
	@Test(expected = BadSqlGrammarException.class)
	public void invalidResultsPerPage() {
		final Page<Company> page = new Page<Company>();
		page.setNbResultsPerPage(-1);
		companyDAO.getPagedList(page);
	}
	
	
	
	/*
	 * Tests of the delete function
	 */
	@Test
	public void delete() {
		companyDAO.delete(2L);
		assertNull(companyDAO.getById(2L));
	}
	
	@Test
	public void deleteInvalidId() {
		companyDAO.delete(-1L);
		assertEquals(list, companyDAO.getAll());
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void deleteComputerLeft() {
		companyDAO.delete(1L);
	}
}
