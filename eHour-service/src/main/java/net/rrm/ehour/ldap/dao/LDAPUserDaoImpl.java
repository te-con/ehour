
package net.rrm.ehour.ldap.dao;

import java.util.List;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import net.rrm.ehour.ldap.LdapUserVO;

import org.springframework.ldap.AttributesMapper;
import org.springframework.ldap.ContextMapper;
import org.springframework.ldap.LdapTemplate;
import org.springframework.ldap.UncategorizedLdapException;
import org.springframework.ldap.support.DirContextAdapter;
import org.springframework.ldap.support.DistinguishedName;
import org.springframework.ldap.support.filter.EqualsFilter;

/**
 * @author Bouayad Mehdi
 */
public class LDAPUserDaoImpl implements LDAPUserDao {

	private LdapTemplate ldapTemplate;
	private String ldapUserDnPattern;

	public String getLdapUserDnPattern() {
		return ldapUserDnPattern;
	}

	public void setLdapUserDnPattern(String ldapUserDnPattern) {
		this.ldapUserDnPattern = ldapUserDnPattern;
	}

	/*
	 * @see LDAPUserDao#create(LDAPUser)
	 */
	public void create(LdapUserVO user) throws Exception {
		Name dn = buildDn(user);
		DirContextAdapter context = new DirContextAdapter(dn);
		mapToContext(user, context);
		ldapTemplate.bind(dn, context, null);
	}

	/*
	 * @see LDAPUserDao#update(LDAPUser)
	 */
	public void update(LdapUserVO user) {
		Name dn = buildDn(user);
		DirContextAdapter context = (DirContextAdapter) ldapTemplate.lookup(dn);
		mapToContext(user, context);
		try {
			ldapTemplate.modifyAttributes(dn, context.getModificationItems());
		} catch (Exception e) {
			e.printStackTrace();
			throw new UncategorizedLdapException(
					"Couldn't get modification items for '" + dn + "'", e);
		}
	}

	/*
	 * @see LDAPUserDao#delete(LDAPUser)
	 */
	public void delete(LdapUserVO user) {
		ldapTemplate.unbind(buildDn(user));
	}

	/*
	 * @see LDAPUserDao#getAllLDAPUserNames()
	 */
	public List getAllLDAPUserNames() {
		EqualsFilter filter = new EqualsFilter("objectclass", "user");
		return ldapTemplate.search(DistinguishedName.EMPTY_PATH, filter
				.encode(), new AttributesMapper() {
			public Object mapFromAttributes(Attributes attrs)
					throws NamingException {
				return attrs.get("cn").get();
			}
		});
	}

	/*
	 * @see LDAPUserDao#findAll()
	 */
	public List findAll() {
		EqualsFilter filter = new EqualsFilter("objectclass", "top");
		DistinguishedName dn = new DistinguishedName();

		// TODO you should add required entries to match your needs in term of DN
		//dn.add("CN", "users");
		
		return ldapTemplate.search(dn, filter
			.encode(), getContextMapper());

	}

	/*
	 * @see LDAPUserDao#findByPrimaryKey(java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	public LdapUserVO findByPrimaryKey(String uid) {
		
		//DistinguishedName dn = buildDn(uid, "users");
		DistinguishedName dn = buildDn(uid);
		return (LdapUserVO) ldapTemplate.lookup(dn, getContextMapper());
	}

	private ContextMapper getContextMapper() {
		return new LDAPUserContextMapper();
	}

	private DistinguishedName buildDn(LdapUserVO user) {
		
		return buildDn(user.getLogin());
	}

	private DistinguishedName buildDn(String userName) {
		DistinguishedName dn = new DistinguishedName();
		//dn.add("CN", userName);
		
		dn.add(getLdapUserDnPattern(), userName);
		
		return dn;
	}

	private void mapToContext(LdapUserVO user, DirContextAdapter context) {
		
		context.setAttributeValues("objectclass", new String[]{"top", "person",
				   "organizationalPerson", "user"});
		
		context.setAttributeValue("displayName", user.getLastName());
		context.setAttributeValue("givenName", user.getFirstName());
		
		context.setAttributeValue("description", user.getDescription());
		context.setAttributeValue("cn", user.getLogin());
		context.setAttributeValue("mail", user.getEmail());
		context.setAttributeValue("userpassword", user.getPassword());

	}

	/**
	 * Maps from DirContextAdapter to LDAPUser objects. A DN for a user will be
	 * of the form <code>cn=[fullname],ou=[company],c=[country]</code>, so
	 * the values of these attributes must be extracted from the DN. For this,
	 * we use the DistinguishedName.
	 * 
	 * @author Mattias Arthursson
	 * @author Ulrik Sandberg
	 */
	private static class LDAPUserContextMapper implements ContextMapper {

		public Object mapFromContext(Object ctx) {
			DirContextAdapter context = (DirContextAdapter) ctx;
			DistinguishedName dn = new DistinguishedName(context.getDn());
			LdapUserVO user = new LdapUserVO();

			user.setLastName(context.getStringAttribute("displayName"));
			user.setFirstName(context.getStringAttribute("givenName"));
			
			user.setDescription(context.getStringAttribute("description"));
			user.setLogin(context.getStringAttribute("cn"));
			user.setEmail(context.getStringAttribute("mail"));
			user.setPassword(context.getStringAttribute("userpassword"));

			return user;
		}
	}

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	


	
	
}
