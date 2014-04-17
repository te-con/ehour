package net.rrm.ehour.ui.report.panel.detail

import java.{util => ju}
import net.rrm.ehour.report.reports.element.FlatReportElement
import org.joda.time.LocalDate
import java.util.Date
import scala.collection.convert.{WrapAsJava, WrapAsScala}
import org.apache.commons.lang.builder.HashCodeBuilder


trait AggregateConverter {
  def toDate(date: Date): Date
  def toString(date: Date): String
}

class ByWeek extends AggregateConverter {
  override def toString(date: Date): String = new LocalDate(date.getTime).toString("yyyyww")
  override def toDate(date: Date): Date = new LocalDate(date.getTime).withDayOfWeek(1).toDate
}

class ByMonth extends AggregateConverter {
  override def toString(date: Date): String = new LocalDate(date.getTime).toString("yyyyMM")
  override def toDate(date: Date): Date = new LocalDate(date.getTime).withDayOfMonth(1).toDate
}

class ByQuarter extends AggregateConverter {
  override def toString(date: Date): String = {
    val localDate = new LocalDate(date.getTime)
    val quarter = (localDate.getMonthOfYear - 1) / 3
    val year = localDate.getYear

    s"$year$quarter"
  }
  override def toDate(date: Date): Date = {
    val localDate = new LocalDate(date.getTime)
    val firstMonth = localDate.getMonthOfYear / 3
    new LocalDate(localDate.getYear, firstMonth, 1).toDate
  }
}

class ByYear extends AggregateConverter {
  override def toString(date: Date): String = new LocalDate(date.getTime).toString("yyyy")
  override def toDate(date: Date): Date = new LocalDate(date.getTime).withMonthOfYear(1).withDayOfMonth(1).toDate
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
        clone.setDayDate(converter.toDate(k.baseElement.getDayDate))
        clone.setTotalHours(v)
        clone.setTotalTurnOver(v * (if (clone.getRate != null) clone.getRate.floatValue() else 0))
        clone.setComment("")
        clone
    }).toList
  }
}

private case class AggregateKey(aggregatedOn: String, baseElement: FlatReportElement) {
  override def hashCode() = new HashCodeBuilder().append(aggregatedOn, baseElement.getAssignmentId).toHashCode

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
