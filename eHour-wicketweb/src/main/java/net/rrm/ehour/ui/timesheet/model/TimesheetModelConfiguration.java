package net.rrm.ehour.ui.timesheet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class TimesheetModelConfiguration {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public PersistableTimesheetModel<TimesheetContainer> createTimesheetModel() {
        return new TimesheetModel();
    }
}
