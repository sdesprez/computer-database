package com.excilys.computerdatabase.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context-test.xml"})
public class ComputerRepositoryTest {
	
	
	@Autowired
	ComputerRepository computerRepository;
	
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
	 * Tests of the findAll function
	 */
	@Test
	public void findAll() {
		assertEquals(list, computerRepository.findAll());
	}
	
	
	
	/*
	 * Tests of the findOne function
	 */
	@Test
	public void findOneValid() {
		assertEquals(list.get(0), computerRepository.findOne(1L));
		}
	
	public void findOneInvalid() {
		assertNull(computerRepository.findOne(5L));
		assertNull(computerRepository.findOne(0L));
		assertNull(computerRepository.findOne(-1L));
	}
	
	
	
	/*
	 * Tests of the getPagedList function
	 */
	@Test
	public void pagedResult() {
		Pageable pageable = new PageRequest(0, 20);
		Page<Computer> page = new PageImpl<Computer>(list, pageable, list.size());
		
		assertEquals(page, computerRepository.findByNameContainingOrCompanyNameContaining("" ,"" ,pageable));
	}
	
	@Test
	public void pagedResultNull() {
		Page<Computer> page = new PageImpl<Computer>(list);
		assertEquals(page, computerRepository.findByNameContainingOrCompanyNameContaining("" ,"" ,null));
	}
	
	@Test(expected = PropertyReferenceException.class)
	public void invalidSort() {
		Pageable pageable = new PageRequest(0, 20, new Sort(Direction.ASC, "x"));
		computerRepository.findByNameContainingOrCompanyNameContaining("" ,"", pageable);
	}
	
	
	
	/*
	 * Tests of the create function
	 */
	@Test
	public void create() {
		final Computer computer = Computer.builder().name("test").introduced(LocalDate.parse("1993-01-10")).company(apple).build();
		
		computerRepository.save(computer);
		computer.setId(5L);
		assertEquals(computer, computerRepository.findOne(5L));
	}
	
	@Test(expected = InvalidDataAccessApiUsageException.class)
	public void createNull() {
		Computer computer = null;
		computerRepository.save(computer);
	}
	
	@Test
	public void createEmptyComputer() {
		computerRepository.save(new Computer());
		list.add(Computer.builder().id(5L).build());
		assertEquals(list, computerRepository.findAll());
	}
	
	
	
	/*
	 * Tests of the update function
	 */
	@Test
	public void update() {
		final Computer computer = Computer.builder().id(2L).name("test").introduced(LocalDate.parse("1993-01-12")).build();
		computerRepository.save(computer);
		assertEquals(computer, computerRepository.findOne(2L));
	}

	@Test(expected = InvalidDataAccessApiUsageException.class)
	public void updateNull() {
		Computer computer = null;
		computerRepository.save(computer);
	}
	
	@Test
	public void updateInvalidId() {
		final Computer computer = new Computer();
		computer.setId(-1L);
		computerRepository.save(computer);
		computer.setId(5L);
		list.add(computer);
		assertEquals(list, computerRepository.findAll());
	}
	
	@Test(expected = JpaObjectRetrievalFailureException.class)
	public void updateInvalidCompanyId() {
		final Computer computer = new Computer();
		computer.setId(1L);
		computer.setCompany(new Company(-1L, ""));
		computerRepository.save(computer);
	}
	
	
	
	/*
	 * Tests of the delete function
	 */
	@Test
	public void delete() {
		assertNotNull(computerRepository.findOne(2L));
		computerRepository.delete(2L);
		assertNull(computerRepository.findOne(2L));
	}
	
	@Test(expected = EmptyResultDataAccessException.class)
	public void deleteInvalidId() {
		computerRepository.delete(-1L);
	}
	
	
	/*
	 * Tests of the deleteByCompanyId function
	 */
	public void deleteByCompanyId() {
		computerRepository.deleteByCompanyId(2L);
		assertNull(computerRepository.findOne(3L));
		assertNull(computerRepository.findOne(4L));
	}
	
	@Test
	public void DeleteCompanyInvalid() {
		computerRepository.deleteByCompanyId(-2L);
		assertEquals(list, computerRepository.findAll());
	}
	

}
