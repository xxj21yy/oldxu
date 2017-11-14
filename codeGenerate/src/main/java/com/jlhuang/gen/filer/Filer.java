package com.jlhuang.gen.filer;

import com.jlhuang.gen.moudel.Column;
import com.jlhuang.gen.moudel.Table;

import java.util.List;

public interface Filer {

    void tableFiler(List<Table> tables);


    List<Column> columnFiler(List<Column> columnMap);
}
