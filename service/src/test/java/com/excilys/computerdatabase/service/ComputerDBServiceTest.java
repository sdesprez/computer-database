package com.excilys.computerdatabase.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.repositories.ComputerRepository;
import com.excilys.computerdatabase.service.impl.ComputerDBServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ComputerDBServiceTest {

	@InjectMocks
	ComputerDBServiceImpl computerDBService;
	Page<Computer> page;
	Pageable pageable;
	
	ComputerRepository computerRepository;
	List<Computer> list;
	Company c1;
	Company c2;
	Computer computer;

	@Before
	public void init() {
		computerRepository = mock(ComputerRepository.class);

		c1 = new Company(1L, "company 1");
		c2 = new Company(2L, "company 2");

		list = new ArrayList<Computer>();
		list.add(new Computer(1L, "ordi 1", null, null, c1));
		list.add(new Computer(2L, "ordi 2", null, null, c1));
		list.add(new Computer(3L, "ordi 3", null, null, c2));

		pageable = new PageRequest(0, 5);

		computer = new Computer(4L, "ordi 4", null, null, c1);

		page = new PageImpl<Computer>(list, pageable, list.size());
		when(computerRepository.findAll()).thenReturn(list);
		when(computerRepository.findByNameContainingOrCompanyNameContaining(anyString(), anyString(), any(Pageable.class))).thenReturn(page);

		doAnswer(new Answer<Computer>() {
			@Override
			public Computer answer(final InvocationOnMock invocation) {
				final long l = (Long) invocation.getArguments()[0];
				if (l > 0 && l < list.size()) {
					return list.get((int) l - 1);
				}
				return null;
			}
		}).when(computerRepository).findOne(anyLong());

		doAnswer(new Answer<Computer>() {
			@Override
			public Computer answer(final InvocationOnMock invocation) {
				final Computer computer = (Computer) invocation.getArguments()[0];
				if (computer != null) {
					if (computer.getId() < 1 || computer.getId() >= list.size()) {
						list.add(computer);
					} else {
						list.set((int) computer.getId() - 1, computer);
					}
				}
				return null;
			}
		}).when(computerRepository).save(any(Computer.class));
		
		doAnswer(new Answer<Computer>() {
			@Override
			public Computer answer(final InvocationOnMock invocation) {
				final long l = (Long) invocation.getArguments()[0];
				list.removeIf(c -> c.getId() == l);
				return null;
			}
			
		}).when(computerRepository).delete(anyLong());
		
		/*
		when(computerRepository.getPagedList(page)).thenReturn(pageReturned);
		doThrow(BadSqlGrammarException.class).when(computerRepository).getPagedList(wrongPNumber);
		doThrow(BadSqlGrammarException.class).when(computerRepository).getPagedList(wrongRPP);
*/

		MockitoAnnotations.initMocks(this);
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
	 * Tests getPagedList
	 */
	@Test
	public void getPagedList() {
		assertEquals(page, computerDBService.getPagedList("" ,pageable));
	}

	@Test
	public void getPagedListNull() {
		assertNull(computerDBService.getPagedList("" ,null));
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
		computerDBService.create(null);
		assertEquals(list, computerDBService.getAll());
	}
	
	@Test
	public void createEmptyComputer() {
		computerDBService.create(new Computer());
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
		computerDBService.update(null);
		assertEquals(list, computerDBService.getAll());
	}
	
	@Test
	public void updateInvalidId() {
		final Computer computer = new Computer();
		computer.setId(-1L);
		computerDBService.update(computer);
		assertEquals(list, computerDBService.getAll());
	}
	
	@Test
	public void updateInvalidCompanyId() {
		final Computer computer = new Computer();
		computer.setId(1L);
		computer.setCompany(new Company(-1L, ""));
		computerDBService.update(computer);
		assertEquals(list, computerDBService.getAll());
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
		computerDBService.delete(-1);
		computerDBService.delete(4L);
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
		l.forEach(id -> assertNotNull(computerDBService.getById(id)));
		computerDBService.delete(l);
		l.forEach(id -> assertNull(computerDBService.getById(id)));
	}
}