package net.rrm.ehour.ui.common.chart

import com.google.gson._
import org.joda.time.DateTime
import java.lang.reflect.Type
import org.joda.time.format.DateTimeFormat

object GsonSerializer {
  val formatter = DateTimeFormat.forPattern("yyyy-MM-dd")

  def create =
    new GsonBuilder()
      .registerTypeAdapter(classOf[DateTime], new DateTimeSerializer)
      .create()
}

class DateTimeSerializer extends JsonSerializer[DateTime] {
  override def serialize(d: DateTime, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = new JsonPrimitive(GsonSerializer.formatter.print(d))
}
