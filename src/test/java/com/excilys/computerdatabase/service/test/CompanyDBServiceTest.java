package com.excilys.computerdatabase.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doThrow;
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
import com.excilys.computerdatabase.exceptions.PersistenceException;
import com.excilys.computerdatabase.service.CompanyDBServiceI;
import com.excilys.computerdatabase.service.test.mock.CompanyDBServiceMock;


@RunWith(MockitoJUnitRunner.class)
public class CompanyDBServiceTest {

	CompanyDBServiceI companyDBService;
	Page<Company> page;
	Page<Company> pageReturned;
	Page<Company> wrongPNumber;
	Page<Company> wrongRPP;
	
	@Before
	public void init() {
		final CompanyDAOI companyDAOI = mock(CompanyDAOI.class);
		page = new Page<Company>();
		page.setNbResultsPerPage(5);
		page.setPageNumber(1);
		
		pageReturned = new Page<Company>();
		page.setNbResultsPerPage(5);
		page.setPageNumber(1);
		page.setNbResults(20);
		page.setList(new ArrayList<Company>());
		
		wrongPNumber = new Page<Company>();
		wrongPNumber.setPageNumber(-1);
		
		wrongRPP = new Page<Company>();
		wrongRPP.setNbResultsPerPage(-1);
		
		when(companyDAOI.getAll()).thenReturn(new ArrayList<Company>());
		when(companyDAOI.getById(1L)).thenReturn(Company.builder().id(1L).build());
		when(companyDAOI.getPagedList(page)).thenReturn(pageReturned);
		
		doThrow(PersistenceException.class).when(companyDAOI).getPagedList(wrongPNumber);
		doThrow(PersistenceException.class).when(companyDAOI).getPagedList(wrongRPP);
		
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
	public void getByIdInvalid() {
		assertNull(companyDBService.getById(-1L));
	}
	
	@Test
	public void getPagedList() {
		assertEquals(pageReturned, companyDBService.getPagedList(page));
	}
	
	@Test
	public void getPagedListNull() {
		assertNull(companyDBService.getPagedList(null));
	}
	
	@Test(expected = PersistenceException.class)
	public void invalidPageNumber() {
		companyDBService.getPagedList(wrongPNumber);
	}
	
	@Test(expected = PersistenceException.class)
	public void invalidResultsPerPage() {
		companyDBService.getPagedList(wrongRPP);
	}
}
