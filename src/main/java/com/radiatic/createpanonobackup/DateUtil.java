package com.radiatic.createpanonobackup;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DateUtil {

	public static final DateTimeZone tzUtc = DateTimeZone.forID("UTC");

	public static DateTime now() {
		return new DateTime().withZone(tzUtc);
	}
}
