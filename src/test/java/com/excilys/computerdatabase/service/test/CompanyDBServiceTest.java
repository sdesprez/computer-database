package com.excilys.computerdatabase.service.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.excilys.computerdatabase.dao.CompanyDAOI;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.service.CompanyDBServiceI;
import com.excilys.computerdatabase.service.test.mock.CompanyDBServiceMock;


@RunWith(MockitoJUnitRunner.class)
public class CompanyDBServiceTest {

	CompanyDBServiceI companyDBService;
	Page<Company> page;
	Page<Company> pageReturned;
	
	@Before
	public void init() {
		CompanyDAOI companyDAOI = mock(CompanyDAOI.class);
		page = new Page<Company>();
		page.setNbResultsPerPage(5);
		page.setPageNumber(1);
		
		pageReturned = new Page<Company>();
		page.setNbResultsPerPage(5);
		page.setPageNumber(1);
		page.setNbResults(20);
		page.setList(new ArrayList<Company>());
		
		when(companyDAOI.getAll()).thenReturn(new ArrayList<Company>());
		when(companyDAOI.getById(1L)).thenReturn(Company.builder().id(1L).build());
		when(companyDAOI.getPagedList(page)).thenReturn(pageReturned);
		
		companyDBService = new CompanyDBServiceMock(companyDAOI);
	}
	
	@Test
	public void getAll() {
		assertEquals(new ArrayList<Company>(), companyDBService.getAll());
	}
	
	@Test
	public void getById() {
		assertEquals(Company.builder().id(1L).build(), companyDBService.getById(1L));
	}
	
	@Test
	public void getPagedList() {
		assertEquals(pageReturned, companyDBService.getPagedList(page));
	}
}
