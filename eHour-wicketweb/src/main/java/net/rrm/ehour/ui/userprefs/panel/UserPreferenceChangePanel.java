package net.rrm.ehour.ui.userprefs.panel;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserPreference;
import net.rrm.ehour.domain.UserPreferenceType;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.userpref.UserPreferenceService;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class UserPreferenceChangePanel extends Panel {

	private static final long serialVersionUID = 4028974383646159890L;
	
	@SpringBean
	private UserPreferenceService userPreferenceService;
	
	private User user;

	public UserPreferenceChangePanel(String id, User user) {
		super(id);
		this.user = user;
		addComponents();
	}

	private void addComponents() {
		Border greyBorder = new GreyRoundedBorder("border", new ResourceModel("userprefs.title"), WebGeo.W_CONTENT_XSMALL);
		add(greyBorder);
		
		Form<Void> userPreferenceChangeForm = new Form<Void>("userPreferenceChangeForm");
		userPreferenceChangeForm.setOutputMarkupId(true);
		
		AjaxButton hideWeekendButton = new AjaxButton("hideWeekendsButton") {
			
			private static final long serialVersionUID = -6080612594252857510L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				UserPreference userPreference = userPreferenceService.getUserPreferenceForUserForType(user, UserPreferenceType.DISABLE_WEEKENDS);
				System.out.println("" + userPreference);
				
			}
		};
		
		hideWeekendButton.setOutputMarkupId(true);
		
		userPreferenceChangeForm.add(hideWeekendButton);
		
		greyBorder.add(userPreferenceChangeForm);
		
		setOutputMarkupId(true);		
	}



}
