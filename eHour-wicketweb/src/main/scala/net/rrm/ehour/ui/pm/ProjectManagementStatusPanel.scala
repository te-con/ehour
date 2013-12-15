package net.rrm.ehour.ui.pm

import net.rrm.ehour.domain.Project
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.border.GreyRoundedBorder
import net.rrm.ehour.report.service.AggregateReportService
import org.apache.wicket.spring.injection.annot.SpringBean
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement
import org.apache.wicket.markup.html.basic.Label
import org.apache.wicket.model.Model
import org.apache.wicket.markup.html.list.{ListItem, ListView}
import com.google.common.collect.Lists
import java.lang.{Float => JFloat}
import scala.Predef.String
import net.rrm.ehour.ui.common.wicket.NonEmptyLabel

class ProjectManagementStatusPanel(id: String, project: Project) extends AbstractBasePanel(id) {
  @SpringBean
  var aggregateReportService: AggregateReportService = _

  override def onInitialize() {
    super.onInitialize()

    val pmReport = aggregateReportService.getProjectManagerDetailedReport(project)

    val border = new GreyRoundedBorder("border")
    addOrReplace(border)

    val aggregates = Lists.newArrayList(pmReport.getAggregates)

    border.add(new ListView[AssignmentAggregateReportElement]("rows", aggregates) {
      def populateItem(item: ListItem[AssignmentAggregateReportElement]) {
        val aggregate = item.getModelObject

        val user = new NonEmptyLabel("user", new Model[String](aggregate.getProjectAssignment.getUser.getFullName))
        item.add(user)

        val booked = new NonEmptyLabel("booked", new Model[JFloat](JFloat.valueOf(aggregate.getHours.floatValue())))
        item.add(booked)

        val allotted = new NonEmptyLabel("allotted", new Model[JFloat](aggregate.getProjectAssignment.getAllottedHours))
        item.add(allotted)

        val overrun = new NonEmptyLabel("overrun", new Model[JFloat](aggregate.getProjectAssignment.getAllowedOverrun))
        item.add(overrun)

        val available = new NonEmptyLabel("available", new Model[JFloat](aggregate.getAvailableHours))
        item.add(available)

        val percentageUsed = new NonEmptyLabel("percentageUsed", new Model[JFloat](JFloat.valueOf(aggregate.getProgressPercentage)))
        item.add(percentageUsed)
      }
    })

    border.add(new Label("totalBooked", new Model[JFloat](pmReport.getTotalHoursBooked)))
    border.add(new Label("totalAvailable", new Model[JFloat](pmReport.getTotalHoursAvailable)))
  }
}
