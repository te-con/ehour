package nl.tecon.highcharts.config


import scala.Predef._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, FlatSpec}
import org.joda.time.DateTime


@RunWith(classOf[JUnitRunner])
class SparseDateSeriesTest extends FlatSpec with Matchers {
  val now = new DateTime(2012, 10, 27, 14, 17, 0, 0)

  behavior of "SparseDataSeries"

  it should "should nullify not provided data" in {
    val series = SparseDateSeries(data = List(DateFloatValue(now, 1), DateFloatValue(now.plusDays(2), 2), DateFloatValue(now.plusDays(4), 3)))

    series.preProcess().data should be (List(1, 0, 2, 0, 3))
  }

  it should "should sum non-unique dates" in {
    val series = SparseDateSeries(data = List(DateFloatValue(now, 1), DateFloatValue(now, 2), DateFloatValue(now.plusDays(1), 4)))

    series.preProcess().data should be (List(3, 4))
  }

}
