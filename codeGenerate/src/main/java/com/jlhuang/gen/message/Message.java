package com.jlhuang.gen.message;

import org.apache.velocity.VelocityContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    public static final String MAPPER_VM_PATH;
    public static final String MAPPER_XML_VM_PATH;
    public static final String POJO_VM_PATH;
    public static final String DAO_VM_PATH;
    public static final String DAO_IMPL_VM_PATH;
    public static final String SERVICE_VM_PATH;
    public static final String SERVICE_IMPL_VM_PATH;
    static {
        Class<? extends Thread> aClass = Thread.currentThread().getClass();
        MAPPER_VM_PATH = aClass.getResource("/template/hcq/Mapper.vm").getPath();
        MAPPER_XML_VM_PATH = aClass.getResource("/template/hcq/MapperXML.vm").getPath();
        POJO_VM_PATH = aClass.getResource("/template/hcq/Pojo.vm").getPath();
        DAO_VM_PATH = aClass.getResource("/template/hcq/Dao.vm").getPath();
        DAO_IMPL_VM_PATH = aClass.getResource("/template/hcq/DaoImpl.vm").getPath();
        SERVICE_VM_PATH = aClass.getResource("/template/hcq/Service.vm").getPath();
        SERVICE_IMPL_VM_PATH = aClass.getResource("/template/hcq/ServiceImpl.vm").getPath();
    }

    public static final String author = "hcq";


    public static VelocityContext singleContext() {
        VelocityContext context = new VelocityContext();
        context.put("author", author);
        context.put("ctime", new SimpleDateFormat("yyyy/M/d").format(new Date()));
        return context;
    }


    public static VelocityContext getContextMore() {
        VelocityContext context = new VelocityContext();
        context.put("author", author);
        context.put("ctime", new SimpleDateFormat("yyyy/M/d").format(new Date()));
        context.put("base_mapper_package", "com.qfy.base.dto");
        context.put("base_entry_package", "com.qfy.base.dto");
        context.put("mapper_package", "com.qfy.mapper");
        context.put("entry_package", "com.qfy.pojo");
        context.put("dao_package", "com.qfy.dao");
        context.put("dao_impl_package", "com.qfy.dao.impl");
        context.put("service_package", "com.qfy.service");
        context.put("service_impl_package", "com.qfy.service.impl");
        context.put("controller_package", "com.qfy.controller");

        return context;
    }
}
