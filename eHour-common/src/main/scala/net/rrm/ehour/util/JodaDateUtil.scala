package net.rrm.ehour.util

import org.joda.time.LocalDate

object JodaDateUtil {
  // returns a list of all dates between start and end, including start and end
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
