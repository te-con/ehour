package nl.tecon.highcharts.config

import scala.language.implicitConversions

object Conversions {
  implicit def valueToOption[T](v: T): Option[T] = Option.apply(v)
}