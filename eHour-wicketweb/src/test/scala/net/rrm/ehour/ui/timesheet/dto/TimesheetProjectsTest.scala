package net.rrm.ehour.ui.timesheet.dto

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import net.rrm.ehour.domain.ActivityMother
import net.rrm.ehour.config.EhourConfigStub
import java.util.Arrays

@RunWith(classOf[JUnitRunner])
class TimesheetProjectsTest  extends FunSuite  with ShouldMatchers with BeforeAndAfter {



  test("should build from timesheetRows") {
    val activityA = ActivityMother.createActivity(1)
    activityA.getProject.setProjectCode("A")
    activityA.getProject.setName("B")

    val timesheetRowA = new TimesheetRow(new EhourConfigStub)
    timesheetRowA.setActivity(activityA)

    val timesheetRowB = new TimesheetRow(new EhourConfigStub)
    timesheetRowB.setActivity(activityA)

    val activityC = ActivityMother.createActivity(2)
    activityC.getProject.setProjectCode("B")
    activityC.getProject.setName("A")

    val timesheetRowC = new TimesheetRow(new EhourConfigStub)
    timesheetRowC.setActivity(activityC)

    val projects = TimesheetProjects(Arrays.asList(timesheetRowA, timesheetRowB, timesheetRowC))

    projects.getTimesheetRow(activityA.getProject).size() should be(2)
    projects.getTimesheetRow(activityC.getProject).size() should be(1)

    projects.get().iterator().next() should be (activityC.getProject)
  }
}