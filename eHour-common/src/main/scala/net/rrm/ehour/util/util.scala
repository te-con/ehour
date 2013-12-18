package net.rrm.ehour

import java.{util => ju}
import scala.collection.convert.{WrapAsJava, WrapAsScala}
import com.google.common.collect.Lists

package object util {
  def toScala[T](xs: ju.List[T]): List[T] = WrapAsScala.asScalaBuffer(xs).toList

  def toScala[T](xs: ju.Set[T]): Set[T] = WrapAsScala.asScalaSet(xs).toSet

  def toScala[T](xs: ju.Queue[T]): Iterator[T] = WrapAsScala.asScalaIterator(xs.iterator())

  def toJava[T](xs: Seq[T]): ju.List[T] = if (xs != null) WrapAsJava.bufferAsJavaList(xs.toBuffer) else Lists.newArrayList()

  implicit def toSetJava[T](xs: Set[T]): ju.Set[T] = WrapAsJava.setAsJavaSet(xs)
}
