package com.jlhuang.gen.genHandle;

import com.jlhuang.gen.filer.Filer;
import com.jlhuang.gen.moudel.Table;
import org.apache.velocity.VelocityContext;

public interface Generate {

    void proess(String modelPath, Filer filer, VelocityContext context, Table table);

}
