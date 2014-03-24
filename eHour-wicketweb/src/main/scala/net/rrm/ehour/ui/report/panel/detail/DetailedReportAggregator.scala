package net.rrm.ehour.ui.report.panel.detail

import net.rrm.ehour.report.reports.element.FlatReportElement
import org.joda.time.LocalDate
import java.util.Date


object DetailedReportAggregator {
  val ByMonth = (date: Date) => new LocalDate(date.getTime).withDayOfMonth(1).toString("yyyyMM")
  val ByWeek = (date: Date) => new LocalDate(date.getTime).toString("yyyyww")
  val ByQuarter = (date: Date) => {
    val localDate = new LocalDate(date.getTime)
    val quarter = (localDate.getMonthOfYear - 1) / 3
    val year = localDate.getYear

    s"$year$quarter"
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
        clone.setTotalTurnOver(v * clone.getRate.floatValue())
        clone.setComment("")
        clone
    }).toList
  }
}

private case class AggregateKey(aggregatedOn: String, baseElement: FlatReportElement) {
  override def equals(other: Any) = other match {
    case that: AggregateKey => that.aggregatedOn == aggregatedOn && baseElement.getAssignmentId.equals(that.baseElement.getAssignmentId)
    case _ => false
  }
}

private object AggregateKey {
  def apply(e: FlatReportElement, f: (Date) => String): AggregateKey = {
    val date = e.getDayDate

    AggregateKey(f(date), e)
  }
}
