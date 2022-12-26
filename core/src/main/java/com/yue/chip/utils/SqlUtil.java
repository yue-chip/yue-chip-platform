package com.yue.chip.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.select.Select;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlUtil {

    private static final String ORDER_BY = "((order){1}\\s{1,}(by){1}\\s{1,}.*)";

    public static String sqlReplaceOrderBy(String sql) throws JSQLParserException {
        Statements statements = CCJSqlParserUtil.parseStatements(sql);
        List<Statement> list = statements.getStatements();
        for (Statement statement : list){
            if (statement instanceof Select) {
                Pattern pattern = Pattern.compile(ORDER_BY);
                Matcher matcher = pattern.matcher(sql);
                if (matcher.find() ) {
                    sql = sql.replaceAll(ORDER_BY, " ");
                    sql = sql.replaceAll(ORDER_BY.toUpperCase(), " ");
                }
                break;
            }
        }
        return sql;
    }

    public static void main(String agrs[]) throws JSQLParserException {
        String sql = "select order1121 as '111' from t_order where  1 = 1  and y=1 order   by 3333,3434,3434";
        System.out.println(SqlUtil.sqlReplaceOrderBy(sql));
    }
}
