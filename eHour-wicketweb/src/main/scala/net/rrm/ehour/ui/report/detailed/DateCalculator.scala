package net.rrm.ehour.ui.report.detailed

import java.util.Date

import org.joda.time.LocalDate

object DateCalculator {
  def toWeekStart(date: Date)(implicit weekStart: Int) = {
    val original = new LocalDate(date.getTime)
    val shifted = original.withDayOfWeek(weekStart)

    if (shifted.isAfter(original))
      shifted.plusDays(-7)
    else
      shifted
  }

  def toMonthStart(date: Date) = new LocalDate(date.getTime).withDayOfMonth(1)

  def inQuarter(month: Int) = month / 3 + (if ((month % 3) > 0) 1 else 0)
  def toQuarterStart(date: Date) = {
    val localDate = new LocalDate(date.getTime)
    val q = inQuarter(localDate.getMonthOfYear)
    new LocalDate(localDate.getYear, ((q - 1) * 3) + 1, 1)
  }
  def toYearStart(date: Date) = new LocalDate(date.getTime).withMonthOfYear(1).withDayOfMonth(1)
}
