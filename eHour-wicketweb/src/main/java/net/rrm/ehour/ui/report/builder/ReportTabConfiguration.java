package net.rrm.ehour.ui.report.builder;

import org.springframework.context.annotation.Bean;

import java.util.List;

@ReportFactoryConfiguration
public class ReportTabConfiguration {
    @Bean
    public ReportTabs reportTabs(List<ReportTabFactory> factories) {
        return new ReportTabs(factories);
    }
}
