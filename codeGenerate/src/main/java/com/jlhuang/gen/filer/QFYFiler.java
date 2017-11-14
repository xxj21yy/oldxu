package com.jlhuang.gen.filer;

import com.jlhuang.gen.moudel.Column;
import com.jlhuang.gen.moudel.Table;

import java.util.*;

public class QFYFiler extends AbstractFiler {
    String[] filerColumnsArray = {"createUser", "createTime", "updateUser", "updateTime","yn"};
    Set<String> filerColumns = new HashSet<String>(Arrays.asList(filerColumnsArray));

    @Override
    public List<Column> columnFiler(List<Column> columns) {
        List<Column> columnstemp = new ArrayList<Column>(columns);
        Iterator<Column> iterator = columnstemp.iterator();
        while (iterator.hasNext()) {
            Column next1 = iterator.next();
            String name = next1.getName();
            if (filerColumns.contains(name)) {
                iterator.remove();
            }
        }
        return columnstemp;
    }

    @Override
    public void tableFiler(List<Table> tables) {
        super.tableFiler(tables);
    }

}
