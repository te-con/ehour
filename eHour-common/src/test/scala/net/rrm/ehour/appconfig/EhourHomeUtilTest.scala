package net.rrm.ehour.appconfig

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EhourHomeUtilTest extends FunSuite {
  test("should get configuration dir") {
    val dir = EhourHomeUtil.getConfDir("a")

    assert(dir == "a/conf/")
  }
}
