package org.openbooth.operating.impl;

import org.openbooth.operating.Operator;
import org.openbooth.operating.OperatorRunner;
import org.openbooth.operating.pipelines.impl.PreviewOperationPipeline;
import org.openbooth.operating.pipelines.impl.ShotOperationPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ThreadOperatorRunner implements OperatorRunner {

    private Operator operator;

    @Autowired
    public ThreadOperatorRunner(Operator operator){
        this.operator = operator;
    }

    public void startOperating(){

        new Thread(operator).start();
    }
}
