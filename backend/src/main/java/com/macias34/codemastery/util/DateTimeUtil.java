package com.macias34.codemastery.util;

import java.sql.Timestamp;
import java.util.Date;

public class DateTimeUtil {

	public static Timestamp getCurrentTimestamp() {
		long currentTimestampMillis = System.currentTimeMillis();
		Date currentDate = new Date(currentTimestampMillis);
		return new Timestamp(currentDate.getTime());
	}
}
