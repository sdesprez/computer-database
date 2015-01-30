package com.excilys.computerdatabase.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context-test.xml"})
public class ComputerDAOTest {
	
	
	@Autowired
	ComputerDAO computerDAO;
	
	@Autowired
	DataSource dataSource;
	
	List<Computer> list;
	Company apple = new Company(1L, "Apple Inc.");
	Company thinking = new Company(2L, "Thinking Machines");
	
	@Before
	public void init() throws SQLException {
		list = new ArrayList<Computer>();
		list.add(new Computer(1L, "MacBook Pro 15.4 inch", null, null, apple));
		list.add(new Computer(2L, "MacBook Pro", LocalDate.parse("2006-01-10"), null, apple));
		list.add(new Computer(3L, "CM-2a", null, null, thinking));
		list.add(new Computer(4L, "CM-200", null, null, thinking));
		
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
		stmt.execute("insert into computer (id,name,introduced,discontinued,company_id) values (  3,'CM-2a',null,null,2);");
		stmt.execute("insert into computer (id,name,introduced,discontinued,company_id) values (  4,'CM-200',null,null,2);");
		connection.close();
		
	}
	
	
	/*
	 * Tests of the getAll function
	 */
	@Test
	public void getAll() {
		assertEquals(list, computerDAO.getAll());
	}
	
	
	
	/*
	 * Tests of the getById function
	 */
	@Test
	public void getByIdValid() {
		assertEquals(list.get(0), computerDAO.getById(1L));
		}
	
	public void getByIdInvalid() {
		assertNull(computerDAO.getById(5L));
		assertNull(computerDAO.getById(0L));
		assertNull(computerDAO.getById(-1L));
	}
	
	
	
	/*
	 * Tests of the getByCompanyId function
	 */
	@Test
	public void getByCompanyId() {
		assertEquals(list.subList(0, 2), computerDAO.getByCompanyId(1L));
	}
	
	@Test
	public void getByCompanyIdInvalid() {
		assertEquals(new ArrayList<Computer>(), computerDAO.getByCompanyId(0L));
		assertEquals(new ArrayList<Computer>(), computerDAO.getByCompanyId(-2L));
	}
	
	
	
	
	/*
	 * Tests of the getPagedList function
	 */
	@Test
	public void getPagedList() {
		final Page<Computer> page = new Page<Computer>();
		page.setNbResultsPerPage(20);
		page.setPageNumber(1);
		
		final Page<Computer> pageReturned = new Page<Computer>();
		pageReturned.setNbResultsPerPage(20);
		pageReturned.setPageNumber(1);
		pageReturned.setNbResults(list.size());
		pageReturned.setNbPages(1);
		pageReturned.setList(list);
		
		assertEquals(pageReturned, computerDAO.getPagedList(page));
	}
	
	@Test
	public void getPagedListNull() {
		assertNull(computerDAO.getPagedList(null));
	}
	
	@Test(expected = BadSqlGrammarException.class)
	public void invalidOrder() {
		final Page<Computer> page = new Page<Computer>();
		page.setOrder("x");
		computerDAO.getPagedList(page);
	}
	
	@Test(expected = BadSqlGrammarException.class)
	public void invalidPageNumber() {
		final Page<Computer> page = new Page<Computer>();
		page.setPageNumber(-1);
		computerDAO.getPagedList(page);
	}
	
	@Test(expected = BadSqlGrammarException.class)
	public void invalidResultsPerPage() {
		final Page<Computer> page = new Page<Computer>();
		page.setNbResultsPerPage(-1);
		computerDAO.getPagedList(page);
	}
	
	
	
	/*
	 * Tests of the create function
	 */
	@Test
	public void create() {
		final Computer computer = Computer.builder().name("test").introducedDate(LocalDate.parse("1993-01-10")).company(apple).build();
		
		computerDAO.create(computer);
		computer.setId(5L);
		assertEquals(computer, computerDAO.getById(5L));
	}
	
	@Test
	public void createNull() {
		computerDAO.create(null);
		assertEquals(list, computerDAO.getAll());
	}
	
	@Test
	public void createEmptyComputer() {
		computerDAO.create(new Computer());
		assertEquals(list, computerDAO.getAll());
	}
	
	
	
	/*
	 * Tests of the update function
	 */
	@Test
	public void update() {
		final Computer computer = Computer.builder().id(2L).name("test").introducedDate(LocalDate.parse("1993-01-12")).build();
		computerDAO.update(computer);
		assertEquals(computer, computerDAO.getById(2L));
	}

	@Test
	public void updateNull() {
		computerDAO.update(null);
		assertEquals(list, computerDAO.getAll());
	}
	
	@Test
	public void updateInvalidId() {
		final Computer computer = new Computer();
		computer.setId(-1L);
		computerDAO.update(computer);
		assertEquals(list, computerDAO.getAll());
	}
	
	@Test
	public void updateInvalidCompanyId() {
		final Computer computer = new Computer();
		computer.setId(1L);
		computer.setCompany(new Company(-1L, ""));
		computerDAO.update(computer);
		assertEquals(list, computerDAO.getAll());
	}
	
	
	
	/*
	 * Tests of the delete function
	 */
	@Test
	public void delete() {
		assertNotNull(computerDAO.getById(2L));
		computerDAO.delete(2L);
		assertNull(computerDAO.getById(2L));
	}
	
	@Test
	public void deleteInvalidId() {
		computerDAO.delete(-1);
		assertEquals(list, computerDAO.getAll());
	}
	
	
	/*
	 * Tests of the deleteByCompanyId function
	 */
	@Test
	public void deleteByCompanyId() {
		computerDAO.deleteByCompanyId(2L);
		
		assertTrue(computerDAO.getByCompanyId(2L).isEmpty());
	}
	
	@Test
	public void DeleteCompanyInvalid() {
		computerDAO.deleteByCompanyId(-2L);
		
		assertEquals(list, computerDAO.getAll());
	}
	
	
	/*
	 * Test of the delete(List) function
	 */
	@Test
	public void multipleDelete() {
		final List<Long> l = new ArrayList<Long>();
		l.add(1L);
		l.add(2L);
		l.forEach(id -> assertNotNull(computerDAO.getById(id)));
		computerDAO.delete(l);
		l.forEach(id -> assertNull(computerDAO.getById(id)));
	}
}
