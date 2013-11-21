package net.rrm.ehour.domain

import org.scalatest.{Matchers, BeforeAndAfter, FunSuite}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.collection.mutable
import java.util

@RunWith(classOf[JUnitRunner])
class ProjectAssignmentTest extends FunSuite with Matchers with BeforeAndAfter {

  val customersCache = new mutable.HashMap[String,  Customer]
  val projectCache = new mutable.HashMap[String, Project]
  var user: User = _
  before {
    customersCache.clear()
    projectCache.clear()
    user = UserObjectMother.createUser()
  }
  
  test("it should sort by Customer name") {
    val unsortedAssignments =
      CreateProjectAssignment("Cust_C",  "PRJX", "Project E") ::
      CreateProjectAssignment("Cust_B",  "PRJY", "Project D") ::
      CreateProjectAssignment("Cust_A",  "PRJZ", "Project F") ::
      Nil

    unsortedAssignments.size should be(3)
    unsortedAssignments(0).getProject.getCustomer.getName should  be("Cust_C")
    unsortedAssignments(1).getProject.getCustomer.getName should  be("Cust_B")

    val sortedArray = sortAssignments(unsortedAssignments)
    sortedArray.size should be(3)
    sortedArray(0) should be(unsortedAssignments(2))
    sortedArray(1) should be(unsortedAssignments(1))
    sortedArray(2) should be(unsortedAssignments(0))
  }

  test("it should sort by Project Code after the Customer name") {
    val unsortedAssignments =
      CreateProjectAssignment("Cust_X",  "PRJ_C", "Project E") ::
      CreateProjectAssignment("Cust_X",  "PRJ_B", "Project D") ::
      CreateProjectAssignment("Cust_X",  "PRJ_A", "Project F") ::
      Nil

    val sortedArray = sortAssignments(unsortedAssignments)
    sortedArray.size should be(3)
    sortedArray(0) should be(unsortedAssignments(2))
    sortedArray(1) should be(unsortedAssignments(1))
    sortedArray(2) should be(unsortedAssignments(0))
  }

  test("it should sort by Project Name after the Customer name and Project Code") {
    val unsortedAssignments =
      CreateProjectAssignment("Cust_X",  "PRJ_Y", "Project C") ::
      CreateProjectAssignment("Cust_X",  "PRJ_Y", "Project B") ::
      CreateProjectAssignment("Cust_X",  "PRJ_Y", "Project A") ::
      Nil

    val sortedArray = sortAssignments(unsortedAssignments)
    sortedArray(0) should be(unsortedAssignments(2))
    sortedArray(1) should be(unsortedAssignments(1))
    sortedArray(2) should be(unsortedAssignments(0))
  }
  
  
  private
  def CreateProjectAssignment(customerName: String,
                              projectCode: String,
                              projectName: String) : ProjectAssignment = {

    if(!customersCache.exists(_._1 == customerName)) {
      val tmp = CustomerObjectMother.createCustomer(customersCache.size+1)
      tmp.setName(customerName)
      customersCache += customerName -> tmp
    }
    val customer = customersCache(customerName)

    if(!projectCache.exists(_._1 == projectName)) {
      val tmp = ProjectObjectMother.createProject(projectCache.size+1, customer)
      tmp.setProjectCode(projectCode)
      tmp.setName(projectName)
      projectCache += projectName -> tmp
    }
    val project = projectCache(projectName)

    ProjectAssignmentObjectMother.createProjectAssignment(user, project)
  }

  def sortAssignments(unsorted: List[ProjectAssignment]) = {
    val sorted = new util.TreeSet[ProjectAssignment]
    unsorted.foreach(sorted.add)
    sorted.toArray
  }
}
