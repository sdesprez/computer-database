package com.excilys.computerdatabase.utils;

import java.sql.Timestamp;
import java.time.LocalDate;

public class LocalDateConverter {

	
	public static Timestamp toTimestamp(final LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		return Timestamp.valueOf(localDate.atStartOfDay());
	}
}
