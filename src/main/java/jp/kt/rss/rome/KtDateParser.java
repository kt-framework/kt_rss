package jp.kt.rss.rome;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日本のタイムゾーン用にカスタマイズしたクラス.
 * <p>
 * com.sun.syndication.io.impl.DateParserクラスをベースにカスタマイズしています.
 * </p>
 *
 * @author tatsuya.kumon
 */
class KtDateParser {
	private static final String TIMEZONE_TEXT = "JST";

	/**
	 * create a W3C Date Time representation of a date.
	 * <p/>
	 * Refer to the java.text.SimpleDateFormat javadocs for details on the
	 * format of each element.
	 * <p/>
	 *
	 * @param date
	 *            Date to parse
	 * @return the W3C Date Time represented by the given Date It returns
	 *         <b>null</b> if it was not possible to parse the date.
	 */
	public static String formatW3CDateTime(Date date) {
		StringBuilder sb = new StringBuilder();
		// タイムゾーン表示の前までを生成
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		format1.setTimeZone(TimeZone.getTimeZone(TIMEZONE_TEXT));
		sb.append(format1.format(date));
		// タイムゾーン表示を生成して追記
		SimpleDateFormat format2 = new SimpleDateFormat("Z");
		format2.setTimeZone(TimeZone.getTimeZone(TIMEZONE_TEXT));
		StringBuilder temp = new StringBuilder(format2.format(date));
		// Zだと「+0900」だが、RSSでは「+09:00」にする必要がある
		sb.append(temp.insert(3, ":"));
		return sb.toString();
	}
}
