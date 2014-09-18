package net.rrm.ehour.ui.common.form;

public interface HistoryFormComponent {
    void rememberCurrentValidity();

    void rememberCurrentValue();

    boolean isPreviousValid();

    boolean isValueChanged();
}
