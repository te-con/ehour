import java.util.TimeZone

import org.joda.time._

val defaultZone = DateTimeZone.getDefault

DateTimeZone.getAvailableIDs

val x = TimeZone.getDefault

(0 to 23).toList.asInstanceOf[List[java.lang.Integer]]


val i = new Interval(new LocalDate(2014, 1,1).toDateTimeAtStartOfDay, new LocalDate(2014, 1, 5).toDateTimeAtStartOfDay)

import java.lang

import com.google.common.collect.Range
val r1 =  Range.open(new lang.Integer(2), new lang.Integer(5))
val r2 =  Range.open(new lang.Integer(1), new lang.Integer(3))
r1.intersection(r2)

val dateStart = new LocalDate(2014, 1, 5)
val dateEnd = new LocalDate(2014, 1, 10)

val weekRange = Range.closed(new lang.Long(dateStart.toDateTimeAtStartOfDay.getMillis), new lang.Long(dateEnd.toDateTimeAtStartOfDay.getMillis))

val asgStart = new LocalDate(2014, 1, 2)
val asgEnd = new LocalDate(2014, 1, 6)
val asgRange = Range.closed(new lang.Long(asgStart.toDateTimeAtStartOfDay.getMillis), new lang.Long(asgEnd.plusDays(1).toDateTimeAtStartOfDay.getMillis))

val asg2Start = new LocalDate(2014, 1, 7)
val asg2End = new LocalDate(2014, 1, 11)
val asg2Range = Range.closed(new lang.Long(asg2Start.toDateTimeAtStartOfDay.getMillis), new lang.Long(asg2End.toDateTimeAtStartOfDay.getMillis))

val intersectA = weekRange.intersection(asgRange)
val intersectB = weekRange.intersection(asg2Range)
val conn = intersectA.isConnected(intersectB)
val span = intersectA.span(intersectB)

span.encloses(weekRange)


//
import net.rrm.ehour.util.JodaDateUtil

def toWeekDays(start: LocalDate, end: LocalDate) = JodaDateUtil.enumerate(start, end).map(_.getDayOfWeek)

val weekDays = toWeekDays(dateStart, dateEnd)
val ds = List((asgStart, asgEnd), (asg2Start, asg2End))

val boundedDs = ds.map(t =>
  (if (t._1.isBefore(dateStart)) dateStart else t._1,
   if (t._2.isAfter(dateEnd)) dateEnd else t._2)
)

boundedDs.map(t => toWeekDays(t._1, t._2))






