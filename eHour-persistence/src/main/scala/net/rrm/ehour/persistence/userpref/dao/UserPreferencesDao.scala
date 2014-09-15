package net.rrm.ehour.persistence.userpref.dao

import net.rrm.ehour.domain._
import net.rrm.ehour.persistence.dao.{AbstractGenericDaoHibernateImpl, GenericDao}
import org.springframework.stereotype.Repository

trait UserPreferencesDao extends GenericDao[UserPreferenceId, UserPreference] {
  def getUserPreferenceForUserForType(user: User, userPreferenceType: UserPreferenceType): UserPreference
}

@Repository("userPreferencesDao")
class UserPreferencesDaoHibernateImpl extends AbstractGenericDaoHibernateImpl[UserPreferenceId, UserPreference](classOf[UserPreference]) with UserPreferencesDao {
  override def getUserPreferenceForUserForType(user: User, userPreferenceType: UserPreferenceType): UserPreference = {
    val keys = List("userPreferenceKey", "user")
    val params = List(userPreferenceType.getValue, user)

    val results = findByNamedQuery("UserPreference.getUserPreferenceForUserForType", keys, params)

    if (results.isEmpty) null else results.get(0)
  }
}
