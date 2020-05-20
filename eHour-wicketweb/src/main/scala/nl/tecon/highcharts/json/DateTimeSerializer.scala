package nl.tecon.highcharts.json

import org.joda.time.DateTime
import net.liftweb.json.JsonAST.JString
import net.liftweb.json._

class DateTimeSerializer extends Serializer[DateTime] {
  override def serialize(implicit format: Formats) = {
    case d: DateTime =>  JString("Date.UTC(%d,%d, %d)" format(d.getYear, d.getMonthOfYear - 1, d.getDayOfMonth))
  }

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), DateTime] = {
    throw new IllegalArgumentException("Not implemented")
  }
}
