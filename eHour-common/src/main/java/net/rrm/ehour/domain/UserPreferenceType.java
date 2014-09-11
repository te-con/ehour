package net.rrm.ehour.domain;

public enum UserPreferenceType {

	DISABLE_WEEKENDS("DISABLE_WEEKENDS", UserPreferenceValueType.yes), ENABLE_WEEKENDS("ENABLE_WEEKENDS", UserPreferenceValueType.no);

	private UserPreferenceValueType userPreferenceValueType;

	private String value;

	private UserPreferenceType(String value, UserPreferenceValueType userPreferenceValueType) {
		this.userPreferenceValueType = userPreferenceValueType;
		this.value = value;
	}

	public UserPreferenceValueType getUserPreferenceValueType() {
		return userPreferenceValueType;
	}

	public void setUserPreferenceValueType(UserPreferenceValueType userPreferenceValueType) {
		this.userPreferenceValueType = userPreferenceValueType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}