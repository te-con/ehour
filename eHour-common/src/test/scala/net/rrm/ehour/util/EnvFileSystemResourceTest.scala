package net.rrm.ehour.util

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite

@RunWith(classOf[JUnitRunner])
class EnvFileSystemResourceTest extends FunSuite {

  test("should parse environment") {
    System.getProperties.put("EHOUR_HOME", "ok")

    val resource = new EnvFileSystemResource("${EHOUR_HOME}${SEPARATOR}test")

    assert(resource.getFile.getPath == "ok${SEPARATOR}test")
  }

  test("should parse without environment") {
    val resource = new EnvFileSystemResource("ok${SEPARATOR}test")

    assert(resource.getFile.getPath == "ok${SEPARATOR}test")
  }
}
