package com.excilys.computerdatabase.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.computerdatabase.domain.Company;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context-test.xml"})
public class CompanyRepositoryTest {

	private static final String FILE = "src/test/resources/scripts/setDBCompanyTest.sql";
	
	@Autowired
	CompanyRepository companyRepository;
	List<Company> list;
	@Autowired
	DataSource dataSource;
	
	@Before
	public void init() throws SQLException, FileNotFoundException {
		list = new ArrayList<Company>();
		list.add(new Company(1L, "Apple Inc."));
		list.add(new Company(2L, "Thinking Machines"));

		FileReader reader = new FileReader(FILE);
		
		final Connection connection = DataSourceUtils.getConnection(dataSource);
		ScriptRunner scriptRunner = new ScriptRunner(connection);
		scriptRunner.runScript(reader);
		
		connection.close();
	}
	
	
	/*
	 * Tests of the getAll function
	 */
	@Test
	public void getAll() {
		assertEquals(list, companyRepository.findAll());
	}
	
	
	/*
	 * Tests of the getById function
	 */
	@Test
	public void getById() {
		assertEquals(new Company(1L, "Apple Inc."), companyRepository.findOne(1L));
	}
	
	@Test
	public void getByIdInvalid() {
		assertNull(companyRepository.findOne(3L));
		assertNull(companyRepository.findOne(-1L));
	}
	
	
	
	/*
	 * Tests of the delete function
	 */
	@Test
	public void delete() {
		companyRepository.delete(2L);
		assertNull(companyRepository.findOne(2L));
	}
	
	@Test(expected = EmptyResultDataAccessException.class)
	public void deleteInvalidId() {
		companyRepository.delete(-1L);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void deleteComputerLeft() {
		companyRepository.delete(1L);
	}
}
