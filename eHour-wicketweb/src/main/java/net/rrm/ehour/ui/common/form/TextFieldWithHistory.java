package net.rrm.ehour.ui.common.form;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class TextFieldWithHistory<T> extends TextField<T> {
    private boolean previousValidity = false;
    private String previousValue;

    public TextFieldWithHistory(String id, IModel<T> model, Class<T> type) {
        super(id, model, type);
    }

    public boolean isValueChanged() {
        return !getRealInput().equals(previousValue);
    }

    public void rememberCurrentValue() {
        previousValue = getRealInput();
    }

    public void rememberCurrentValidity() {
        previousValidity = isValid();
    }

    public boolean isPreviousValid() {
        return previousValidity;
    }

    public final void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    /**
     * Extracts the real user input
     *
     * @return the string input or an empty string if no input provided
     */
    protected String getRealInput() {
        //first try to get the float value
        T value = getConvertedInput();
        if (value != null)
            return value.toString();
        else {
            //if there was a conversion error we can see the rawInput
            String raw = getRawInput();
            return  (raw == null) ? "" : raw;
        }
    }
}