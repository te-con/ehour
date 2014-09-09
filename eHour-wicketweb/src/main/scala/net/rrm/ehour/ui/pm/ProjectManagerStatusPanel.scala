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
import com.google.common.base.Optional
import java.util.{Comparator, Collections}

class ProjectManagerStatusPanel(id: String, project: Project) extends AbstractBasePanel(id) {
  @SpringBean
  var aggregateReportService: AggregateReportService = _

  override def onBeforeRender() {
    super.onBeforeRender()

    val pmReport = aggregateReportService.getProjectManagerDetailedReport(project)

    val border = new GreyRoundedBorder("border")
    addOrReplace(border)

    val aggregates = Lists.newArrayList(pmReport.getAggregates)
    Collections.sort(aggregates, new Comparator[AssignmentAggregateReportElement]{
      override def compare(o1: AssignmentAggregateReportElement, o2: AssignmentAggregateReportElement): Int = o1.getProjectAssignment.getUser.compareTo(o2.getProjectAssignment.getUser)
    })


    border.add(new ListView[AssignmentAggregateReportElement]("rows", aggregates) {
      def populateItem(item: ListItem[AssignmentAggregateReportElement]) {
        val aggregate = item.getModelObject

        val user = new NonEmptyLabel("user", new Model[String](aggregate.getProjectAssignment.getUser.getFullName))
        item.add(user)

        val role = new NonEmptyLabel("role", new Model[String](aggregate.getProjectAssignment.getRole))
        item.add(role)

        val booked = new NonEmptyLabel("booked", new Model[JFloat](JFloat.valueOf(aggregate.getHours.floatValue())))
        item.add(booked)

        val converter = new OptionalFloatConverter()
        val available = new NonEmptyLabel("available", new Model[Optional[JFloat]](aggregate.getAvailableHours), Some(converter))
        item.add(available)

        val percentageUsed = new NonEmptyLabel("percentageUsed", new Model[Optional[JFloat]](aggregate.getProgressPercentage), Some(converter))
        item.add(percentageUsed)
      }
    })

    border.add(new Label("totalBooked", new Model[JFloat](pmReport.getTotalHoursBooked)))
    border.add(new Label("totalAvailable", new Model[JFloat](pmReport.getTotalHoursAvailable)))
  }
}

