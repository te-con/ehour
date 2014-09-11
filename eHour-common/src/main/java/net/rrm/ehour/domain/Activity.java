package net.rrm.ehour.domain;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "ACTIVITIES")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Activity extends DomainObject<Integer, Activity> {

	private static final long serialVersionUID = -6070312413971626368L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ACTIVITY_ID")
	private Integer id;
	
	@Column(name = "CODE", nullable = false, length = 128)
	@NotNull
	private String code;

	@Column(name = "NAME")
	@NotNull
	private String name;

	@Column(name = "DATE_START")
	private Date dateStart;

	@Column(name = "DATE_END")
	private Date dateEnd;

	@Column(name = "ALLOTTED_HOURS")
	private Float allottedHours;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User assignedUser;

	@ManyToOne
	@JoinColumn(name = "PROJECT_ID")
	private Project project;

	@Column(name = "ACTIVE", nullable = false)
	@Type(type = "yes_no")
	private Boolean active = Boolean.TRUE;

    // should be in a separate DAO
    @Transient
    private Float availableHours;

    public Activity() {
    }

    public Activity(User assignedUser, Project project) {
        this.assignedUser = assignedUser;
        this.project = project;
    }


    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Float getAllottedHours() {
		return allottedHours;
	}

	public void setAllottedHours(Float allottedHours) {
		this.allottedHours = allottedHours;
	}

	public User getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

    public Float getAvailableHours()
    {
        return availableHours;
    }

    public void setAvailableHours(Float availableHours)
    {
        this.availableHours = availableHours;
    }

    @Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Activity other = (Activity) obj;
		return new EqualsBuilder().append(this.getId(), other.getId())
		                          .append(this.getName(), other.getName())
		                          .append(this.getAllottedHours(), other.getAllottedHours())
		                          .append(this.getActive(), other.getActive())
		                          .append(this.getDateStart(), other.getDateStart())
		                          .append(this.getDateEnd(), other.getDateEnd())
		                          .append(this.getProject(), other.getProject())
		                          .append(this.getAssignedUser(), other.getAssignedUser())
		                          .isEquals();
	}

	@Override
	public String getFullName() {
		return getName();
	}

	@Override
	public Integer getPK() {
		return id;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name).append(dateStart).append(dateEnd).append(allottedHours).append(active).toHashCode();
	}

	@Override
	public int compareTo(Activity activity) {
		return new CompareToBuilder().append(this.getId(), activity.getId())
									 .append(this.getProject(), activity.getProject())
									 .append(this.getDateStart(), activity.getDateStart())
									 .append(this.getDateEnd(), activity.getDateEnd())
									 .append(this.getAllottedHours(), activity.getAllottedHours()).toComparison();
	}


}

