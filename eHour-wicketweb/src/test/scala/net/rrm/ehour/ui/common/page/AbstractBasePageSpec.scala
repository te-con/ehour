package net.rrm.ehour.ui.common.page

import com.richemont.jira.JiraService
import com.richemont.windchill.WindChillUpdateService
import net.rrm.ehour.AbstractSpringWebAppSpec
import org.apache.wicket.model.ResourceModel

class AbstractBasePageSpec extends AbstractSpringWebAppSpec {
  "render" in {
    mockService[WindChillUpdateService]
    mockService[JiraService]

    tester.startPage(classOf[DummyPage])

    tester.assertNoErrorMessage()
    tester.assertNoInfoMessage()
    tester.assertRenderedPage(classOf[DummyPage])
  }
}

class DummyPage extends AbstractBasePage[Void](new ResourceModel("test"))