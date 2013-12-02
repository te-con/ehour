package net.rrm.ehour.ui.admin.project

import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl
import net.rrm.ehour.domain.{ProjectAssignment, Project}
import scala.collection.mutable.ListBuffer
import java.{util => ju}
import scala.collection.convert.WrapAsJava

class ProjectAdminBackingBean(private val project: Project) extends AdminBackingBeanImpl {
  val getProject: Project = project
  override def getDomainObject: Project = getProject

  private var assignExistingUsersToDefaultProjects: Boolean = false

  def isAssignExistingUsersToDefaultProjects: Boolean = assignExistingUsersToDefaultProjects

  def setAssignExistingUsersToDefaultProjects(assignExistingUsersToDefaultProjects: Boolean) {
    this.assignExistingUsersToDefaultProjects = assignExistingUsersToDefaultProjects
  }

  private var assignmentsQueue: ListBuffer[ProjectAssignment] = ListBuffer()

  // assignments queue contains the modifications
  def getAssignmentsQueue: ju.List[ProjectAssignment] = WrapAsJava.bufferAsJavaList(assignmentsQueue)


  private def removeAssignmentFromQueue(assignment: ProjectAssignment): ListBuffer[ProjectAssignment] = assignmentsQueue.filterNot(p => equalsAssignment(assignment, p))
  private def equalsAssignment(needle: ProjectAssignment, fromStack: ProjectAssignment): Boolean = {
    if (needle.getPK != null)
      fromStack.getPK == needle.getPK
    else
      needle.getUser == fromStack.getUser && needle.getProject == fromStack.getProject
  }

  def addAssignmentToQueue(assignment: ProjectAssignment) {
    val cleanQueue = removeAssignmentFromQueue(assignment)
    assignmentsQueue = cleanQueue
    assignmentsQueue += assignment
  }

  private var assignmentRemovalQueue:ListBuffer[ProjectAssignment] = ListBuffer()
  def getRemovalQueue: ju.List[ProjectAssignment] = WrapAsJava.bufferAsJavaList(assignmentRemovalQueue.filter(_.getPK != null))

  def deleteAssignment(assignment: ProjectAssignment) {
    assignmentsQueue = removeAssignmentFromQueue(assignment)

    assignmentRemovalQueue = assignmentRemovalQueue.filterNot(p => equalsAssignment(assignment, p))

    assignmentRemovalQueue += assignment
  }

  def isDeleted(assignment: ProjectAssignment) = assignmentRemovalQueue.exists(equalsAssignment(assignment, _))

  // returns the whole sanitized list of original assignments plus modifications
  def mergeOriginalAssignmentsWithQueue(assignments: List[ProjectAssignment]):List[ProjectAssignment] = {
    val isNewPredicate = (p:ProjectAssignment) => p.getPK == null
    val newAssignments = assignmentsQueue.filter(isNewPredicate)
    val updatedAssignments = assignmentsQueue.filterNot(isNewPredicate)
    val originalAssignmentsMinusModified = assignments.filterNot(p => updatedAssignments.exists(_.getPK.equals(p.getPK)))
    def filterRemovals(ps:List[ProjectAssignment]) = ps.filterNot(p => assignmentRemovalQueue.exists(r => equalsAssignment(p, r)))
    
    filterRemovals(originalAssignmentsMinusModified ++ newAssignments ++ updatedAssignments)
  }
}

