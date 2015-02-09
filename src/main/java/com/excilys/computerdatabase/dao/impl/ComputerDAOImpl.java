package com.excilys.computerdatabase.dao.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.excilys.computerdatabase.dao.ComputerDAO;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.mapper.ComputerRowMapper;
import com.excilys.computerdatabase.utils.LocalDateConverter;

/**
 * Data Access Object for the Computer
 */
@Repository
public class ComputerDAOImpl implements ComputerDAO {
		
	/**
	 * Base Query for all the Select queries
	 */
	private static final String SELECT_QUERY = "SELECT c.id, c.name, c.introduced, c.discontinued, company_id, company.name as company_name "
												+ "FROM computer c LEFT JOIN company ON company.id=c.company_id";
	private static final String SELECT_BY_ID = SELECT_QUERY + " WHERE c.id=?";
	private static final String SELECT_BY_COMPANY = SELECT_QUERY + " WHERE company_id=?";
	private static final String INSERT_QUERY = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?,?,?,?)";
	private static final String UPDATE_QUERY = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id  =? WHERE id = ?";
	private static final String DELETE_QUERY = "DELETE computer FROM computer WHERE id = ?";
	private static final String DELETE_COMPANY_QUERY = "DELETE computer FROM computer WHERE computer.company_id = ?";
	private static final String COUNT_QUERY = "SELECT COUNT(c.id) AS total "
												+ "FROM computer c "
												+ "LEFT JOIN company ON company.id=c.company_id "
												+ "WHERE c.name LIKE ? OR company.name LIKE ?;";
	
	
	private RowMapper<Computer> computerMapper = new ComputerRowMapper();
	
	private JdbcTemplate jdbcTemplate;

	@Autowired
    public void setDataSource(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Computer> getAll() {
		return jdbcTemplate.query(SELECT_QUERY, computerMapper);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Computer getById(final long id) {
		final List<Computer> computers = jdbcTemplate.query(SELECT_BY_ID, new Long[]{id} , computerMapper);
		if (computers.isEmpty()) {
			return null;
		} else {
			return computers.get(0);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Computer> getByCompanyId(final long id) {
		return jdbcTemplate.query(SELECT_BY_COMPANY, new Long[]{id} , computerMapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(final Computer computer) {		
		final Company company = computer.getCompany();
		Long companyId = null;
		
		if (company != null) {
			companyId = company.getId();
		}
		
		final Object[] args = new Object[] {
				computer.getName(),
				LocalDateConverter.toTimestamp(computer.getIntroducedDate()),
				LocalDateConverter.toTimestamp(computer.getDiscontinuedDate()),
				companyId
		};
		
		jdbcTemplate.update(INSERT_QUERY, args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(final Computer computer) {
		final Company company = computer.getCompany();
		Long companyId = null;
		
		if (company != null) {
			companyId = company.getId();
		}
		
		final Object[] args = new Object[] {
				computer.getName(),
				LocalDateConverter.toTimestamp(computer.getIntroducedDate()),
				LocalDateConverter.toTimestamp(computer.getDiscontinuedDate()),
				companyId,
				computer.getId()
		};
		
		jdbcTemplate.update(UPDATE_QUERY, args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final long id) {
		jdbcTemplate.update(DELETE_QUERY, id);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final List<Long> list) {
		final List<Object[]> batchList = list.stream().map(l -> new Object[]{l}).collect(Collectors.toList());
		jdbcTemplate.batchUpdate(DELETE_QUERY, batchList);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<Computer> getPagedList(final Page<Computer> page) {
		final String search = "%" + page.getSearch() + "%";
		
		page.setNbResults(jdbcTemplate.queryForObject(COUNT_QUERY, new String[]{search, search}, Integer.class));
		page.refreshNbPages();

		//Create the SELECT query
		final String query = new StringBuilder(SELECT_QUERY).append(" WHERE c.name LIKE ? OR company.name LIKE ? ORDER BY ")
															.append(page.getSort())
															.append(" ").append(page.getOrder())
															.append(" LIMIT ? OFFSET ?;").toString();
		
		final Object[] args = new Object[] {
				search,
				search,
				page.getNbResultsPerPage(),
				(page.getPageNumber() - 1) * page.getNbResultsPerPage()
		};
		
		page.setList(jdbcTemplate.query(query, args, computerMapper));
		return page;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteByCompanyId(final long id) {
		jdbcTemplate.update(DELETE_COMPANY_QUERY, id);		
	}	
}
