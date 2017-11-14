package com.jlhuang.gen.moudel;

import com.jlhuang.gen.util.CodeUtil;
import com.jlhuang.gen.util.TypeUtils;
import org.apache.commons.lang.StringUtils;

public class Column {
    private String columnName;
    private String name;
    private String comment;
    private String dataType;//数据库type
    private String type;//javatype
    private String mybatisType;//mybatis type
    private String upName;


    public String getMybatisType() {
        if (StringUtils.isBlank(mybatisType)) {
            mybatisType = TypeUtils.getMybatisTypeByPre(dataType);
        }
        return mybatisType;
    }

    public void setMybatisType(String mybatisType) {
        this.mybatisType = mybatisType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getUpName() {
        if (StringUtils.isBlank(upName)) {
            upName = CodeUtil.toUpperCaseFirstOne(name);
        }
        return upName;
    }

    public void setUpName(String upName) {

        this.upName = upName;
    }

    public String getType() {
        if (StringUtils.isBlank(type)) {
            type = TypeUtils.getTypeByPre(dataType);
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
