package net.rrm.ehour.ui.report.summary;

import com.richemont.jira.JiraService;
import com.richemont.windchill.WindChillUpdateService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProjectSummaryPageTest extends BaseSpringWebAppTester {
    @Mock
    private WindChillUpdateService windChillUpdateService;

    @Mock
    private JiraService jiraService;

    @Test
    public void renderPage() {
        MockitoAnnotations.initMocks(this);
        getMockContext().putBean("windChillUpdateService", windChillUpdateService);
        getMockContext().putBean("jiraService", jiraService);

        tester.startPage(ProjectSummaryPage.class);
    }
}
