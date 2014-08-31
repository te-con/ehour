package net.rrm.ehour.report.reports

import java.util.Calendar

import com.google.common.collect.Lists
import net.rrm.ehour.data.DateRange
import net.rrm.ehour.domain._
import net.rrm.ehour.report.criteria.UserSelectedCriteria
import net.rrm.ehour.report.reports.element.{ActivityAggregateReportElement, ActivityAggregateReportElementMother}
import net.rrm.ehour.util.DateUtil

import scala.collection.convert.WrapAsJava
import scala.collection.mutable

object AggregateReportDataObjectMother {
  def generateReportData: ReportData = {
    val customerA = new Customer(1).setName("Customer")
    val customerB = new Customer(2).setName("TECON")
    val customerC = new Customer(3).setName("CECON")
    val customerD = new Customer(4).setName("DECON")

    val projectA = new Project(1, customerA).setName("Project A")
    val projectB = new Project(2, customerA).setName("Project B")
    val projectC = new Project(3, customerB).setName("Project C")
    val projectD = new Project(4, customerC).setName("Project D")
    val projectE = new Project(5, customerD).setName("Project E")

    val userR = new User(1, "Rosalie", "Edeling")
    val userT = new User(2, "Thies", "Edeling")

    val reportElementA = new ActivityAggregateReportElement(new Activity(userR, projectA), 14)
    val reportElementB = new ActivityAggregateReportElement(new Activity(userR, projectB), 8)
    val reportElementC = new ActivityAggregateReportElement(new Activity(userT, projectC), 10)
    val reportElementD = new ActivityAggregateReportElement(new Activity(userT, projectD), 12)
    val reportElementE = new ActivityAggregateReportElement(new Activity(userT, projectE), 10)
    val reportElementF = new ActivityAggregateReportElement(new Activity(userT, projectB
    ), 10)

    val elements = mutable.Buffer(reportElementA, reportElementB, reportElementC, reportElementD, reportElementE, reportElementF)
    new ReportData(Lists.newArrayList(), WrapAsJava.bufferAsJavaList(elements), DateUtil.getDateRangeForMonth(Calendar.getInstance()), new UserSelectedCriteria())
  }

  def getActivityAggregateReportElements = {
    val pagA = ActivityAggregateReportElementMother.createActivityAggregate(1, 1, 1)
    pagA.getActivity.getProject.setProjectId(1)

    val pagB = ActivityAggregateReportElementMother.createActivityAggregate(20, 1, 2)
    pagB.getActivity.getProject.setProjectId(2)

    val pagC = ActivityAggregateReportElementMother.createActivityAggregate(3, 2, 1)
    val pagD = ActivityAggregateReportElementMother.createActivityAggregate(4, 2, 2)
    val pagE = ActivityAggregateReportElementMother.createActivityAggregate(5, 3, 3)

    val pagF = ActivityAggregateReportElementMother.createActivityAggregate(30, 1, 3)
    pagF.getActivity.getProject.setProjectId(2)

    WrapAsJava.bufferAsJavaList(mutable.Buffer(pagE, pagD, pagB, pagC, pagA, pagF))
  }

  def getReportData: ReportData = new ReportData(Lists.newArrayList(), getActivityAggregateReportElements, new DateRange, new UserSelectedCriteria())
}