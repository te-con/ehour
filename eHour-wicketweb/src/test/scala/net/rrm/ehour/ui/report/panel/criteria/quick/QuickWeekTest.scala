package net.rrm.ehour.ui.report.panel.criteria.quick

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import net.rrm.ehour.config.EhourConfigStub
import org.scalatest.{BeforeAndAfter, FunSuite}
import java.util.{Date, Calendar}
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class QuickWeekTest extends FunSuite with BeforeAndAfter with ShouldMatchers {
  var cal: Calendar = _
  var config: EhourConfigStub = _
  before {
    cal = Calendar.getInstance()
    cal.set(2012, Calendar.DECEMBER, 21)
    
    config = new EhourConfigStub()
    config.setFirstDayOfWeek(Calendar.MONDAY)
  }
  
  test("weeks are 7 day long") {
    val startCal = Calendar.getInstance()
      startCal.set(2012, Calendar.DECEMBER, 17)
    val endCal = Calendar.getInstance()
      endCal.set(2012, Calendar.DECEMBER, 23)
    val qw = new QuickWeek(cal, config)

    IsTheSameDay(startCal.getTime, qw.getPeriodStart)
    IsTheSameDay(endCal.getTime, qw.getPeriodEnd)
  }
  
  private def IsTheSameDay(expected: Date,  toCheck: Date) {
    toCheck should have (
      'day (expected.getDay),
      'month (expected.getMonth),
      'year (expected.getYear)
    )
  }
}
