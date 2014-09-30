package net.rrm.ehour.init

import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import net.rrm.ehour.config.service.ConfigurationService
import org.apache.log4j.Logger
import net.rrm.ehour.persistence.user.dao.UserDao
import scalaj.collection.Imports._
import net.rrm.ehour.user.service.UserService
import net.rrm.ehour.config.EhourConfig

@Service
class LdapSynchronizer {

  @Autowired
  var eHourConfig: EhourConfig = _

  @Autowired
  var configurationService: ConfigurationService = _

  @Autowired
  var userService: UserService = _

  @Autowired
  var userDao: UserDao = _

  val Log = Logger.getLogger(getClass)

  @PostConstruct
  def ldapSync() {
    if (eHourConfig.isLdapSynced) {
      Log.info("Local database is already synced with LDAP - skipping.")
    } else {
      Log.info("****");
      Log.info("Matching existing users with LDAP, setting DN's and names (from CN)");
      syncWithLdap()
      Log.info("Finished matching")

      val config = configurationService.getConfiguration
      config.setLdapSynced(true)
      configurationService.persistConfiguration(config)
    }
  }

  private[init] def syncWithLdap() {
    val users = userDao.findAll().asScala

    users.foreach(u => {
      val ldapUsers = userService.getLdapUser(u.getUsername).asScala

      if (!ldapUsers.isEmpty) {
        if (ldapUsers.size == 1) {
          Log.info("For db user %s found ldap user %s, updating entry" format(u.getUsername, ldapUsers(0).dn))
          u.setDn(ldapUsers(0).dn)
          u.setName(ldapUsers(0).fullName)
          userDao.persist(u)
        } else if (ldapUsers.size == 0) {
          Log.error("No user found for username %s (id: %d), please fix manually." format (u.getUsername, u.getPK))
        } else {
          Log.error("Multiple users found in LDAP for username %s (id: %d) !" format(u.getUsername, u.getPK))
          Log.error("Please execute one of the following SQL statements manually:")
          ldapUsers.foreach(l => {
            Log.error("UPDATE USERS SET DN='%s',FULLNAME='%s' WHERE USER_ID = %d" format(l.dn, l.fullName, u.getPK))
          })
          Log.error("------")
        }
      }

    })
  }
}
