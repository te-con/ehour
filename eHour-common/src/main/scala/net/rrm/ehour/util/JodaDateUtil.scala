package net.rrm.ehour.util

import org.joda.time.{DateTimeConstants, Interval, LocalDate}

object JodaDateUtil {
  def findWorkDays(interval: Interval):List[LocalDate] = {
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
