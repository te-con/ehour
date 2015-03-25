package net.rrm.ehour.persistence.database;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;


// very dirty hack to be able to expose the springcontext to the test dao classes so they can pluck the datasource out of the context
// used to be no problem when the datasource was created (and registered in jndi) by the ehourserverrunner class
@Repository
public class SpringContext implements ApplicationContextAware {
    private static ApplicationContext context;

    @Autowired
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }
    public static ApplicationContext getApplicationContext() {
        return context;
    }
}
