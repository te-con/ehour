package nl.tecon.highcharts.json

import net.liftweb.json._
import nl.tecon.highcharts.config._


class JavascriptFunctionSerializer extends Serializer[JavascriptFunction] {
  override def serialize(implicit format: Formats) = {
    case d: JavascriptFunction =>  JString("${JSF}$"  + d.function + "${/JSF}$")
  }

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), JavascriptFunction] = {
    throw new IllegalArgumentException("Not implemented")
  }
}
