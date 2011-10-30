package net.rrm.ehour.ui.userprefs.panel;

import net.rrm.ehour.domain.DomainObject;
import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl;
import net.rrm.ehour.ui.common.session.EhourWebSession;

public class ChangePasswordBackingBean extends AdminBackingBeanImpl {

    private String password;

    public String getUsername() {
        return EhourWebSession.getSession().getUser().getUsername();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public DomainObject<?, ?> getDomainObject() {
        throw new IllegalArgumentException("Not implemented here");
    }
}
