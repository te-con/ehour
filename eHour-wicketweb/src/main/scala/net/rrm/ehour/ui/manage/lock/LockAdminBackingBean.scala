package net.rrm.ehour.ui.manage.lock

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import com.github.nscala_time.time.Imports._
import net.rrm.ehour.domain.TimesheetLockDomain
import net.rrm.ehour.timesheet.dto.TimesheetLock
import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.wicket.AdminFormBean
import net.rrm.ehour.util.DateUtil
import org.joda.time.Days

case class LockAdminBackingBean(id: Option[Int], var name: String, var dateStart: DateTime, var dateEnd: DateTime)  {
  def isNew = id.isEmpty

  def updateName(formattingLocale: Locale) = {
    name = LockAdminBackingBean.determineName(dateStart, dateEnd, formattingLocale)
    this
  }

  def isDeletable = !isNew
}

object LockAdminBackingBean {
  def apply(locale: Locale): LockAdminBackingBean = {
    val start = new DateTime().withDayOfMonth(1)
    val end = new DateTime().withDayOfMonth(1).plusMonths(1).minusDays(1)

    val name = LockAdminBackingBean.determineName(start, end, locale)

    LockAdminBackingBean(id = None, name = name, dateStart = start, dateEnd = end)
  }

  def determineName(startDate: DateTime, endDate: DateTime, formattingLocale: Locale): String = {
    def formatForMonth(date: DateTime): String = DateTimeFormat.forPattern("MMMM, yyyy").withLocale(formattingLocale).print(date)

    if (startDate == null && endDate == null) {
      ""
    } else if (startDate == null) {
      formatForMonth(endDate)
    } else if (endDate == null) {
      formatForMonth(startDate)
    } else {
      val start = new DateTime(startDate)
      val end = new DateTime(endDate)

      val days = Days.daysBetween(start, end).getDays
      days / 7 match {
        case 0 | 1 => "Week %s" format DateTimeFormat.forPattern("w, yyyy").withLocale(formattingLocale).print(start)
        case 3 | 4 | 5 => formatForMonth(startDate)
        case 11 | 12 | 13 => "Q%d, %d" format((start.getMonthOfYear / 3) + 1, start.getYear)
        case _ =>
          val formatter = new SimpleDateFormat(DateUtil.getPatternForDateLocale(formattingLocale), formattingLocale)
          "%s - %s" format(formatter.format(startDate), formatter.format(endDate))
      }
    }
  }
}
