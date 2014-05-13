package net.rrm.ehour.ui.common.chart

import org.joda.time.DateTime
import collection.Seq

import scala.language.implicitConversions


case class ValuePoint[V](givenValue: V)

sealed class KeyValuePoint[K, V](val givenKey: K, val givenValue: V)

case class FloatValue(key: Float, value: Float) extends KeyValuePoint[Float, Float](key, value)

case class DateFloatValue(date: DateTime, value: Float) extends KeyValuePoint[DateTime, Float](date, value) {
  def <(other: DateFloatValue) = date isBefore other.date
}

sealed abstract class AbstractSeries[T] {
  def preProcess(): AbstractSeries[_] = this
}

case class Series[T](name: String = "",
                     data: Iterable[T],
                     yAxis: Option[Int] = None) extends AbstractSeries[T]

case class SparseDateSeries(name: String = "",
                            data: Seq[DateFloatValue],
                            dateStart: DateTime,
                            dateEnd: DateTime,
                            yAxis: Option[Int] = None,
                            dateIncrease: DateTime => DateTime = _.plusDays(1)) extends AbstractSeries[DateFloatValue] {
  override def preProcess(): Series[Float] = {
    val groupedByDate = data groupBy (_.date)

    val dateTuplesWithSummedValues = groupedByDate.keys.toSeq sortWith (_ isBefore _) map (k =>
      (k, groupedByDate.getOrElse(k, Seq()).foldLeft(0f)(_ + _.value))
      )

    val dateMappedValues = dateTuplesWithSummedValues.toMap

    def padSeriesData(date: DateTime, paddedSeries: List[Float] = List()): Seq[Float] = {
      if (date isAfter dateEnd) {
        paddedSeries.reverse
      } else {
        padSeriesData(dateIncrease(date), dateMappedValues.getOrElse(date, 0f) :: paddedSeries)
      }
    }

    val paddedSeriesData = padSeriesData(dateStart)

    Series[Float](name, paddedSeriesData, yAxis)
  }
}
