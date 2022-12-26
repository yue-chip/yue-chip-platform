package com.yue.chip.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author liufang
 *
 */
public class MapToBeanUtil {

	public MapToBeanUtil() {
		super();
	}

	public static Object convert(Object obj, final Map<String, Object> map) {
		try {
			ConvertUtils.deregister(Date.class);
			DateTimeConverter dtConverter = new DateTimeConverter();
			LocalDateTimeConverter localDateTimeConverter = new LocalDateTimeConverter();
//			ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
//			BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());
			ConvertUtils.register(dtConverter, Date.class);
			ConvertUtils.register(localDateTimeConverter, Timestamp.class);
			ConvertUtils.register(new StringConverter(null), String.class);
			ConvertUtils.register(new LongConverter(null), Long.class);
			ConvertUtils.register(new BooleanConverter(null), Boolean.class);
			ConvertUtils.register(new DoubleConverter(null), Double.class);
			ConvertUtils.register(new IntegerConverter(null), Integer.class);
			ConvertUtils.register(new FloatConverter(null), Float.class);
			ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
			BeanUtils.populate(obj, map);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return obj;
	}

}

class DateTimeConverter implements Converter {
	private static final String DATE = "yyyy-MM-dd";
	private static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
	private static final String TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";

	@Override
	public Object convert(Class type, Object value) {
		return toDate(type, value);
	}

	public static Object toDate(Class type, Object value) {
		if (value == null || "".equals(value))
			return null;
		if (value instanceof String) {
			String dateValue = value.toString().trim();
			int length = dateValue.length();
			if (type.equals(Date.class)) {
				try {
					DateFormat formatter = null;
					if (length <= 10) {
						formatter = new SimpleDateFormat(DATE, new DateFormatSymbols(Locale.CHINA));
						return formatter.parse(dateValue);
					}
					if (length <= 19) {
						formatter = new SimpleDateFormat(DATETIME, new DateFormatSymbols(Locale.CHINA));
						return formatter.parse(dateValue);
					}
					if (length <= 23) {
						formatter = new SimpleDateFormat(TIMESTAMP, new DateFormatSymbols(Locale.CHINA));
						return formatter.parse(dateValue);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}
}


class LocalDateTimeConverter implements Converter {
	@Override
	public Object convert(Class type, Object value) {
		return toLocalDateTime(type, value);
	}

	public static LocalDateTime toLocalDateTime(Class type, Object value) {

		return ((Timestamp)value).toLocalDateTime();
	}
}
