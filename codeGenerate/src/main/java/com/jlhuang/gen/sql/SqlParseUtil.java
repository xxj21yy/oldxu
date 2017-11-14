package com.jlhuang.gen.sql;

import com.jlhuang.gen.util.CodeUtil;
import com.jlhuang.gen.util.JdbcUtil;
import com.jlhuang.gen.moudel.Column;
import com.jlhuang.gen.moudel.Table;
import com.jlhuang.gen.util.ResourceUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlParseUtil {
    private static final String jdbc_driver = ResourceUtil.getProperty("datasource.driverClass");
    private static final String jdbc_url = ResourceUtil.getProperty("datasource.url");
    private static final String jdbc_username = ResourceUtil.getProperty("datasource.username");
    private static final String jdbc_password = ResourceUtil.getProperty("datasource.password");

    private static List<Table> tables = new ArrayList<Table>();
    public static Map<String, Table> parseTableName(String datebaseName,String tableNameLike) throws SQLException {
        Map<String, Table> tableMap = new HashMap<String, Table>();
        List<String> list = new ArrayList<String>();
        String sql = "SELECT TABLE_NAME,TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = ? AND table_name LIKE ?";
        JdbcUtil jdbcUtil = new JdbcUtil(jdbc_driver, jdbc_url, jdbc_username, jdbc_password);
        list.add(datebaseName);
        list.add(tableNameLike);
        List<Map> result = jdbcUtil.selectByParams(sql, list);
        for (Map map : result) {
//            System.out.println(map.get("TABLE_NAME"));
            Object table_name = map.get("TABLE_NAME");
            Object table_comment = map.get("TABLE_COMMENT");
            Table table = new Table((String)table_name,(String)table_comment);
            tables.add(table);
        }
        for (Table table1 : tables) {
            String tableName = table1.getTableName();
            sql = "SELECT column_name,data_type,column_comment,is_nullable,COLUMN_KEY FROM information_schema.`COLUMNS`\n" +
                    "WHERE table_schema = ? AND table_name = ?";
            list.clear();
            list.add(datebaseName);
            list.add(tableName );
            List<Map> mapTemp = jdbcUtil.selectByParams(sql, list);

            List<Column> columns = parseSqlToColumnList(mapTemp);
            table1.setColumns(columns);
            tableMap.put(table1.getCamelTableName(), table1);
        }

        jdbcUtil.release();
        return tableMap;
    }

    public static List<Column> parseSqlToColumnList(List<Map> mapTemp) {
        List<Column> columns = new ArrayList<Column>();
        for (Map map : mapTemp) {
            Column column = parseMaptoColumn(map);
            columns.add(column);
        }
        return columns;
    }

    public static Map<String, Column> parseSqlToColumnMap(List<Map> mapTemp) {
        Map<String, Column> columnMap = new HashMap<String, Column>();
        for (Map map : mapTemp) {
            Column column = parseMaptoColumn(map);
            columnMap.put(column.getName(), column);
        }
        return columnMap;
    }

    public static Column parseMaptoColumn(Map map) {
        Column column = new Column();
//        String is_nullable = (String) map.get("IS_NULLABLE");
        String column_name = (String) map.get("COLUMN_NAME");
//        String column_key = (String) map.get("COLUMN_KEY");
        String data_type = (String) map.get("DATA_TYPE");
        String column_comment = (String)map.get("COLUMN_COMMENT");
        column.setColumnName(column_name);
        column_name = CodeUtil.underlineToCamel(column_name);
        column.setComment(column_comment);
        column.setDataType(data_type);
        column.setName(column_name);
        return column;
    }

    public static void main(String[] args) throws Exception {
        Map<String, Table> stringTableMap = parseTableName("test", "user");
        System.out.println(1);

    }
}
