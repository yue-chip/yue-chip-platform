package com.yue.chip.core;

import com.yue.chip.core.common.enums.Delete;
import org.apache.commons.dbutils.ColumnHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumColumnHandler implements ColumnHandler {

    private Class<?> propType;

    @Override
    public boolean match(Class<?> propType) {
        Boolean isEnum = propType.isEnum();
        if (isEnum) {
            this.propType = propType;
        }
        return isEnum;
    }

    @Override
    public Object apply(ResultSet rs, int columnIndex) throws SQLException {
        if (propType.isEnum()) {
            try {
                Method method = propType.getMethod("instance",Object.class);
                return method.invoke(propType,rs.getInt(columnIndex));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> classz = Delete.class;
        Method method = classz.getMethod("instance",Object.class);
        System.out.println(method.invoke(classz,0));
    }
}
