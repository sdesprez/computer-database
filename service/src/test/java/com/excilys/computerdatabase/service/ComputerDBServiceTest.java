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
import java.util.stream.Collectors;

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
	Computer computer1;
	Computer computer;

	@Before
	public void init() {
		computerRepository = mock(ComputerRepository.class);

		c1 = new Company(1L, "company 1");
		c2 = new Company(2L, "company 2");

		list = new ArrayList<Computer>();
		computer1 = new Computer(1L, "ordi 1", null, null, c1);
		list.add(computer1);
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
				List<Computer> comp = list.stream().filter(c -> c.getId()==l).collect(Collectors.toList());
				if (comp.isEmpty()) {
					return null;
				}
				else {
					return comp.get(0);
				}
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
		assertEquals(computerRepository.findOne(1L), computerDBService.getById(1L));
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
		List<Computer> l = new ArrayList<Computer>(list);
		computerDBService.create(null);
		assertEquals(l, list);
	}
	
	@Test
	public void createEmptyComputer() {
		List<Computer> l = new ArrayList<Computer>(list);
		computerDBService.create(new Computer());
		assertEquals(l, list);
	}

	
	/*
	 * Tests of update function
	 */
	@Test
	public void update() {
		computer1.setName("Updated");
		computerDBService.update(computer1);
		assertEquals(computer1, computerRepository.findOne(computer1.getId()));
	}
	
	@Test
	public void updateNull() {
		List<Computer> l = new ArrayList<Computer>(list);
		computerDBService.update(null);
		assertEquals(l, list);
	}
	
	@Test
	public void updateInvalidId() {
		final Computer computer = new Computer();
		List<Computer> l = new ArrayList<Computer>(list);
		computer.setId(-1L);
		computerDBService.update(computer);
		assertEquals(l, list);
	}
	
	@Test
	public void updateInvalidCompanyId() {
		final Computer computer = new Computer();
		List<Computer> l = new ArrayList<Computer>(list);
		computer.setId(1L);
		computer.setCompany(new Company(-1L, ""));
		computerDBService.update(computer);
		assertEquals(l, list);
	}
	
	
	/*
	 * Tests of the delete() function
	 */
	@Test
	public void delete() {
		final long id = 3L;
		computerDBService.delete(id);
		assertNull(computerRepository.findOne(id));
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
		l.forEach(id -> assertNotNull(computerRepository.findOne(id)));
		computerDBService.delete(l);
		l.forEach(id -> assertNull(computerRepository.findOne(id)));
	}
}
