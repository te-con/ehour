package net.rrm.ehour.ui.report.detailed

import java.util.Date
import java.{util => ju}

import net.rrm.ehour.report.reports.element.FlatReportElement
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.util.DateUtil
import org.apache.commons.lang.builder.HashCodeBuilder
import org.joda.time.LocalDate

import scala.collection.convert.{WrapAsJava, WrapAsScala}

sealed trait AggregateConverter {
  def toDate(date: Date): Date
  def toString(date: Date): String
}

class ByDay extends AggregateConverter {
  override def toDate(date: Date): Date = date
  override def toString(date: Date): String = new LocalDate(date.getTime).toString("yyyyMMdd")
}

class ByWeek extends AggregateConverter {
  override def toString(date: Date): String = if (date != null) new LocalDate(date.getTime).toString("yyyyww") else ""
  override def toDate(date: Date): Date = {
    implicit val weekStart = DateUtil.fromCalendarToJodaTimeDayInWeek(EhourWebSession.getEhourConfig.getFirstDayOfWeek)
    DateCalculator.toWeekStart(date).toDate
  }
}

class ByMonth extends AggregateConverter {
  override def toString(date: Date): String = if (date != null) new LocalDate(date.getTime).toString("yyyyMM") else ""
  override def toDate(date: Date): Date = DateCalculator.toMonthStart(date).toDate
}

class ByQuarter extends AggregateConverter {
  override def toString(date: Date): String = {
    if (date != null) {
      val localDate = new LocalDate(date.getTime)
      val month = localDate.getMonthOfYear
      val q = DateCalculator.inQuarter(month)
      val year = localDate.getYear

      s"$year$q"
    } else
      ""
  }
  override def toDate(date: Date): Date = DateCalculator.toQuarterStart(date).toDate
}

class ByYear extends AggregateConverter {
  override def toString(date: Date): String = if (date != null) new LocalDate(date.getTime).toString("yyyy") else ""
  override def toDate(date: Date): Date = DateCalculator.toYearStart(date).toDate
}

object DetailedReportAggregator {
  def aggregate(elements: ju.List[FlatReportElement], converter: AggregateConverter): ju  .List[FlatReportElement] = {
    WrapAsJava.bufferAsJavaList(aggregate(WrapAsScala.asScalaBuffer(elements).toList, converter).toBuffer)
  }

  def aggregate(elements: List[FlatReportElement], converter: AggregateConverter): List[FlatReportElement] = {
    def sum(accumulator: Map[AggregateKey, Float], xs: List[FlatReportElement]): Map[AggregateKey, Float] = {
      if (xs.isEmpty) {
        accumulator
      } else {
        val element = xs.head
        val key = AggregateKey(element, converter)

        val v = accumulator.getOrElse(key, 0f) + element.getTotalHours.floatValue()

        sum(accumulator + (key -> v), xs.tail)
      }
    }

    val aggregated = sum(Map[AggregateKey, Float](), elements)

    (aggregated map {
      case (k, v) =>
        val clone = new FlatReportElement(k.baseElement)

        if (k.baseElement.getDayDate != null)
          clone.setDayDate(converter.toDate(k.baseElement.getDayDate))

        clone.setTotalHours(v)
        clone.setTotalTurnOver(v * (if (clone.getRate != null) clone.getRate.floatValue() else 0))
        clone.setComment("")
        clone
    }).toList
  }
}

private case class AggregateKey(aggregatedOn: String, baseElement: FlatReportElement) {
  override def hashCode() = new HashCodeBuilder().append(aggregatedOn).append(baseElement.getAssignmentId).toHashCode

  override def equals(other: Any) = other match {
    case that: AggregateKey =>
      val id = baseElement.getAssignmentId
      val otherId = that.baseElement.getAssignmentId
      that.aggregatedOn.equals(aggregatedOn) && id.equals(otherId)
    case _ => false
  }
}

private object AggregateKey {
  def apply(e: FlatReportElement, converter: AggregateConverter): AggregateKey = {
    val date = e.getDayDate

    AggregateKey(converter.toString(date), e)
  }
}
