package com.yue.chip.core;

import org.apache.commons.dbutils.ColumnHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * @author SmartLinks
 */
public class LocalDateTimeColumnHandler implements ColumnHandler {
    @Override
    public boolean match(Class<?> propType) {
        return LocalDateTime.class.equals(propType);
    }

    @Override
    public Object apply(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getTimestamp(columnIndex).toLocalDateTime();
    }
}
