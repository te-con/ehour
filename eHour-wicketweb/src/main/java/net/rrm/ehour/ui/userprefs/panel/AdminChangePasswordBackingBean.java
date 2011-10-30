package net.rrm.ehour.ui.userprefs.panel;

public class AdminChangePasswordBackingBean extends ChangePasswordBackingBean {
    private String username;

    public AdminChangePasswordBackingBean(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
