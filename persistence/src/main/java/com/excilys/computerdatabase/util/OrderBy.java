package com.excilys.computerdatabase.util;

import org.springframework.data.domain.Sort;

/**
 * Enum containing a link between a Sort and its column name and direction 
 */
public enum OrderBy {

	COMPUTER_NAME_ASC(new Sort(Sort.Direction.ASC, "name"), "name", "ASC"), 
	COMPUTER_NAME_DESC(new Sort(Sort.Direction.DESC, "name"), "name", "DESC"), 
	INTRODUCED_DATE_ASC(new Sort(Sort.Direction.ASC, "introduced"), "introduced", "ASC"), 
	INTRODUCED_DATE_DESC(new Sort(Sort.Direction.DESC, "introduced"), "introduced", "DESC"), 
	DISCONTINUED_DATE_ASC(new Sort(Sort.Direction.ASC, "discontinued"), "discontinued", "ASC"), 
	DISCONTINUED_DATE_DESC(new Sort(Sort.Direction.DESC, "discontinued"), "discontinued", "DESC"), 
	COMPANY_NAME_ASC(new Sort(Sort.Direction.ASC, "company.name"), "company.name", "ASC"), 
	COMPANY_NAME_DESC(new Sort(Sort.Direction.DESC, "company.name"), "company.name", "DESC");
	
	private Sort sort;
	private String colName = "name";
	private String dir = "ASC";

	private OrderBy(final Sort sort, final String colName, final String dir) {
		this.sort = sort;
		this.colName = colName;
		this.dir = dir;
	}

	public Sort getSort() {
		return sort;
	}


	public String getColName() {
		return colName;
	}

	public String getDir() {
		return dir;
	}

	/**
	 * Get the instance of OrderBy corresponding the sort parameter
	 * @param sort Sort from which we want the OrderBy instance
	 * @return The OrderBy corresponding the sort parameter or null if there is none
	 */
	public static OrderBy getOrderByFromSort(final Sort sort) {
		if (sort == null) {
			return null;
		}
		final OrderBy[] list = OrderBy.values();
		for (OrderBy ob : list) {
			if (ob.getSort().equals(sort)) {
				return ob;
			}
		}
		return null;
	}
}
