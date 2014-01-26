package net.rrm.ehour.ui.financial.lock

import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl
import net.rrm.ehour.domain.TimesheetLock
import java.util.{Locale, Date}
import org.joda.time.Days
import com.github.nscala_time.time.Imports._
import java.text.SimpleDateFormat
import net.rrm.ehour.util.DateUtil

class LockAdminBackingBean(val lock: TimesheetLock) extends AdminBackingBeanImpl[TimesheetLock] {
  override def getDomainObject: TimesheetLock = lock

  def getLock = lock

  def isNew = lock.getPK == null

  def updateName(formattingLocale: Locale) = {
    lock.setName(LockAdminBackingBean.determineName(lock.getDateStart, lock.getDateEnd, formattingLocale))
    this
  }
}

object LockAdminBackingBean {
  def determineName(startDate: Date, endDate: Date, formattingLocale: Locale): String = {
    val start = new DateTime(startDate)
    val end = new DateTime(endDate)

    val days = Days.daysBetween(start, end).getDays
    days / 7 match {
      case 0 | 1 => "Week %s" format DateTimeFormat.forPattern("w, yyyy").withLocale(formattingLocale).print(start)
      case 3 | 4 | 5 => DateTimeFormat.forPattern("MMMM, yyyy").withLocale(formattingLocale).print(start)
      case 11 | 12 | 13 => "Quarter %d, %d" format((start.getMonthOfYear / 3) + 1, start.getYear)
      case _ =>
        val formatter = new SimpleDateFormat(DateUtil.getPatternForDateLocale(formattingLocale), formattingLocale)
        "%s - %s" format(formatter.format(startDate), formatter.format(endDate))
    }
  }
}
