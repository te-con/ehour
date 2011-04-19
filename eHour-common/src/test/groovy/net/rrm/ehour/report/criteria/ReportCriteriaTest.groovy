package net.rrm.ehour.report.criteria

import net.rrm.ehour.data.DateRange
import net.rrm.ehour.util.DateUtil
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.assertEquals

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 * Created on: 12/7/10 - 12:55 AM
 */
class ReportCriteriaTest
{
  AvailableCriteria availCriteria
  DateRange nowRange

  @Before
  void setUp()
  {
    availCriteria = new AvailableCriteria(reportRange: new DateRange())
    nowRange = DateUtil.calendarToMonthRange(new GregorianCalendar())
  }

  @Test
  void shouldGetOnEmptyUserCriteria()
  {
    def criteria = new ReportCriteria(availCriteria, new UserCriteria(reportRange: new DateRange()))

    assertEquals nowRange, criteria.reportRange
  }

  @Test
  void shouldGetWithUserCriteriaStart()
  {
    def criteria = new ReportCriteria(availCriteria, new UserCriteria(reportRange: new DateRange(dateStart: new Date() + 1)))

    assertEquals DateUtil.nullifyTime(new Date() + 1), criteria.reportRange.dateStart
  }

  @Test
  void shouldGetWithUserCriteriaStartAndAvailableEnd()
  {
    def myAvailCriteria = new AvailableCriteria(reportRange: new DateRange(dateEnd: new Date() + 2))

    def criteria = new ReportCriteria(myAvailCriteria, new UserCriteria(reportRange: new DateRange(dateStart: new Date() + 1)))

    assertEquals DateUtil.nullifyTime(new Date() + 1), criteria.reportRange.dateStart
    assertEquals DateUtil.maximizeTime(new Date() + 2), criteria.reportRange.dateEnd
  }

}
