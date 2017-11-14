package com.jlhuang.gen;

import com.jlhuang.gen.util.CodeUtil;
import com.jlhuang.gen.filer.Filer;
import com.jlhuang.gen.genHandle.Generate;
import com.jlhuang.gen.moudel.Table;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.util.*;

public class ClientHandle {

    private List<Generate> generates = new ArrayList<Generate>();
    private VelocityContext context;
    private Map<String, Table> tableMaps;
    private Filer filer;
    public void start() throws Exception {
        File directory = new File("");
        String canonicalPath = directory.getCanonicalPath();
        context.put("base_path", canonicalPath);
        Set<Map.Entry<String, Table>> entries = tableMaps.entrySet();
        Iterator<Map.Entry<String, Table>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Table> next = iterator.next();
            Table value = next.getValue();
            String camelTableName = value.getCamelTableName();
            String comment = value.getComment();
            String tableName = value.getTableName();
            context.put("model", camelTableName);
            context.put("model_low_first", CodeUtil.toLowerCaseFirstOne(camelTableName));
            context.put("table", tableName);
            context.put("table_coment", comment);
            for (int i = 0; i < generates.size(); i++) {
                generates.get(i).proess(null, filer, context, value);
            }

        }
    }

    public ClientHandle(VelocityContext context, Map<String, Table> tableMaps, Filer filer) {
        this.context = context;
        this.tableMaps = tableMaps;
        this.filer = filer;
    }

    public void register(Generate generate) {
        generates.add(generate);
    }

    public Map<String, Table> getTableMaps() {
        return tableMaps;
    }

    public void setTableMaps(Map<String, Table> tableMaps) {
        this.tableMaps = tableMaps;
    }
}
