package net.rrm.ehour

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}
import org.scalatest.mock.MockitoSugar

@RunWith(classOf[JUnitRunner])
abstract class AbstractSpec extends WordSpec with Matchers with MockitoSugar with BeforeAndAfterEach