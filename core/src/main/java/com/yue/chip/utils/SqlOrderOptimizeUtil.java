package com.yue.chip.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.select.Select;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * @author Mr.Liu
 * @classname TenantSqlUtil
 * @description
 * @date 2022/04/15 下午6:20
 */
@Deprecated
//性能慢 不建议使用
public class SqlOrderOptimizeUtil {
//    private static final String ORDER_BY = "(((order){1}|(ORDER){1})\\s*{1,}.*)";

    public static String sqlReplace(String sql) {
        //性能慢屏蔽
//        try {
//            Statements statements = CCJSqlParserUtil.parseStatements(sql);
//            List<Statement> list = statements.getStatements();
//            for (Statement statement : list){
//                if (statement instanceof Select) {
//                    Pattern pattern = Pattern.compile(ORDER_BY);
//                    Matcher matcher = pattern.matcher(sql);
//                    if (matcher.find() ) {
//                        sql = sql.replaceAll(ORDER_BY, " ");
//                    }
//                    break;
//                }
//            }
//        } catch (JSQLParserException e) {
//            throw new RuntimeException(e);
//        }
        try {
            Statements statements = CCJSqlParserUtil.parseStatements(sql);
            List<Statement> list = statements.getStatements();
            for (Statement statement : list) {
                if (statement instanceof Select) {
                    int index = sql.toLowerCase().lastIndexOf("order");
                    if (index>-1) {
                        sql = sql.substring(0,index);
                    }
                    return sql;
                }
            }

        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
        return sql;
    }

    public static void main(String agrs[]) throws JSQLParserException {
        Long milliSecond = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String sql = "select * from t_user where  1 = 1  and y=1 order   by 3333,3434,3434";
        System.out.println(SqlOrderOptimizeUtil.sqlReplace(sql));
        System.out.println(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() - milliSecond);
    }
}
