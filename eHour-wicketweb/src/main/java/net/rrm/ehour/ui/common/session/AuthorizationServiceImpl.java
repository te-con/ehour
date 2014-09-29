package net.rrm.ehour.ui.common.session;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.user.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.List;

@Service("authenticationService")
public class AuthorizationServiceImpl implements AuthorizationService {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = Logger.getLogger(AuthorizationServiceImpl.class);

    @Override
    public User getAuthorizedUser(String ldapUid) {

        User user = userService.getUser(ldapUid);

        if (user != null) {
            String filter = String.format("(&(uid=%s)(objectClass=person))", ldapUid);

            List cn = ldapTemplate.search("", filter, new AttributesMapper() {
                public Object mapFromAttributes(Attributes attrs) throws NamingException {
                    return attrs.get("cn").get();
                }
            });

            if (cn.size() > 1) {
                LOGGER.warn(String.format("Multiple LDAP entries found for uid %s", ldapUid));
            }

            if (cn.size() > 0) {
                user.setFullName((String) cn.get(0));
            } else {
                LOGGER.warn(String.format("No LDAP entry found for uid %s", ldapUid));
            }
        }

        return user;
    }
}
