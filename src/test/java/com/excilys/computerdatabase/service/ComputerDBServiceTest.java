package com.excilys.computerdatabase.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.excilys.computerdatabase.dao.ComputerDAO;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exceptions.PersistenceException;
import com.excilys.computerdatabase.service.mock.ComputerDBServiceMock;

@RunWith(MockitoJUnitRunner.class)
public class ComputerDBServiceTest {

	ComputerDBService computerDBService;
	Page<Computer> page;
	Page<Computer> pageReturned;
	Page<Computer> wrongPNumber;
	Page<Computer> wrongRPP;
	ComputerDAO computerDAO;
	List<Computer> list;
	Company c1;
	Company c2;
	Computer computer;

	@Before
	public void init() {
		computerDAO = mock(ComputerDAO.class);

		c1 = new Company(1L, "company 1");
		c2 = new Company(2L, "company 2");

		list = new ArrayList<Computer>();
		list.add(new Computer(1L, "ordi 1", null, null, c1));
		list.add(new Computer(2L, "ordi 2", null, null, c1));
		list.add(new Computer(3L, "ordi 3", null, null, c2));

		page = new Page<Computer>();

		pageReturned = new Page<Computer>();
		page.setNbResults(list.size());
		page.setList(list);
		
		wrongPNumber = new Page<Computer>();
		wrongPNumber.setPageNumber(-1);
		
		wrongRPP = new Page<Computer>();
		wrongRPP.setNbResultsPerPage(-1);

		computer = new Computer(4L, "ordi 4", null, null, c1);

		when(computerDAO.getAll()).thenReturn(list);
		when(computerDAO.getPagedList(page)).thenReturn(pageReturned);

		doAnswer(new Answer<List<Computer>>() {
			@Override
			public List<Computer> answer(final InvocationOnMock invocation) {
				final long l = (Long) invocation.getArguments()[0];
				return list.stream().filter(c -> c.getCompany().getId() == l).collect(Collectors.toList());
			}
		}).when(computerDAO).getByCompanyId(anyLong());

		doAnswer(new Answer<Computer>() {
			@Override
			public Computer answer(final InvocationOnMock invocation) {
				final long l = (Long) invocation.getArguments()[0];
				if (l > 0 && l < list.size()) {
					return list.get((int) l - 1);
				}
				return null;
			}
		}).when(computerDAO).getById(anyLong());

		doAnswer(new Answer<Computer>() {

			@Override
			public Computer answer(final InvocationOnMock invocation) {
				final Computer computer = (Computer) invocation.getArguments()[0];
				if (computer != null) {
					list.add(computer);
				}
				return null;
			}
		}).when(computerDAO).create(any(Computer.class));
		
		doAnswer(new Answer<Computer>() {

			@Override
			public Computer answer(final InvocationOnMock invocation) {
				final Computer computer = (Computer) invocation.getArguments()[0];
				if (computer != null && computer.getId() > 0 && computer.getId() < list.size()) {
					if (computer.getCompany() != null && (computer.getCompany().getId() < 0 || computer.getCompany().getId() > 2)) {
						throw new PersistenceException();
					}
					list.set((int) computer.getId() - 1, computer);
				}
				return null;
			}
			
		}).when(computerDAO).update(any(Computer.class));
		
		
		doAnswer(new Answer<Computer>() {

			@Override
			public Computer answer(final InvocationOnMock invocation) {
				final long l = (Long) invocation.getArguments()[0];
				list.removeIf(c -> c.getId() == l);
				return null;
			}
			
		}).when(computerDAO).delete(anyLong());
		
		doAnswer(new Answer<Computer>() {

			@Override
			public Computer answer(final InvocationOnMock invocation) {
				@SuppressWarnings("unchecked")
				final List<Long> l = (List<Long>) invocation.getArguments()[0];
				list.removeIf(c -> l.contains(c.getId()));
				return null;
			}
			
		}).when(computerDAO).delete(Matchers.anyListOf(Long.class));
		
		when(computerDAO.getPagedList(page)).thenReturn(pageReturned);
		doThrow(PersistenceException.class).when(computerDAO).getPagedList(wrongPNumber);
		doThrow(PersistenceException.class).when(computerDAO).getPagedList(wrongRPP);

		computerDBService = new ComputerDBServiceMock(computerDAO);
	}

	
	
	/*
	 * Test getAll function
	 */
	@Test
	public void getAll() {
		assertEquals(list, computerDBService.getAll());
	}

	/*
	 * Tests getById function
	 */
	@Test
	public void getById() {
		assertEquals(list.get(0), computerDBService.getById(1L));
	}

	@Test
	public void getByIdInvalid() {
		assertNull(computerDBService.getById(-1L));
		assertNull(computerDBService.getById(5L));
	}

	/*
	 * Tests getByCompanyId function
	 */
	@Test
	public void getByCompanyId() {
		assertEquals(list.subList(0, 2), computerDBService.getByCompanyId(1L));
	}
	
	@Test
	public void getByCompanyIdInvalid() {
		assertEquals(new ArrayList<Computer>(), computerDBService.getByCompanyId(-1L));
		assertEquals(new ArrayList<Computer>(), computerDBService.getByCompanyId(5L));
	}

	
	/*
	 * Tests getPagedList
	 */
	@Test
	public void getPagedList() {
		assertEquals(pageReturned, computerDBService.getPagedList(page));
	}

	@Test
	public void getPagedListNull() {
		assertNull(computerDBService.getPagedList(null));
	}
	
	@Test(expected = PersistenceException.class)
	public void invalidPageNumber() {
		computerDBService.getPagedList(wrongPNumber);
	}
	
	@Test(expected = PersistenceException.class)
	public void invalidResultsPerPage() {
		computerDBService.getPagedList(wrongRPP);
	}
	
	
	
	/*
	 * Tests of create function
	 */
	@Test
	public void create() {
		computerDBService.create(computer);
		assertEquals(computer, list.get(3));
	}
	
	@Test
	public void createNull() {
		computerDAO.create(null);
		assertEquals(list, computerDBService.getAll());
	}
	
	@Test
	public void createEmptyComputer() {
		computerDAO.create(new Computer());
		assertEquals(list, computerDBService.getAll());
	}

	
	/*
	 * Tests of update function
	 */
	@Test
	public void update() {
		computerDBService.update(computer);
		assertEquals(list, computerDBService.getAll());
	}
	
	@Test
	public void updateNull() {
		computerDAO.update(null);
		assertEquals(list, computerDBService.getAll());
	}
	
	@Test
	public void updateInvalidId() {
		final Computer computer = new Computer();
		computer.setId(-1L);
		computerDAO.update(computer);
		assertEquals(list, computerDBService.getAll());
	}
	
	@Test(expected = PersistenceException.class)
	public void updateInvalidCompanyId() {
		final Computer computer = new Computer();
		computer.setId(1L);
		computer.setCompany(new Company(-1L, ""));
		computerDAO.update(computer);
	}
	
	
	/*
	 * Tests of the delete() function
	 */
	@Test
	public void delete() {
		final int x = computerDBService.getAll().size();
		computerDBService.delete(3L);
		assertEquals(x - 1, list.size());
	}
	
	@Test
	public void deleteInvalidId() {
		final int x = computerDBService.getAll().size();
		computerDAO.delete(-1);
		computerDAO.delete(4L);
		assertEquals(x, list.size());
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
		computerDBService.delete(l);
		l.forEach(id -> assertNull(computerDAO.getById(id)));
	}
}
