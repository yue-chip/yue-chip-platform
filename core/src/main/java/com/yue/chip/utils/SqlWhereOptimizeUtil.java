package com.yue.chip.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.select.Select;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mr.Liu
 * @classname TenantSqlUtil
 * @description
 * @date 2022/04/15 下午6:20
 */
@Deprecated
//性能慢 不建议使用
public class SqlWhereOptimizeUtil {
    private static final String PATTERN_WHERE = "(((where){1}|(WHERE){1})\\s*1\\s*\\={1}\\s*1)";

    private static final String PATTERN_WHERE1 = "(((where){1}|(WHERE){1})\\s*1\\s*\\={1}\\s*1\\s*((and){1}|(AND){1}))";

    public static String sqlReplace(String sql) {
        try {
            Statements statements = CCJSqlParserUtil.parseStatements(sql);
            List<Statement> list = statements.getStatements();
            for (Statement statement : list) {
                if (statement instanceof Select) {
                    Pattern pattern = Pattern.compile(PATTERN_WHERE1);
                    Matcher matcher = pattern.matcher(sql);
                    if (matcher.find()) {
                        sql = sql.replaceAll(PATTERN_WHERE1, " where ");
                    }

                    pattern = Pattern.compile(PATTERN_WHERE);
                    matcher = pattern.matcher(sql);
                    if (matcher.find()) {
                        sql = sql.replaceAll(PATTERN_WHERE, " ");
                    }

                    break;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return sql;
    }

    public static void main(String agrs[]) throws JSQLParserException {
        Long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String sql = "select * from t_user where  1 = 1  and y=1 order   by 3333,3434,3434";
        System.out.println(SqlWhereOptimizeUtil.sqlReplace(sql));
        System.out.println(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()-milliSecond);

        Long milliSecond1 = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println(sql.replace(" where  1 = 1  ",""));
        System.out.println(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()-milliSecond1);
    }
}
