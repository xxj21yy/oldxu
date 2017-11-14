package com.jlhuang.gen;

import com.jlhuang.gen.filer.Filer;
import com.jlhuang.gen.filer.QFYFiler;
import com.jlhuang.gen.genHandle.*;
import com.jlhuang.gen.message.Message;
import com.jlhuang.gen.moudel.Table;
import com.jlhuang.gen.sql.SqlParseUtil;
import org.apache.velocity.VelocityContext;

import java.util.Map;

public class Client {
    private static final String dateBaseName = "test";
    private static final String tableNameLike = "user";
    public static void start() throws Exception {
        Map<String, Table> tableMaps = SqlParseUtil.parseTableName(dateBaseName, tableNameLike);

        VelocityContext context = Message.getContextMore();
        Filer filer = new QFYFiler();
        ClientHandle clientHandle = new ClientHandle(context, tableMaps, filer);
        clientHandle.register(new MapperXmlGenerate());
        clientHandle.register(new MapperGenerate());
        clientHandle.register(new PojoGenerate());
        clientHandle.register(new DaoGenerate());
        clientHandle.register(new DaoImplGenerate());
        clientHandle.register(new ServiceGenerate());
        clientHandle.register(new ServiceImplGenerate());
        clientHandle.start();
    }

    public static void main(String[] args) throws Exception {
        start();
    }
}
