package nl.tecon.highcharts.json

import net.liftweb.json._
import nl.tecon.highcharts.config.FloatValue

class NumericValueSerializer extends Serializer[FloatValue] {
  override def serialize(implicit format: Formats) = {
   case d: FloatValue =>  JArray(List(JInt(d.key.intValue()), JDouble(d.value.doubleValue())))
  }

 def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), FloatValue] = {
    throw new IllegalArgumentException("Not implemented")
  }
}
