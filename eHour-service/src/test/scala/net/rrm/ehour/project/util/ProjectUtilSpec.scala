package net.rrm.ehour.project.util

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{WordSpec, Matchers}
import net.rrm.ehour.domain.{ProjectObjectMother, UserObjectMother}
import scala.collection.convert.WrapAsJava

@RunWith(classOf[JUnitRunner])
class ProjectUtilSpec extends WordSpec with Matchers {
  "Project Util" must {
    "find projects with PM" in {
      val user = UserObjectMother.createUser()

      val projectA = ProjectObjectMother.createProject(1)
      projectA.setProjectManager(user)

      val projectB = ProjectObjectMother.createProject(2)

      val filtered = ProjectUtil.filterProjectsOnPm(user, WrapAsJava.asJavaCollection(Seq(projectA, projectB)))

      filtered should have size 1
      filtered.get(0).getProjectId should be(projectA.getProjectId)
    }
  }

}
