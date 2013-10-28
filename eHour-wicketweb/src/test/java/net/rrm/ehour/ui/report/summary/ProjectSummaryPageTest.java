package net.rrm.ehour.ui.report.summary;


import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.junit.Test;

public class ProjectSummaryPageTest extends BaseSpringWebAppTester {
    @Test
    public void renderPage() {
        tester.startPage(ProjectSummaryPage.class);
    }
}
