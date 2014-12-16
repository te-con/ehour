package net.rrm.ehour.ui.report.builder;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReportTabs {
    private List<ReportTabFactory> factories;

    // cglib..
    public ReportTabs() {
        factories = Lists.newArrayList();
    }

    public ReportTabs(List<ReportTabFactory> factories) {
        this.factories = sortByRenderPriority(factories);
    }

    private List<ReportTabFactory> sortByRenderPriority(List<ReportTabFactory> factories) {
        List<ReportTabFactory> sortedClone = Lists.newArrayList(factories);

        Collections.sort(sortedClone, new Comparator<ReportTabFactory>() {
            @Override
            public int compare(ReportTabFactory o1, ReportTabFactory o2) {
                return o1.getRenderPriority() - o2.getRenderPriority();
            }
        });

        return sortedClone;

    }

    public List<ReportTabFactory> getTabFactories() {
        return factories;
    }

    public void setFactories(List<ReportTabFactory> factories) {
        this.factories = factories;
    }
}
