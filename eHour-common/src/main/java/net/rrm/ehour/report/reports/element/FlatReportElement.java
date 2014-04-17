/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.report.reports.element;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

/**
 * Report importer for trend reports (more data so each importer is flattened)
 */

public class FlatReportElement implements ProjectStructuredReportElement {
    private static final long serialVersionUID = -2146747873763924275L;
    private Integer customerId;
    private Boolean locked;
    private String customerName;
    private String customerCode;
    private Number totalHours = 0;
    private Number totalTurnOver = 0;
    private Number rate = 0;
    private String entryDate;
    private Integer userId;
    private String userLastName;
    private String userFirstName;
    private Integer projectId;
    private String projectName;
    private String projectCode;
    private Integer assignmentId;
    private String role;
    private Date dayDate;
    private String comment;
    private Integer displayOrder;
    private LockableDate lockableDate;
    private Boolean emptyEntry;

    public FlatReportElement() {
    }

    public FlatReportElement(FlatReportElement clone) {
        customerId = clone.customerId;
        locked = clone.locked;
        customerName = clone.customerName;
        customerCode = clone.customerCode;
        totalHours = clone.totalHours;
        totalTurnOver = clone.totalTurnOver;
        rate = clone.rate;
        entryDate = clone.entryDate;
        userId = clone.userId;
        userLastName = clone.userLastName;
        userFirstName = clone.userFirstName;
        projectId = clone.projectId;
        projectName = clone.projectName;
        projectCode = clone.projectCode;
        assignmentId = clone.assignmentId;
        role = clone.role;
        dayDate = clone.dayDate;
        comment = clone.comment;
        displayOrder = clone.displayOrder;
        lockableDate = clone.lockableDate;
        emptyEntry = clone.emptyEntry;
    }



    @Override
    public Boolean isEmptyEntry() {
        return emptyEntry == null ? false : emptyEntry;
    }

    public void setEmptyEntry(Boolean emptyEntry) {
        this.emptyEntry = emptyEntry;
    }

    public LockableDate getLockableDate() {
        return lockableDate;
    }

    public void setLockableDate(LockableDate lockableDate) {
        this.lockableDate = lockableDate;
    }

    public Number getRate() {
        return rate;
    }


    public void setRate(Number rate) {
        this.rate = rate;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the projectId
     */
    @Override
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * @param projectId the projectId to set
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return the totalHours
     */
    public Number getTotalHours() {
        return totalHours;
    }

    /**
     * @param totalHours the totalHours to set
     */
    public void setTotalHours(Number totalHours) {
        this.totalHours = totalHours;
    }

    /**
     * @return the totalTurnOver
     */
    public Number getTotalTurnOver() {
        return totalTurnOver == null ? 0 : totalTurnOver;
    }

    /**
     * @param totalTurnOver the totalTurnOver to set
     */
    public void setTotalTurnOver(Number totalTurnOver) {
        this.totalTurnOver = totalTurnOver;
    }

    /**
     * @return the userFirstName
     */
    public String getUserFirstName() {
        return userFirstName;
    }

    /**
     * @param userFirstName the userFirstName to set
     */
    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the userLastName
     */
    public String getUserLastName() {
        return userLastName;
    }

    /**
     * @param userLastName the userLastName to set
     */
    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    /**
     * @return the entryDate
     */
    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String weekYear) {
        this.entryDate = weekYear;
    }

    /**
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }


    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }


    /**
     * @return the customerCode
     */
    public String getCustomerCode() {
        return customerCode;
    }


    /**
     * @param customerCode the customerCode to set
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }


    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }


    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }


    /**
     * @return the assignmentId
     */
    public Integer getAssignmentId() {
        return assignmentId;
    }


    /**
     * @param assignmentId the assignmentId to set
     */
    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    /**
     * @return the dayDate
     */
    public Date getDayDate() {
        return dayDate;
    }

    /**
     * @param dayDate the dayDate to set
     */
    public void setDayDate(Date dayDate) {
        this.dayDate = dayDate;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("assignmentId", assignmentId)
                .append("date", dayDate)
                .append("totalHours", totalHours)
                .toString();
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object object) {
        if (!(object instanceof FlatReportElement)) {
            return false;
        }

        FlatReportElement rhs = (FlatReportElement) object;
        return new EqualsBuilder()
                .append(this.assignmentId, rhs.getAssignmentId())
                .append(this.dayDate, rhs.getDayDate())
                .append(this.entryDate, rhs.getEntryDate())
                .append(this.totalHours, rhs.getTotalHours())
                .append(this.totalTurnOver, rhs.getTotalTurnOver())
                .append(this.displayOrder, rhs.getDisplayOrder())
                .isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.assignmentId)
                .append(this.dayDate)
                .append(this.entryDate)
                .append(this.totalHours)
                .append(this.totalTurnOver)
                .append(this.displayOrder)
                .toHashCode();
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the displayOrder
     */
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    /**
     * @param displayOrder the displayOrder to set
     */
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
