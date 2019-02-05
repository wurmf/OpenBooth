package org.openbooth.application;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    private static void setStaticApplicationContext(ApplicationContext context){
        applicationContext = context;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {
        setStaticApplicationContext(context);
    }
}
