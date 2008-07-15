
package net.rrm.ehour.ldap.dao;

import java.util.List;

import net.rrm.ehour.ldap.LdapUserVO;


/**
 * @author  Bouayad Mehdi
 */
public interface LDAPUserDao {
   void create(LdapUserVO person) throws Exception;

   void update(LdapUserVO person);

   void delete(LdapUserVO person);

   List getAllLDAPUserNames();

   List findAll();

   LdapUserVO findByPrimaryKey(String uid);
   
      
}
