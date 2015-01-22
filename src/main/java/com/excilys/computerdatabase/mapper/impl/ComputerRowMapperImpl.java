package com.excilys.computerdatabase.mapper.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.computerdatabase.domain.Company;
import com.excilys.computerdatabase.domain.Computer;
import com.excilys.computerdatabase.exceptions.PersistenceException;
import com.excilys.computerdatabase.mapper.RowMapper;

public class ComputerRowMapperImpl implements RowMapper<Computer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerRowMapperImpl.class);
	
	@Override
	public Computer mapRow(final ResultSet rs) {
		if (rs == null) {
			return null;
		}
		try {
			final Computer.Builder builder = Computer.builder().id(rs.getLong("id")).name(rs.getString("name"));
			final Timestamp introduced = rs.getTimestamp("introduced");
			final Timestamp discontinued = rs.getTimestamp("discontinued");
			if (introduced != null) {
				builder.introducedDate(introduced.toLocalDateTime().toLocalDate());
			}
			if (discontinued != null) {
				builder.discontinuedDate(discontinued.toLocalDateTime().toLocalDate());
			}
			final Long companyId = rs.getLong("company_Id");
			if (companyId != null) {
				builder.company(Company.builder().id(companyId).name(rs.getString("company_name")).build());
			}
			
			return builder.build();
		} catch (final SQLException e) {
			LOGGER.error("SQLException while mapping a computer");
			throw new PersistenceException(e.getMessage(), e);
		}
	}

	@Override
	public List<Computer> mapRowList(final ResultSet rs) {
		if (rs == null) {
			return null;
		}
		try {
			final List<Computer> computers = new ArrayList<Computer>();
			while (rs.next()) {
				computers.add(mapRow(rs));
			}
			return computers;
		} catch (final SQLException e) {
			LOGGER.error("SQLException while mapping a list of computers");
			throw new PersistenceException(e.getMessage(), e);
		}
	}

}
