package com.excilys.computerdatabase.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.excilys.computerdatabase.dao.ComputerDAOI;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.mock.ComputerDBServiceMock;

@RunWith(MockitoJUnitRunner.class)
public class ComputerDBServiceTest {

	ComputerDBServiceI computerDBService;
	Page<Computer> page;
	Page<Computer> pageReturned;
	Computer computer;
	
	@Before
	public void init() {
		ComputerDAOI computerDAO = mock(ComputerDAOI.class);
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
		doAnswer(new Answer() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				// TODO Auto-generated method stub
				return null;
			}}).when(computerDAO).create(computer);
		
		
		
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
	}
}
