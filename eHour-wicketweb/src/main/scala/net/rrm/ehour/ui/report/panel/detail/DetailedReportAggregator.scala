package net.rrm.ehour.ui.report.panel.detail

import java.{util => ju}
import net.rrm.ehour.report.reports.element.FlatReportElement
import org.joda.time.LocalDate
import java.util.Date
import scala.collection.convert.{WrapAsJava, WrapAsScala}
import org.apache.commons.lang.builder.HashCodeBuilder


object DetailedReportAggregator {
  val ByMonth = (date: Date) => new LocalDate(date.getTime).toString("yyyyMM")
  val ByWeek = (date: Date) => new LocalDate(date.getTime).toString("yyyyww")
  val ByQuarter = (date: Date) => {
    val localDate = new LocalDate(date.getTime)
    val quarter = (localDate.getMonthOfYear - 1) / 3
    val year = localDate.getYear

    s"$year$quarter"
  }
  val ByYear= (date: Date) => new LocalDate(date.getTime).toString("yyyy")

  def aggregate(elements: ju.List[FlatReportElement], f: (Date) => String): ju  .List[FlatReportElement] = {
    WrapAsJava.bufferAsJavaList(aggregate(WrapAsScala.asScalaBuffer(elements).toList, f).toBuffer)
  }

  def aggregate(elements: List[FlatReportElement], f: (Date) => String): List[FlatReportElement] = {
    def sum(accumulator: Map[AggregateKey, Float], xs: List[FlatReportElement]): Map[AggregateKey, Float] = {
      if (xs.isEmpty) {
        accumulator
      } else {
        val element = xs.head
        val key = AggregateKey(element, f)

        val v = accumulator.getOrElse(key, 0f) + element.getTotalHours.floatValue()

        sum(accumulator + (key -> v), xs.tail)
      }
    }

    val aggregated = sum(Map[AggregateKey, Float](), elements)

    (aggregated map {
      case (k, v) =>
        val clone = new FlatReportElement(k.baseElement)
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
  def apply(e: FlatReportElement, f: (Date) => String): AggregateKey = {
    val date = e.getDayDate

    AggregateKey(f(date), e)
  }
}
