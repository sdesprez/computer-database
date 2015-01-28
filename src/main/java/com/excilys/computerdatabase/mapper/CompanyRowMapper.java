package com.excilys.computerdatabase.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.exceptions.PersistenceException;

public class CompanyRowMapper implements RowMapper<Company> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyRowMapper.class);
	
	@Override
	public Company mapRow(final ResultSet rs, final int rowNum) {
		if (rs == null) {
			return null;
		}
		try {
			return Company.builder().id(rs.getLong("id")).name(rs.getString("name")).build();
		} catch (final SQLException e) {
			LOGGER.error("SQLException while mapping a company");
			throw new PersistenceException(e.getMessage(), e);
		}
	}
}
