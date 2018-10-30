package org.openbooth;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This class uses a xml configuration file to provide dependency injection for testing purposes
 */
public class SpringTestEnvironment {

    private static ApplicationContext applicationContext;

    protected ApplicationContext getApplicationContext(){
        if(applicationContext == null) {
            applicationContext = new ClassPathXmlApplicationContext("testing_beans.xml");
        }
        return applicationContext;
    }

}
