package net.rrm.ehour.ui.report.builder;

import com.google.common.base.Optional;
import net.rrm.ehour.report.criteria.ReportCriteria;
import org.apache.wicket.extensions.markup.html.tabs.ITab;

import java.io.Serializable;

public interface ReportTabFactory extends Serializable {
    Optional<ITab> createReportTab(ReportCriteria criteria);

    int getRenderPriority();
}
