package net.rrm.ehour.ui.common.chart

import net.rrm.ehour.AbstractSpec
import org.joda.time.{DateTimeConstants, LocalDate}
import com.google.gson.JsonElement

class DateTimeSerializerSpec extends AbstractSpec {
  "DateTime Serializer" should {
    "serialize localDate's to yyyy-mm-dd format" in {
      val element: JsonElement = new DateTimeSerializer().serialize(new LocalDate(2014, DateTimeConstants.MARCH, 14).toDateTimeAtStartOfDay, null, null)

      element.getAsString should equal("2014-03-14")
    }
  }
}
