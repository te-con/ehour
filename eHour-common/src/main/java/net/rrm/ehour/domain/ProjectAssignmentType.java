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

package net.rrm.ehour.domain;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Project Assignment type
 */
@Entity
@Table(name = "PROJECT_ASSIGNMENT_TYPE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProjectAssignmentType extends DomainObject<Integer, ProjectAssignmentType> {
    private static final long serialVersionUID = -4306635642163206242L;
    private static final int ASSIGNMENT_DATE = 0;
    private static final int ASSIGNMENT_TIME_ALLOTTED_FIXED = 2;
    private static final int ASSIGNMENT_TIME_ALLOTTED_FLEX = 3;

    @Id
    @Column(name = "ASSIGNMENT_TYPE_ID")
    private Integer assignmentTypeId;

    @Column(name = "ASSIGNMENT_TYPE", length = 64)
    private String assignmentType;

    @Deprecated
    public ProjectAssignmentType() { }

    private ProjectAssignmentType(Integer assignmentTypeId) {
        this.assignmentTypeId = assignmentTypeId;
    }

    public static ProjectAssignmentType newAssignmentDate() {
        return new ProjectAssignmentType(ASSIGNMENT_DATE);
    }

    public static ProjectAssignmentType newAssignmentTimeAllottedFixed() {
        return new ProjectAssignmentType(ASSIGNMENT_TIME_ALLOTTED_FIXED);
    }

    public static ProjectAssignmentType newAssignmentTimeAllottedFlex() {
        return new ProjectAssignmentType(ASSIGNMENT_TIME_ALLOTTED_FLEX);
    }

    public boolean isDateType() {
        return assignmentTypeId == ASSIGNMENT_DATE;
    }

    public boolean isFixedAllottedType() {
        return assignmentTypeId == ASSIGNMENT_TIME_ALLOTTED_FIXED;
    }

    public boolean isFlexAllottedType() {
        return assignmentTypeId == ASSIGNMENT_TIME_ALLOTTED_FLEX;
    }

    public boolean isAllottedType() {
        return isFixedAllottedType() || isFlexAllottedType();
    }

    public String getResourceKeyForProjectAssignmentType() {
        switch (getAssignmentTypeId()) {
            case ASSIGNMENT_DATE:
                return "assignment.dateRange";
            case ASSIGNMENT_TIME_ALLOTTED_FIXED:
                return "assignment.allottedFixed";
            case ASSIGNMENT_TIME_ALLOTTED_FLEX:
                return "assignment.allottedFlex";
            default:
                return "assignment.allotted";
        }
    }

    /**
     * @return the assignmentType
     */
    public String getAssignmentType() {
        return assignmentType;
    }

    /**
     * @param assignmentType the assignmentType to set
     */
    public void setAssignmentType(String assignmentType) {
        this.assignmentType = assignmentType;
    }

    /**
     * @return the assignmentTypeId
     */
    public Integer getAssignmentTypeId() {
        return assignmentTypeId;
    }

    /**
     * @param assignmentTypeId the assignmentTypeId to set
     */
    public void setAssignmentTypeId(Integer assignmentTypeId) {
        this.assignmentTypeId = assignmentTypeId;
    }

    @Override
    public Integer getPK() {
        return getAssignmentTypeId();
    }

    /**
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo(ProjectAssignmentType type) {
        return new CompareToBuilder()
                .append(this.getAssignmentType(), type.getAssignmentType())
                .append(this.getAssignmentTypeId(), type.getAssignmentTypeId()).toComparison();
    }

    /*
     * (non-Javadoc)
     * @see net.rrm.ehour.domain.DomainObject#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        if ((this == other))
            return true;

        if (!(other instanceof ProjectAssignmentType))
            return false;

        ProjectAssignmentType castOther = (ProjectAssignmentType) other;

        return new EqualsBuilder()
                .append(this.getAssignmentTypeId(), castOther.getAssignmentTypeId())
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getAssignmentTypeId()).toHashCode();
    }
}
