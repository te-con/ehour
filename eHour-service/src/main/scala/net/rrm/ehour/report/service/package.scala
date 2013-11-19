package net.rrm.ehour.report

import java.{util => ju}
import scala.collection.convert.{WrapAsJava, WrapAsScala}

package object service {
  def toScala[T](xs: ju.List[T]): List[T] = WrapAsScala.asScalaBuffer(xs).toList

  def toScala[T](xs: ju.Set[T]): Set[T] = WrapAsScala.asScalaSet(xs).toSet

  def toJava[T](xs: List[T]): ju.List[T] = WrapAsJava.bufferAsJavaList(xs.toBuffer)

  implicit def toSetJava[T](xs: Set[T]): ju.Set[T] = WrapAsJava.setAsJavaSet(xs)
}
