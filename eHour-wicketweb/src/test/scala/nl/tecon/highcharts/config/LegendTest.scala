package nl.tecon.highcharts.config


import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.matchers.ShouldMatchers
import net.liftweb.json.Serialization.write
import net.liftweb.json.ext._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class LegendTest extends FlatSpec with Matchers {

  "Legend" should
    "be serialized" in {
    import Conversions.valueToOption
    val legend = new Legend(align = Alignment.center)

    implicit val formats = net.liftweb.json.DefaultFormats + new EnumNameSerializer(Alignment)
    val jsonLegend = write(legend)

    jsonLegend should be( """{"align":"center"}""")
  }
}