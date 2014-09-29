package net.rrm.ehour.ui.common.session;

import net.rrm.ehour.domain.User;

public interface AuthorizationService {
    User getAuthorizedUser(String ldapUid);


}
