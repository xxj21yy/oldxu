package com.jlhuang.gen.filer;

import com.jlhuang.gen.moudel.Column;
import com.jlhuang.gen.moudel.Table;

import java.util.List;

public abstract class AbstractFiler implements Filer {
    @Override
    public void tableFiler(List<Table> tables) {

    }
    @Override
    public List<Column> columnFiler(List<Column> columns) {
        return columns;
    }

}
