package com.excilys.computerdatabase.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.excilys.computerdatabase.dao.CompanyDAO;
import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Page;
import com.excilys.computerdatabase.mapper.CompanyRowMapper;

/**
 * Data Access Object for the Computer
 */
@Repository
public class CompanyDAOImpl implements CompanyDAO {
	
	private static final String SELECT_QUERY = "SELECT * FROM company";
	private static final String SELECT_BY_ID = SELECT_QUERY + " WHERE company.id=?;";
	private static final String COUNT_QUERY = "SELECT COUNT(id) AS total FROM company";
	private static final String DELETE_QUERY = "DELETE company FROM company WHERE id = ?";
	private RowMapper<Company> companyMapper = new CompanyRowMapper();

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    public void setDataSource(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Company> getAll() {
		return jdbcTemplate.query(SELECT_QUERY, companyMapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Company getById(final long id) {
		final List<Company> companies = jdbcTemplate.query(SELECT_BY_ID, new Long[]{id} , companyMapper);
		if (companies.isEmpty()) {
			return null;
		} else  {
			return companies.get(0);
		} 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<Company> getPagedList(final Page<Company> page) {		
		page.setNbResults(jdbcTemplate.queryForObject(COUNT_QUERY, Integer.class));
		page.refreshNbPages();

		//Create the SELECT query
		final String query = new StringBuilder(SELECT_QUERY)
				.append(" LIMIT ? OFFSET ?;").toString();
		
		final Object[] args = new Object[] {
				page.getNbResultsPerPage(),
				(page.getPageNumber() - 1) * page.getNbResultsPerPage()
		};
		
		page.setList(jdbcTemplate.query(query, args, companyMapper));
		return page;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final long id) {
		jdbcTemplate.update(DELETE_QUERY, id);
	}
}
