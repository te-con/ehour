package net.rrm.ehour.ui.report.panel.criteria.quick

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import net.rrm.ehour.config.EhourConfigStub
import org.scalatest.{Matchers, BeforeAndAfter, FunSuite}
import java.util.Calendar
import org.joda.time.LocalDate

@RunWith(classOf[JUnitRunner])
class QuickWeekTest extends FunSuite with BeforeAndAfter with Matchers {
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

    new LocalDate(qw.getPeriodStart) should equal(new LocalDate(startCal.getTime))
    new LocalDate(qw.getPeriodEnd) should equal(new LocalDate(endCal.getTime))
  }
}
