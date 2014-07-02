package net.rrm.ehour.ui.manage.assignment.form

import java.util

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.customer.service.CustomerService
import net.rrm.ehour.project.service.{ProjectAssignmentService, ProjectService}
import net.rrm.ehour.ui.manage.assignment.AssignmentAdminBackingBean
import net.rrm.ehour.ui.manage.assignment.form.AssignmentFormComponentContainerPanel.DisplayOption
import org.apache.wicket.markup.html.form.Form
import org.apache.wicket.model.Model
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
