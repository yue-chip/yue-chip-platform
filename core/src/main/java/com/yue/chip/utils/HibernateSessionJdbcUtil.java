package com.yue.chip.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

/**
 * @author coby
 * @description: TODO
 * @date 2024/1/16 下午2:58
 */
public class HibernateSessionJdbcUtil {

    public static void close(Object... objects) {
        for (Object obj : objects) {
            try {
                if (Objects.nonNull(obj) && obj instanceof Statement) {
                    ((Statement)obj).close();
                }else if (Objects.nonNull(obj) && obj instanceof PreparedStatement) {
                    ((PreparedStatement)obj).close();
                }else if (Objects.nonNull(obj) && obj instanceof ResultSet) {
                    ((ResultSet)obj).close();
                }
            } catch (SQLException e) {

            }
        }
    }
}
