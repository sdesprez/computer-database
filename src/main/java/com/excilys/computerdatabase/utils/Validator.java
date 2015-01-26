package com.excilys.computerdatabase.utils;

import com.excilys.computerdatabase.dto.ComputerDTO;

/**
 * Utilitary class to Validate the format of Strings
 */
public class Validator {

	/**
	 * Regex expression for date with yyyy-MM-dd format
	 */
	private static final String DATE_PATTERN = "("
			+ "((\\d{4})([-])(0[13578]|10|12)([-])(0[1-9]|[12][0-9]|3[01]))"
			+ "|((\\d{4})([-])(0[469]|11)([-])([0][1-9]|[12][0-9]|30))"
			+ "|((\\d{4})([-])(02)([-])(0[1-9]|1[0-9]|2[0-8]))"
			+ "|(([02468][048]00)([-])(02)([-])(29))"
			+ "|(([13579][26]00)([-])(02)([-])(29))"
			+ "|(([0-9][0-9][0][48])([-])(02)([-])(29))"
			+ "|(([0-9][0-9][2468][048])([-])(02)([-])(29))"
			+ "|(([0-9][0-9][13579][26])([-])(02)([-])(29))" + ")";

	/**
	 * Regex expression for a long
	 */
	private static final String POSITIVE_LONG_PATTERN = "\\d{1,19}";

	/**
	 * Regex expression for a int
	 */
	private static final String POSITIVE_INT_PATTERN = "\\d{1,9}";

	/**
	 * Check if a String as the format of a date
	 * 
	 * @param date
	 *            String to check
	 * @return true if the format if correct
	 */
	public static boolean isDate(final String date) {
		if (date == null || date.trim().isEmpty()) {
			return false;
		}
		if (!date.matches(DATE_PATTERN)) {
			return false;
		}
		final int year = Integer.valueOf(date.substring(0, 4));
		if (year < 1970 || year > 2038) {
			return false;
		}
		if (year == 2038) {
			if (new Integer(date.substring(5, 7)) > 1) {
				return false;
			} else {
				if (new Integer(date.substring(8)) > 19) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Check if a String as the format of a long and positive
	 * 
	 * @param string
	 *            String to check
	 * @return true if the format if correct
	 */
	public static boolean isPositiveLong(final String string) {
		if (string == null || string.trim().isEmpty()) {
			return false;
		}
		if (!string.matches(POSITIVE_LONG_PATTERN)) {
			return false;
		}

		return true;
	}

	/**
	 * Check if a String is a valid name
	 * 
	 * @param string
	 *            String to check
	 * @return true if it's a valid name
	 */
	public static boolean isName(final String name) {
		if (name == null) {
			return false;
		}
		if (name.trim().isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Check if a String as the format of an int and positive
	 * 
	 * @param string
	 *            String to check
	 * @return true if the format if correct
	 */
	public static boolean isPositiveInt(final String integer) {
		if (integer == null || integer.trim().isEmpty()) {
			return false;
		}
		if (!integer.matches(POSITIVE_INT_PATTERN)) {
			return false;
		}

		return true;
	}

	/**
	 * Check if the fields of a ComputerDTO are valid for a Computer
	 * 
	 * @param dto
	 *            ComputerDTO to check
	 * @return Return true if the ComputerDTO is valid.
	 */
	public static boolean validComputerDTO(final ComputerDTO dto) {
		if (dto == null) {
			return false;
		}
		if (dto.getId() < 0) {
			return false;
		}
		if (dto.getName() == null) {
			return false;
		}
		if (dto.getName().trim().isEmpty()) {
			return false;
		}
		if (dto.getIntroduced() != null) {
			if (!isDate(dto.getIntroduced())) {
				return false;
			}
		}
		if (dto.getDiscontinued() != null) {
			if (!isDate(dto.getDiscontinued())) {
				return false;
			}
		}
		if (dto.getCompany() < 0) {
			return false;
		}
		return true;
	}
}
