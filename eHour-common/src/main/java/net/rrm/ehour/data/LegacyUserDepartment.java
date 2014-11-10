package net.rrm.ehour.data;

public class LegacyUserDepartment {
    private Integer userId;
    private Integer departmentId;

    public LegacyUserDepartment() {
    }

    public LegacyUserDepartment(Integer userId, Integer departmentId) {
        this.userId = userId;
        this.departmentId = departmentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}
