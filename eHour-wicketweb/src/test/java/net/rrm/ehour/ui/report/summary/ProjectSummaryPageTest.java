package net.rrm.ehour.ui.report.summary;


import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import org.junit.Test;

public class ProjectSummaryPageTest extends AbstractSpringWebAppTester {
    @Test
    public void renderPage() {
        tester.startPage(ProjectSummaryPage.class);
    }
}
