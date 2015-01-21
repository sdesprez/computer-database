package com.excilys.computerdatabase.mapper;

import java.sql.ResultSet;
import java.util.List;

public interface RowMapper<T> {

	/**
	* Maps an element
	* @param resultSet The element that need to be mapped
	* @return A mapped instance of <T>
	*/
	T mapRow(ResultSet rs);
	/**
	* Maps a list of element
	* @param resultSet The list of element that need to be mapped
	* @return A mapped instance of List<T>
	*/
	List<T> mapRowList(ResultSet rs);
}
