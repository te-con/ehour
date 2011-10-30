package net.rrm.ehour.ui.userprefs.panel;

import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

public class PasswordFieldFactory {
    public static void createPasswordFields(Form<?> form, IModel<String> model) {
        // password inputs
        PasswordTextField passwordTextField = new PasswordTextField("password", model);
        passwordTextField.setLabel(new ResourceModel("user.password"));
        form.add(passwordTextField);

        PasswordTextField confirmPasswordTextField = new PasswordTextField("confirmPassword", new Model<String>());
        form.add(confirmPasswordTextField);
        form.add(new AjaxFormComponentFeedbackIndicator("confirmPasswordValidationError", confirmPasswordTextField));
        form.add(new EqualPasswordInputValidator(passwordTextField, confirmPasswordTextField) {
            protected String resourceKey() {
                return "user.errorConfirmPassNeeded";
            }
        });
    }
}
