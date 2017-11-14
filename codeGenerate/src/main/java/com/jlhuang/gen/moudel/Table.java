package com.jlhuang.gen.moudel;

import com.jlhuang.gen.util.CodeUtil;

import java.util.List;

public class Table {
    private String tableName;
    private String comment;
    private String camelTableName;
    private List<Column> columns;


    public Table(String tableName, String comment) {
        this.tableName = tableName;
        this.comment = comment;
        this.camelTableName = CodeUtil.toUpperCaseFirstOne(CodeUtil.underlineToCamel(tableName));
    }

    public String getTableName() {
        return tableName;
    }

    public String getComment() {
        return comment;
    }

    public String getCamelTableName() {
        return camelTableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
}
