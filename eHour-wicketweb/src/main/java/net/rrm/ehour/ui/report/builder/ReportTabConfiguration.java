package net.rrm.ehour.ui.report.builder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ReportTabConfiguration {
    @Bean
    public ReportTabs reportTabs(List<ReportTabFactory> factories) {
        return new ReportTabs(factories);
    }
}
