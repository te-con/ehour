import org.joda.time._


val dateStart = new LocalDate(2014, 8, 9)
val dateEnd = new LocalDate(2014, 8, 15)

val asgStart = new LocalDate(2014, 8, 9)
val asgEnd = new LocalDate(2014, 8, 10)

val asg2Start = new LocalDate(2014, 8, 11)
val asg2End = new LocalDate(2014, 8, 15)

import net.rrm.ehour.util.JodaDateUtil

def toWeekDays(t: (LocalDate, LocalDate)) = JodaDateUtil.enumerate(t._1, t._2).map(_.getDayOfWeek)

val weekDays = toWeekDays(dateStart, dateEnd)
val ds = List((asgStart, asgEnd), (asg2Start, asg2End))

val boundedDs = ds.map(t =>
  (if (t._1.isBefore(dateStart)) dateStart else t._1,
    if (t._2.isAfter(dateEnd)) dateEnd else t._2)
)

val joinedWeekDays = boundedDs.flatMap(toWeekDays)
joinedWeekDays.foldLeft(0)(_ + _) == weekDays.foldLeft(0)(_ + _)





