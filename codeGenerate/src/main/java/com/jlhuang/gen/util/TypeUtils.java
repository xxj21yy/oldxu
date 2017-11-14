package com.jlhuang.gen.util;

import java.util.HashMap;
import java.util.Map;

public class TypeUtils {
    private static final String[][] x =
            {
                    {"bigint", "long", "BIGINT"},
                    {"varchar", "String", "VARCHAR"},
                    {"tinyint", "int", "TINYINT"},
                    {"datetime", "Date", "TIMESTAMP"}
            };

    private static final Map<String, String> typeMap;
    private static final Map<String, String> mybatistypeMap;
    static {
        typeMap = new HashMap<String, String>();
        mybatistypeMap = new HashMap<String, String>();
        for (int i = 0; i < x.length; i++) {
            typeMap.put(x[i][0], x[i][1]);
            mybatistypeMap.put(x[i][0], x[i][2]);
        }
    }

    public static String getTypeByPre(String pre) {
        if (!typeMap.containsKey(pre)) {
            throw new RuntimeException(pre+"类型未定义");
        }
        return typeMap.get(pre);
    }

    public static String getMybatisTypeByPre(String pre) {
        if (!mybatistypeMap.containsKey(pre)) {
            throw new RuntimeException(pre+"类型未定义");
        }
        return mybatistypeMap.get(pre);
    }

}
