package net.rrm.ehour.ui.timesheet.dto

import java.util.Arrays

import net.rrm.ehour.config.EhourConfigStub
import net.rrm.ehour.domain.{ActivityMother, Project}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite, ShouldMatchers}

@RunWith(classOf[JUnitRunner])
class TimesheetProjectsTest extends FunSuite with ShouldMatchers with BeforeAndAfter {

  var prjA: Project = _
  var prjB: Project = _
  var projects: TimesheetProjects = _

  before {
    prjA = new Project(1)
    prjA.setProjectCode("B")
    prjA.setName("B")

    prjB = new Project(2)
    prjB.setProjectCode("A")
    prjB.setName("A")

    val activityA = ActivityMother.createActivity(1)
    activityA.setProject(prjA)
    activityA.setName("CDE")
    prjA.addActivity(activityA)

    val timesheetRowA = new TimesheetRow(new EhourConfigStub)
    timesheetRowA.setActivity(activityA)

    val activityB = ActivityMother.createActivity(2)
    activityB.setProject(prjA)
    activityB.setName("ABC")
    prjA.addActivity(activityB)

    val timesheetRowB = new TimesheetRow(new EhourConfigStub)
    timesheetRowB.setActivity(activityB)

    val activityC = ActivityMother.createActivity(3)
    activityC.setProject(prjB)
    activityC.setName("AXE")
    prjB.addActivity(activityC)

    val timesheetRowC = new TimesheetRow(new EhourConfigStub)
    timesheetRowC.setActivity(activityC)

    projects = TimesheetProjects(Arrays.asList(timesheetRowA, timesheetRowB, timesheetRowC))
  }

  test("should build from timesheetRows") {
    projects.getTimesheetRow(prjA).size() should be(2)
    projects.getTimesheetRow(prjB).size() should be(1)

    projects.get().iterator().next() should be(prjB)
  }
}