package com.yue.chip.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.select.Select;

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
public class TenantSqlUtil {

    private static final String WHERE_TENANT_ID = "tenant_id = ";

    private static final String PATTERN_TENANT = "(((tenant_id){1}|(TENANT_ID){1})\\s*\\={1}\\s*\\?{1})";

    public static String sqlReplace(String sql) {
        //性能慢屏蔽
//        try {
//            Statements statements = CCJSqlParserUtil.parseStatements(sql);
//            List<Statement> list = statements.getStatements();
//            for (Statement statement : list){
//                if (statement instanceof Select) {
//                    Pattern pattern = Pattern.compile(PATTERN_TENANT);
//                    Matcher matcher = pattern.matcher(sql);
//                    if (matcher.find() ) {
//                        Long tenantId = CurrentUserUtil.getCurrentUserTenantId();
//                        sql = sql.replaceAll(PATTERN_TENANT, WHERE_TENANT_ID + (Objects.isNull( tenantId)?" null " :tenantId));
//                    }
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Long tenantId = CurrentUserUtil.getCurrentUserTenantId();
        sql.replace("#tenant_id#",String.valueOf(tenantId));
        return sql;
    }

    public static void main(String agrs[]) throws JSQLParserException {
        String sql = "select * from t_user where  1 = 1  and y=1 and tenant_id = ? order   by 3333,3434,3434";
        System.out.println(TenantSqlUtil.sqlReplace(sql));
    }
}
