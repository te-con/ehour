package net.rrm.ehour.ui.pm

import net.rrm.ehour.AbstractSpringWebAppSpec
import net.rrm.ehour.domain.ProjectObjectMother
import org.scalatest.BeforeAndAfterAll
import net.rrm.ehour.report.service.AggregateReportService
import org.mockito.Mockito._
import net.rrm.ehour.report.reports.ProjectManagerReport
import net.rrm.ehour.report.reports.element.{AssignmentAggregateReportElement, AssignmentAggregateReportElementMother}
import com.google.common.collect.Sets

class ProjectManagerStatusPanelSpec extends AbstractSpringWebAppSpec with BeforeAndAfterAll  {
  val aggregateReportService = mock[AggregateReportService]

  override def beforeAll() {
    springTester.getMockContext.putBean(aggregateReportService)
    springTester.setUp()
  }

  "Project Management Status Panel" should {
    "render" in {
      val project = ProjectObjectMother.createProject(1)
      val report = new ProjectManagerReport()

      val aggregate = AssignmentAggregateReportElementMother.createProjectAssignmentAggregate()

      val aggregates = Sets.newTreeSet[AssignmentAggregateReportElement]
      aggregates.add(aggregate)
      report.setAggregates(aggregates)

      when(aggregateReportService.getProjectManagerDetailedReport(project)).thenReturn(report)
      tester.startComponentInPage(new ProjectManagerStatusPanel("id", project))
      tester.assertNoErrorMessage()
    }
  }
}
