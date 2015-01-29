package com.excilys.computerdatabase.dao.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.excilys.computerdatabase.dao.ComputerDAO;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.exceptions.PersistenceException;
import com.excilys.computerdatabase.mapper.ComputerRowMapper;

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
	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDAOImpl.class);
	
	
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
		if (computers.size() == 1) {
			return computers.get(0);
		} else if (computers.size() == 0) {
			return null;
		} else {
			LOGGER.error("There was more than 1 computer with id={} in the database", id);
			throw new PersistenceException("There was more than 1 computer with id=" + id + " in the database");
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
		if (computer == null || StringUtils.isEmpty(computer.getName())) {
			return;
		}
		
		final Company company = computer.getCompany();
		Long companyId = null;
		final LocalDate introducedDate = computer.getIntroducedDate();
		final LocalDate discontinuedDate = computer.getDiscontinuedDate();
		Timestamp introduced = null;
		Timestamp discontinued = null;
		
		if (introducedDate != null) {
			introduced = Timestamp.valueOf(introducedDate.atStartOfDay());
		}
		if (discontinuedDate != null) {
			discontinued =  Timestamp.valueOf(discontinuedDate.atStartOfDay());
		} 		
		
		if (company != null) {
			companyId = company.getId();
		}
		
		final Object[] args = new Object[] {
				computer.getName(),
				introduced,
				discontinued,
				companyId
		};
		
		jdbcTemplate.update(INSERT_QUERY, args);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(final Computer computer) {
		if (computer == null || StringUtils.isEmpty(computer.getName())) {
			return;
		}
		
		final Company company = computer.getCompany();
		Long companyId = null;
		final LocalDate introducedDate = computer.getIntroducedDate();
		final LocalDate discontinuedDate = computer.getDiscontinuedDate();
		Timestamp introduced = null;
		Timestamp discontinued = null;
		
		if (introducedDate != null) {
			introduced = Timestamp.valueOf(introducedDate.atStartOfDay());
		}
		if (discontinuedDate != null) {
			discontinued =  Timestamp.valueOf(discontinuedDate.atStartOfDay());
		} 		
		
		if (company != null) {
			companyId = company.getId();
		}
		
		final Object[] args = new Object[] {
				computer.getName(),
				introduced,
				discontinued,
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
		if (page == null) {
			return null;
		}
		
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
