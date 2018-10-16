package org.openbooth;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"org.openbooth.util","org.openbooth.service"})
public class SpringTestEnvironment {

    private static AnnotationConfigApplicationContext applicationContext;

    protected ApplicationContext getApplicationContext(){
        if(applicationContext == null) {
            applicationContext = new AnnotationConfigApplicationContext();
            applicationContext.getEnvironment().setActiveProfiles("simulated_camera");
            applicationContext.register(SpringTestEnvironment.class);
            applicationContext.refresh();
        }
        return applicationContext;
    }

}
