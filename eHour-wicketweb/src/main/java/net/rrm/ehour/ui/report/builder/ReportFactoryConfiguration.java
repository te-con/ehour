package net.rrm.ehour.ui.report.builder;

import org.springframework.context.annotation.Configuration;

import java.lang.annotation.*;

/**
 * Report Factory
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@ReportFactory
public @interface ReportFactoryConfiguration {
}
