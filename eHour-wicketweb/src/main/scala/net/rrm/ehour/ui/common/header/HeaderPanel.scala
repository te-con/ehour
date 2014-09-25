package net.rrm.ehour.ui.common.header

import java.util

import com.richemont.windchill.{WindChillService, WindChillUpdateService}
import net.rrm.ehour.ui.common.panel.AbstractBasePanel
import net.rrm.ehour.ui.common.session.EhourWebSession
import net.rrm.ehour.ui.common.util.AuthUtil
import net.rrm.ehour.user.service.UserService
import org.apache.log4j.Logger
import org.apache.wicket.Component
import org.apache.wicket.ajax.{AbstractAjaxTimerBehavior, AjaxRequestTarget}
import org.apache.wicket.behavior.Behavior
import org.apache.wicket.markup.html.link.BookmarkablePageLink
import org.apache.wicket.markup.html.panel.Fragment
import org.apache.wicket.spring.injection.annot.SpringBean
import org.apache.wicket.util.time.Duration

class HeaderPanel(id: String, showSyncLink: Boolean = false) extends AbstractBasePanel(id) {
  @SpringBean
  protected var authUtil: AuthUtil = _
  @SpringBean protected var windChillService: WindChillService = _
  @SpringBean protected var chillUpdateService: WindChillUpdateService = _
  @SpringBean protected var userService: UserService = _
//  @SpringBean private var jiraService: JiraService = _

  private val LOGGER: Logger = Logger.getLogger("ext.service.JiraServiceImpl")

  override def onInitialize(): Unit = {
    super.onInitialize()

    val homepage = authUtil.getHomepageForRole(EhourWebSession.getSession.getRoles)
    add(new BookmarkablePageLink("homeLink", homepage.homePage, homepage.parameters))
    add(createNav("nav"))
    add(new LoggedInAsPanel("loggedInAs"))
    addSyncLink(showSyncLink);
  }

  private def createNav(id: String) = new TreeBasedMenu(id, MenuDefinition.createMenuDefinition)

  /**
   * JIRA - eHOUR - PJL synchronization
   * added by LLI for Richemont
   *
   * @param visible
   */
  private def addSyncLink(visible: Boolean) {
    val fragment: Fragment = new Fragment("syncFailContainer", "syncFail", this)
    fragment.setOutputMarkupId(true)
    fragment.setOutputMarkupPlaceholderTag(true)
    fragment.setVisible(false)
    add(fragment)

//    val link: AjaxLink[Void] = new GuardedAjaxLink[Void](("syncLink")) {
//      def onClick(target: AjaxRequestTarget) {
//        val user: User = EhourWebSession.getUser
//        var isJiraSync: Boolean = false
//        var isWindSync: Boolean = false
//        LOGGER.info("\n\n")
//        LOGGER.info("****************** STEP# 1 : GET ALL ACTIVITIES from HeaderPanel ******************")
//        val allAssignedActivitiesByCode: Map[String, Activity] = windChillService.getAllAssignedActivitiesByCode(user)
//        LOGGER.info("\n\n")
//        LOGGER.info("****************** START JIRA SYNC (get Jira issues) ****************************")
//        val isJiraUser: Boolean = userService.isLdapUserMemberOf(user.getUsername, JiraConst.GET_JIRA_ISSUES_FOR_USER_MEMBER_OF)
//        LOGGER.info("User in " + JiraConst.GET_JIRA_ISSUES_FOR_USER_MEMBER_OF + " createJiraIssuesForUser() for user " + user.getUsername + " = " + isJiraUser)
//        var activitiesMasteredByJira: Map[JiraIssue, Activity] = null
//        try {
//          if (isJiraUser) {
//            activitiesMasteredByJira = jiraService.createJiraIssuesForUser(allAssignedActivitiesByCode, user.getUsername)
//            if (activitiesMasteredByJira == null) {
//              isJiraSync = false
//            }
//            else {
//              isJiraSync = true
//              LOGGER.info("\n\n")
//              LOGGER.info("****************** Identify missing activity in PJL from JIRA->EHOUR ********************")
//              val activitiesPjlToBeCreated: JsonArray = jiraService.identifyMissingPjlActivity(activitiesMasteredByJira)
//              LOGGER.debug("JsonArray for activitiesPjlToBeCreated:")
//              LOGGER.debug(activitiesPjlToBeCreated)
//              val request: HttpServletRequest = getWebRequest.getHttpServletRequest
//              request.getSession.setAttribute("MissingPjlActivity", activitiesPjlToBeCreated)
//            }
//          }
//          else {
//            LOGGER.info("User do not exist in " + JiraConst.GET_JIRA_ISSUES_FOR_USER_MEMBER_OF + " : skip createJiraIssuesForUser() for user " + user.getUsername)
//            isJiraSync = true
//          }
//        }
//        catch {
//          case e: Exception => {
//            e.printStackTrace
//            isJiraSync = false
//          }
//        }
//        LOGGER.info("\n\n")
//        LOGGER.info("****************** START WINDCHILL SYNC (get PJL activities) ************************")
//        if (!isEnabled) {
//          LOGGER.info("WARNING: Windchill sync is disabled")
//        }
//        else {
//          try {
//            isWindSync = windChillService.updateDataForUser(allAssignedActivitiesByCode, user.getUsername)
//          }
//          catch {
//            case e: Exception => {
//              e.printStackTrace
//            }
//          }
//        }
//        allAssignedActivitiesByCode.clear
//        if (!isWindSync || !isJiraSync) {
//          val container: Component = getSyncFailContainer
//          container.setVisible(true)
//          removeHiderBehavior(container)
//          container.add(new HeaderPanel#SyncFailureMessageHider)
//          target.addComponent(container)
//        }
//        else {
//          EventPublisher.publishAjaxEventToPageChildren(HeaderPanel.this, new AjaxEvent(WindchillSyncEvent.WINDCHILL_SYNCED, target))
//        }
//        allAssignedActivitiesByCode.clear
//      }
//
//      protected override def getAjaxCallDecorator: IAjaxCallDecorator = {
//        return new LoadingSpinnerDecorator
//      }
//    }
//    link.setVisible(visible)
//    add(link)
  }

  protected def removeHiderBehavior(container: Component) {
    val behaviors: util.List[_ <: Behavior] = container.getBehaviors
    import scala.collection.JavaConversions._
    for (behavior <- behaviors) {
      if (behavior.isInstanceOf[HeaderPanel#SyncFailureMessageHider]) {
        container.remove(behavior)
      }
    }
  }

  def getSyncFailContainer: Component = HeaderPanel.this.get("syncFailContainer")

  class SyncFailureMessageHider extends AbstractAjaxTimerBehavior(Duration.seconds(15)) {
    protected def onTimer(target: AjaxRequestTarget) {
      stop(target)
      val container = getSyncFailContainer
      container.setVisible(false)
      target.add(container)
    }
  }

}