package com.excilys.computerdatabase.service.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.excilys.computerdatabase.dao.ComputerDAOI;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.ComputerDBServiceI;
import com.excilys.computerdatabase.service.test.mock.ComputerDBServiceMock;

@RunWith(MockitoJUnitRunner.class)
public class ComputerDBServiceTest {

	ComputerDBServiceI computerDBService;
	Page<Computer> page;
	Page<Computer> pageReturned;
	Computer computer;
	ComputerDAOI computerDAO;
	
	@Before
	public void init() {
		computerDAO = mock(ComputerDAOI.class);
		computer = Computer.builder().name("test").build();
		
		page = new Page<Computer>();
		page.setNbResultsPerPage(5);
		page.setPageNumber(1);
		
		pageReturned = new Page<Computer>();
		page.setNbResultsPerPage(5);
		page.setPageNumber(1);
		page.setNbResults(20);
		page.setList(new ArrayList<Computer>());
		
		when(computerDAO.getAll()).thenReturn(new ArrayList<Computer>());
		when(computerDAO.getById(anyLong())).thenReturn(Computer.builder().id(1L).build());
		when(computerDAO.getByCompanyId(anyLong())).thenReturn(new ArrayList<Computer>());
		when(computerDAO.getPagedList(page)).thenReturn(pageReturned);
		
		
		computerDBService = new ComputerDBServiceMock(computerDAO);
	}
	
	
	@Test
	public void getAll() {
		assertEquals(new ArrayList<Computer>(), computerDBService.getAll());
	}
	
	@Test
	public void getById() {
		assertEquals(Computer.builder().id(1L).build(), computerDBService.getById(1L));
	}
	
	@Test
	public void getByCompanyId() {
		assertEquals(new ArrayList<Computer>(), computerDBService.getByCompanyId(1L));
	}
	
	@Test
	public void getPagedList() {
		assertEquals(pageReturned,computerDBService.getPagedList(page));
	}
	
	@Test
	public void create() {
		computerDBService.create(computer);
		verify(computerDAO).create(computer);
	}
	
	@Test
	public void delete() {
		computerDBService.delete(1L);
		verify(computerDAO).delete(1L);
	}
	
	@Test
	public void update() {
		computerDBService.update(computer);
		verify(computerDAO).update(computer);
	}
}
