package net.rrm.ehour.sort

import net.rrm.ehour.AbstractSpec
import net.rrm.ehour.report.criteria.Sort
import net.rrm.ehour.domain.{CustomerObjectMother, Project}
import java.util
import java.util.Collections

class ProjectComparatorSpec extends AbstractSpec {
  "Project Comparator" should {
    "compare on project name" in {
      val comparator = new ProjectComparator(Sort.NAME)

      val customer = CustomerObjectMother.createCustomer()

      val p1 = new Project(1, customer)
      p1.setName("BB")

      val p2 = new Project(2, customer)
      p2.setName("aa")

      comparator.compare(p1, p2) should equal(1)
    }

    "compare on project code" in {
      val comparator = new ProjectComparator(Sort.CODE)

      val customer = CustomerObjectMother.createCustomer()

      val p1 = new Project(1, customer)
      p1.setName("aa")
      p1.setProjectCode("bb")

      val p2 = new Project(2, customer)
      p2.setName("bb")
      p2.setProjectCode("aa")

      comparator.compare(p1, p2) should equal(1)
    }

    "compare on customer code first and then project code" in {
      val comparator = new ProjectComparator(Sort.PARENT_CODE_FIRST)

      val c1 = CustomerObjectMother.createCustomer()
      c1.setCode("A")

      val c2 = CustomerObjectMother.createCustomer()
      c2.setCode("B")

      val p1 = new Project(1, c2)
      p1.setProjectCode("bb")

      val p2 = new Project(2, c2)
      p2.setProjectCode("aa")

      val p3 = new Project(3, c1)
      p3.setProjectCode("c")

      val projects = util.Arrays.asList(p1, p2, p3)

      Collections.sort(projects, comparator)

      projects.get(0) should equal(p3)
      projects.get(1) should equal(p2)
      projects.get(2) should equal(p1)
    }
  }

}
