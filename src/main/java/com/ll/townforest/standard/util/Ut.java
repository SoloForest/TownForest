package com.ll.townforest.standard.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ut {
	public static class url {
		public static String encode(String str) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return str;
			}
		}

		public static String modifyQueryParam(String url, String paramName, String paramValue) {
			url = deleteQueryParam(url, paramName);
			url = addQueryParam(url, paramName, paramValue);

			return url;
		}

		public static String addQueryParam(String url, String paramName, String paramValue) {
			if (url.contains("?") == false) {
				url += "?";
			}

			if (url.endsWith("?") == false && url.endsWith("&") == false) {
				url += "&";
			}

			url += paramName + "=" + paramValue;

			return url;
		}

		private static String deleteQueryParam(String url, String paramName) {
			int startPoint = url.indexOf(paramName + "=");
			if (startPoint == -1)
				return url;

			int endPoint = url.substring(startPoint).indexOf("&");

			if (endPoint == -1) {
				return url.substring(0, startPoint - 1);
			}

			String urlAfter = url.substring(startPoint + endPoint + 1);

			return url.substring(0, startPoint) + urlAfter;
		}
	}

	public static class date {
		public static int getEndDayOf(String yearMonth) {
			LocalDate convertedDate = LocalDate.parse(yearMonth + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			convertedDate = convertedDate.withDayOfMonth(
				convertedDate.getMonth().length(convertedDate.isLeapYear()));

			return convertedDate.getDayOfMonth();
		}

		public static LocalDateTime parse(String pattern, String dateText) {
			return LocalDateTime.parse(dateText, DateTimeFormatter.ofPattern(pattern));
		}

		public static LocalDateTime parse(String dateText) {
			return parse("yyyy-MM-dd HH:mm:ss.SSSSSS", dateText);
		}
	}
}