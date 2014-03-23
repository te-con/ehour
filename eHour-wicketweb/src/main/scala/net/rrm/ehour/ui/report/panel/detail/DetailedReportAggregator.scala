package net.rrm.ehour.ui.report.panel.detail

import java.{util => ju}
import net.rrm.ehour.report.reports.element.FlatReportElement
import org.joda.time.LocalDate


object DetailedReportAggregator {
  def aggregate(elements: List[FlatReportElement]): List[FlatReportElement] = {
    def sum(accumulator: Map[AggregateKey, Float], xs: List[FlatReportElement]): Map[AggregateKey, Float] = {
      if (xs.isEmpty) {
        accumulator
      } else {
        val element = xs.head
        val key = AggregateKey(element)

        val v = accumulator.getOrElse(key, 0f) + element.getTotalHours.floatValue()

        val newAccumulator: Map[AggregateKey, Float] = accumulator + (key -> v)
        sum(newAccumulator, xs.tail)
      }
    }

    val aggregated = sum(Map[AggregateKey, Float](), elements)

    (aggregated map {
      case (k, v) =>
        val clone = new FlatReportElement(k.baseElement)
        clone.setTotalHours(v)
        clone.setTotalTurnOver(v * clone.getRate.floatValue())
        clone.setComment("")
        clone
    }).toList
  }
}

case class AggregateKey(aggregatedOn: LocalDate, baseElement: FlatReportElement) {
  override def equals(other: Any) = other match {
    case that: AggregateKey => that.aggregatedOn == aggregatedOn && baseElement.getAssignmentId.equals(that.baseElement.getAssignmentId)
    case _ => false
  }
}

object AggregateKey {
  def apply(e: FlatReportElement): AggregateKey = {
    val date = e.getDayDate
    val thisMonth = new LocalDate(date.getTime).withDayOfMonth(1)

    AggregateKey(thisMonth, e)
  }
}
