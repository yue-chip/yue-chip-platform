package com.yue.chip.core;

import org.apache.commons.dbutils.ColumnHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class LocalDateColumnHandler implements ColumnHandler {
    @Override
    public boolean match(Class<?> propType) {
        return LocalDate.class.equals(propType);
    }

    @Override
    public Object apply(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getDate(columnIndex).toLocalDate();
    }
}
