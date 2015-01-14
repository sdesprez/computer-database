package com.excilys.computerdatabase.utils;

/**
 * Utilitary class to Validate the format of Strings
 */
public class Validator {

	/**
	 * Regex expression for date with dd/MM/yyyy format
	 */
	private static final String DATE_PATTERN = "(((0[1-9]|[12][0-9]|3[01])([/])(0[13578]|10|12)([/])(\\d{4}))"
			+ "|(([0][1-9]|[12][0-9]|30)([/])(0[469]|11)([/])(\\d{4}))"
			+ "|((0[1-9]|1[0-9]|2[0-8])([/])(02)([/])(\\d{4}))|((29)(\\.|-|\\/)(02)([/])([02468][048]00))"
			+ "|((29)([/])(02)([/])([13579][26]00))"
			+ "|((29)([/])(02)([/])([0-9][0-9][0][48]))"
			+ "|((29)([/])(02)([/])([0-9][0-9][2468][048]))"
			+ "|((29)([/])(02)([/])([0-9][0-9][13579][26])))";
	/**
	 * Regex expression for a long
	 */
	private static final String LONG_PATTERN = "^-?\\d{1,19}$";
	
	
	/**
	 * Check if a String as the format of a date
	 * @param date String to check
	 * @return true if the format if correct
	 */
	public static boolean validateDate(String date) {
		if (date == null || date.trim().isEmpty()) {
			return false;
		}
		if (!date.matches(DATE_PATTERN)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check if a String as the format of a long
	 * @param string String to check
	 * @return true if the format if correct
	 */
	public static boolean validateLong(String string) {
		if (string == null || string.trim().isEmpty()) {
			return false;
		}
		if (!string.matches(LONG_PATTERN)) {
			return false;
		}
		
		return true;
	}
}
