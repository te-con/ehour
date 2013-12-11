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
import scala.Predef.String
import org.apache.wicket.AttributeModifier
import java.lang.{Float => JFloat}

class ProjectManagementStatusPanel(id: String, project: Project) extends AbstractBasePanel(id) {
  @SpringBean
  var aggregateReportService: AggregateReportService = _

  override def onInitialize() {
    super.onInitialize()

    val pmReport = aggregateReportService.getProjectManagerDetailedReport(project)

    val border = new GreyRoundedBorder("border")
    addOrReplace(border)

    val aggregates = Lists.newArrayList(pmReport.getAggregates)

    border.add(new ListView[AssignmentAggregateReportElement]("report", aggregates) {
      def populateItem(item: ListItem[AssignmentAggregateReportElement]) {
        def applyCssAndAdd(label: Label) {
          if (item.getIndex == 0)
            label.add(AttributeModifier.append("class", "firstRow"))
          else if (item.getIndex % 2 != 0) {
            item.add(AttributeModifier.append("class", "oddRow"))
          }

          item.add(label)
        }

        val aggregate = item.getModelObject

        val user = new Label("user", aggregate.getProjectAssignment.getUser.getFullName)
        user.add(AttributeModifier.append("class", "firstColumn"))
        applyCssAndAdd(user)

        val booked = new Label("booked", new Model[JFloat](JFloat.valueOf(aggregate.getHours.floatValue())))
        applyCssAndAdd(booked)

        val allotted = new Label("allotted", new Model[JFloat](aggregate.getProjectAssignment.getAllottedHours))
        applyCssAndAdd(allotted)

        val overrun: Label = new Label("overrun", new Model[JFloat](aggregate.getProjectAssignment.getAllowedOverrun))
        applyCssAndAdd(overrun)

        val available: Label = new Label("available", new Model[JFloat](aggregate.getAvailableHours))
        applyCssAndAdd(available)

        val percentageUsed: Label = new Label("percentageUsed", new Model[JFloat](JFloat.valueOf(aggregate.getProgressPercentage)))
        applyCssAndAdd(percentageUsed)
      }
    })
  }
}
