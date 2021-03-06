package com.excilys.computerdatabase.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.repositories.CompanyRepository;
import com.excilys.computerdatabase.repositories.ComputerRepository;
import com.excilys.computerdatabase.service.impl.CompanyDBServiceImpl;


@RunWith(MockitoJUnitRunner.class)
public class CompanyDBServiceTest {

	@InjectMocks
	CompanyDBServiceImpl companyDBService;
	

	CompanyRepository companyRepository;
	ComputerRepository computerRepository;
	
	List<Computer> computerList;
	List<Company> companyList;
	Company c1;
	Company c2;
	
	@Before
	public void init() {
		companyRepository = mock(CompanyRepository.class);
		computerRepository = mock(ComputerRepository.class);
		
		companyList = new ArrayList<Company>();
		c1 = new Company(1L, "company 1");
		c2 = new Company(2L, "company 2");
		companyList.add(c1);
		companyList.add(c2);
		
		computerList = new ArrayList<Computer>();
		computerList.add(new Computer(1L, "ordi 1", null, null, c1));
		computerList.add(new Computer(2L, "ordi 2", null, null, c1));
		computerList.add(new Computer(3L, "ordi 3", null, null, c2));
		
		
		when(companyRepository.findAll()).thenReturn(companyList);
		
		doAnswer(new Answer<Company>() {
			@Override
			public Company answer(final InvocationOnMock invocation) {
				final long l = (Long) invocation.getArguments()[0];
				List<Company> comp = companyList.stream().filter(c -> c.getId()==l).collect(Collectors.toList());
				if (comp.isEmpty()) {
					return null;
				}
				else {
					return comp.get(0);
				}
			}
		}).when(companyRepository).findOne(anyLong());
		
		doAnswer(new Answer<List<Computer>>() {
			@Override
			public List<Computer> answer(final InvocationOnMock invocation) {
				final long l = (Long) invocation.getArguments()[0];
				companyList.removeIf(c -> c.getId() == l);
				return null;
			}
		}).when(companyRepository).delete(anyLong());
		
		doAnswer(new Answer<List<Computer>>() {
			@Override
			public List<Computer> answer(final InvocationOnMock invocation) {
				final long l = (Long) invocation.getArguments()[0];
				computerList.removeIf(c -> c.getCompany().getId() == l);
				return null;
			}
		}).when(computerRepository).deleteByCompanyId(anyLong());
		
		MockitoAnnotations.initMocks(this);
	}
	
	
	/*
	 * Test getAll function
	 */
	@Test
	public void getAll() {
		assertEquals(companyList, companyDBService.getAll());
	}
	
	
	/*
	 * Tests getById function
	 */
	@Test
	public void getById() {
		assertEquals(c1, companyDBService.getById(1L));
	}
	
	@Test
	public void getByIdInvalid() {
		assertNull(companyDBService.getById(-1L));
	}
	
	
	/*
	 * Tests delete function
	 */
	@Test
	public void delete() {
		final int x = companyList.size();
		final int y = computerList.size();
		companyDBService.delete(1L);
		
		assertEquals(x - 1, companyList.size());
		assertEquals(y - 2, computerList.size());
	}
	
	@Test
	public void deleteInvalidId() {
		final long id = 3L;
		final int computerCount = computerList.size();
		companyDBService.delete(id);
		assertNull(companyRepository.findOne(id));
		assertEquals(computerCount, computerList.size());
	}
}
