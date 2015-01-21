package com.excilys.computerdatabase.mapper.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.exceptions.PersistenceException;
import com.excilys.computerdatabase.mapper.RowMapper;

public class CompanyRowMapperImpl implements RowMapper<Company> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyRowMapperImpl.class);
	
	@Override
	public Company mapRow(final ResultSet rs) {
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

	@Override
	public List<Company> mapRowList(final ResultSet rs) {
		if (rs == null) {
			return null;
		}
		try {
			final List<Company> companies = new ArrayList<Company>();
			while (rs.next()) {
				companies.add(mapRow(rs));
			}
			return companies;
		} catch (final SQLException e) {
			LOGGER.error("SQLException while mapping a list of companies");
			throw new PersistenceException(e.getMessage(), e);
		}
	}

}
