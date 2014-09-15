package net.rrm.ehour.ui.userprefs.panel;

import net.rrm.ehour.domain.*;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.userprefs.page.UserPreferencePage;
import net.rrm.ehour.userpref.UserPreferenceService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class UserPreferenceChangePanel extends Panel {

    private static final long serialVersionUID = 4028974383646159890L;

    private static final String WEEKEND_VISIBLE = "userprefs.weekend.enable";

    private static final String WEEKEND_HIDDEN = "userprefs.weekend.disable";

    @SpringBean
    private UserPreferenceService userPreferenceService;

    private User user;

    public UserPreferenceChangePanel(String id, User user) {
        super(id);
        this.user = user;
        addComponents();
    }

    private void addComponents() {
        Border greyBorder = new GreyRoundedBorder("border", new ResourceModel("userprefs.panel.title"));
        greyBorder.setOutputMarkupId(true);
        add(greyBorder);

        Form<Void> userPreferenceChangeForm = new Form<Void>("userPreferenceChangeForm");
        userPreferenceChangeForm.setOutputMarkupId(true);

        String currentWeekendVisibilitySettings = getCurrentWeekendVisibilitySettings();

        final Label userPreferenceSelection = new Label("userPreferenceSelection", new ResourceModel(currentWeekendVisibilitySettings));

        userPreferenceSelection.setOutputMarkupId(true);

        userPreferenceChangeForm.add(userPreferenceSelection);

        AjaxButton hideWeekendButton = new AjaxButton("hideWeekendsButton") {

            private static final long serialVersionUID = -6080612594252857510L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                UserPreference userPreference = userPreferenceService.getUserPreferenceForUserForType(user, UserPreferenceType.DISABLE_WEEKENDS);
                if(userPreference == null) {
                    UserPreferenceId userPreferenceId = new UserPreferenceId(UserPreferenceType.DISABLE_WEEKENDS.getValue(), user);
                    userPreference = new UserPreference(userPreferenceId, UserPreferenceType.DISABLE_WEEKENDS);
                    userPreferenceService.persist(userPreference);
                } else {
                    userPreference.setUserPreferenceValue(UserPreferenceType.DISABLE_WEEKENDS.getUserPreferenceValueType().name());
                    userPreferenceService.merge(userPreference);
                }
                setResponsePage(UserPreferencePage.class);
            }
        };

        hideWeekendButton.setOutputMarkupId(true);
        if (WEEKEND_HIDDEN.equalsIgnoreCase(currentWeekendVisibilitySettings)) {
            hideWeekendButton.setVisible(false);
        }


        AjaxButton enableWeekendsButton = new AjaxButton("enableWeekendsButton") {

            private static final long serialVersionUID = -6080612594252857510L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                UserPreference userPreference = userPreferenceService.getUserPreferenceForUserForType(user, UserPreferenceType.ENABLE_WEEKENDS);
                if (userPreference == null) {
                    UserPreferenceId userPreferenceId = new UserPreferenceId(UserPreferenceType.ENABLE_WEEKENDS.getValue(), user);
                    userPreference = new UserPreference(userPreferenceId, UserPreferenceType.ENABLE_WEEKENDS);
                    userPreferenceService.persist(userPreference);
                } else {
                    userPreference.setUserPreferenceValue(UserPreferenceType.ENABLE_WEEKENDS.getUserPreferenceValueType().name());
                    userPreferenceService.merge(userPreference);
                }
                setResponsePage(UserPreferencePage.class);
            }
        };

        enableWeekendsButton.setOutputMarkupId(true);
        if (WEEKEND_VISIBLE.equalsIgnoreCase(currentWeekendVisibilitySettings)) {
            enableWeekendsButton.setVisible(false);
        }

        userPreferenceChangeForm.add(hideWeekendButton);
        userPreferenceChangeForm.add(enableWeekendsButton);
        greyBorder.add(userPreferenceChangeForm);

        setOutputMarkupId(true);
    }

    private String getCurrentWeekendVisibilitySettings() {
        UserPreference userPreference = userPreferenceService.getUserPreferenceForUserForType(user, UserPreferenceType.ENABLE_WEEKENDS);
        if (userPreference != null && UserPreferenceValueType.ENABLE.name().equalsIgnoreCase(userPreference.getUserPreferenceValue())) {
            return WEEKEND_HIDDEN;
        } else {
            return WEEKEND_VISIBLE;
        }
    }

}
