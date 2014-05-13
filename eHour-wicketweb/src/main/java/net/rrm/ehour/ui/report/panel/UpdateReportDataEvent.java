package net.rrm.ehour.ui.report.panel;

import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.common.wicket.Event;
import org.apache.wicket.ajax.AjaxRequestTarget;

public class UpdateReportDataEvent extends Event {
    private final String cacheKey;
    private final ReportConfig reportConfig;

    public UpdateReportDataEvent(AjaxRequestTarget target, String cacheKey, ReportConfig reportConfig) {
        super(target);
        this.cacheKey = cacheKey;
        this.reportConfig = reportConfig;
    }

    public ReportConfig getReportConfig() {
        return reportConfig;
    }

    public String getCacheKey() {
        return cacheKey;
    }
}
