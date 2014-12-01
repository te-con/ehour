package net.rrm.ehour.util

import java.util.Calendar

import org.joda.time.{DateTime, DateTimeConstants, Interval, LocalDate}

object JodaDateUtil {
  val calendarToJodaWeekDays = Map(Calendar.SUNDAY -> DateTimeConstants.SUNDAY,
    Calendar.MONDAY -> DateTimeConstants.MONDAY,
    Calendar.TUESDAY -> DateTimeConstants.TUESDAY,
    Calendar.WEDNESDAY -> DateTimeConstants.WEDNESDAY,
    Calendar.THURSDAY -> DateTimeConstants.THURSDAY,
    Calendar.FRIDAY -> DateTimeConstants.FRIDAY,
    Calendar.SATURDAY -> DateTimeConstants.SATURDAY)

  /**
   * get interval for week the calendar is in
   * @param cal should fall in the interval
   * @param firstDayOfWeek (java.util.Calendar) first day of week
   * @return interval containing @cal
   */
  def intervalForWeek(cal: Calendar, firstDayOfWeek: Integer): Interval = {
    val jodaWeekDay = JodaDateUtil.calendarToJodaWeekDays(firstDayOfWeek)

    val s = new LocalDate(cal).toDateTimeAtStartOfDay
    val start: DateTime = s.withDayOfWeek(jodaWeekDay)

    val adjustedStart = if (start.isAfter(s)) start.minusWeeks(1) else start

    val end: DateTime = adjustedStart.plusWeeks(1).minusMillis(1)
    new Interval(adjustedStart, end)
  }

  def findWorkDays(interval: Interval): List[LocalDate] = {
    val dates = enumerate(interval.getStart.toLocalDate, interval.getEnd.toLocalDate)

    dates.filterNot(isWeekend)
  }

  def isWeekend(p: LocalDate): Boolean = p.getDayOfWeek == DateTimeConstants.SATURDAY || p.getDayOfWeek == DateTimeConstants.SUNDAY

  // returns an enumeration of all dates between start and end, including start and end
  def enumerate(start: LocalDate, end: LocalDate): List[LocalDate] = {

    def aggregate(xs: List[LocalDate], currentDate: LocalDate): List[LocalDate] = {
      if (currentDate.isAfter(end)) {
        xs
      } else {
        aggregate(currentDate :: xs, currentDate.plusDays(1))
      }
    }

    aggregate(Nil, start).reverse
  }
}
