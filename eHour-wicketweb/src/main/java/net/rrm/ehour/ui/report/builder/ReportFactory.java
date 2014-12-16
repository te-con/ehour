package net.rrm.ehour.ui.report.builder;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Report Factory
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ReportFactory {
}
