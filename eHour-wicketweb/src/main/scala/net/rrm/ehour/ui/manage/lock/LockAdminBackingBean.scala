package net.rrm.ehour.ui.manage.lock

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import com.github.nscala_time.time.Imports._
import net.rrm.ehour.domain.TimesheetLock
import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl
import net.rrm.ehour.util.DateUtil
import org.joda.time.Days

class LockAdminBackingBean(val lock: TimesheetLock) extends AdminBackingBeanImpl[TimesheetLock] {
  override def getDomainObject: TimesheetLock = lock

  def getLock = lock

  def isNew = lock.getPK == null

  def updateName(formattingLocale: Locale) = {
    lock.setName(LockAdminBackingBean.determineName(lock.getDateStart, lock.getDateEnd, formattingLocale))
    this
  }

  override def isDeletable = !isNew
}

object LockAdminBackingBean {
  def determineName(startDate: Date, endDate: Date, formattingLocale: Locale): String = {
    def formatForMonth(date: Date): String = DateTimeFormat.forPattern("MMMM, yyyy").withLocale(formattingLocale).print(new DateTime(date))

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
