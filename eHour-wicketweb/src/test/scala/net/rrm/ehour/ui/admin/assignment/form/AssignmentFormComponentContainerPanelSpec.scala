package net.rrm.ehour.ui.admin.assignment.form

import net.rrm.ehour.AbstractSpringWebAppSpec
import org.apache.wicket.model.Model
import org.apache.wicket.markup.html.form.Form
import net.rrm.ehour.ui.admin.assignment.AssignmentAdminBackingBean
import java.util
import net.rrm.ehour.ui.admin.assignment.form.AssignmentFormComponentContainerPanel.DisplayOption
import net.rrm.ehour.project.service.{ProjectAssignmentService, ProjectService}
import net.rrm.ehour.customer.service.CustomerService
import org.mockito.Mockito._

class AssignmentFormComponentContainerPanelSpec extends AbstractSpringWebAppSpec {
  val customerService = mockService[CustomerService]

  mockService[ProjectService]
  mockService[ProjectAssignmentService]

  "Assignment Form Component Container" should {
    "render for creating an assignment" in {
      val mockedForm = mock[Form[AssignmentAdminBackingBean]]

      val bean = mock[AssignmentAdminBackingBean]
      when(bean.isNewAssignment).thenReturn(true)

      tester.startComponentInPage(new AssignmentFormComponentContainerPanel("id", mockedForm, new Model(bean), util.Arrays.asList(DisplayOption.SHOW_PROJECT_SELECTION)))
      tester.assertNoErrorMessage()
    }

    "render for editing an assignment" in {
      val mockedForm = mock[Form[AssignmentAdminBackingBean]]

      val bean = mock[AssignmentAdminBackingBean]
      when(bean.isNewAssignment).thenReturn(false)

      tester.startComponentInPage(new AssignmentFormComponentContainerPanel("id", mockedForm, new Model(bean), util.Arrays.asList(DisplayOption.SHOW_PROJECT_SELECTION)))
      tester.assertNoErrorMessage()
    }

  }
}
