package net.rrm.ehour.ui.userprefs.panel;

import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl;
import net.rrm.ehour.ui.common.session.EhourWebSession;

public class ChangePasswordBackingBean extends AdminBackingBeanImpl {

    private String currentPassword;
    private String password;

    public String getUsername() {
        return EhourWebSession.getSession().getAuthUser().getUsername();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    @Override
    public DomainObject<?, ?> getDomainObject() {
        throw new IllegalArgumentException("Not implemented here");
    }
}
