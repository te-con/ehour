package net.rrm.ehour.domain;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
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
    @Basic(fetch = FetchType.EAGER)
	private Project project;

	@Column(name = "ACTIVE", nullable = false)
	@Type(type = "yes_no")
	private Boolean active = Boolean.TRUE;

    @Column(name = "LOCKED")
    @Type(type = "yes_no")
    private Boolean locked = Boolean.FALSE;


    // should be in a separate DAO
    @Transient
    private Float availableHours;

    public Activity() {
    }

    public Activity(Integer id) {
        this.id = id;
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

    public boolean contains(String otherName) {
        return StringUtils.isBlank(otherName) ||
                 ((name != null) && name.toLowerCase().contains(otherName));
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
        project.addActivity(this);
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

    public void mutateAvailableHours(Float delta) {
        synchronized (this) {
            availableHours += delta;
        }
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        if (!active.equals(activity.active)) return false;
        if (allottedHours != null ? !allottedHours.equals(activity.allottedHours) : activity.allottedHours != null)
            return false;
        if (!assignedUser.equals(activity.assignedUser)) return false;
        if (dateEnd != null ? !dateEnd.equals(activity.dateEnd) : activity.dateEnd != null) return false;
        if (dateStart != null ? !dateStart.equals(activity.dateStart) : activity.dateStart != null) return false;
        if (!name.equals(activity.name)) return false;
        if (!project.equals(activity.project)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (dateStart != null ? dateStart.hashCode() : 0);
        result = 31 * result + (dateEnd != null ? dateEnd.hashCode() : 0);
        result = 31 * result + (allottedHours != null ? allottedHours.hashCode() : 0);
        result = 31 * result + (assignedUser != null ? assignedUser.hashCode() : 0);
        result = 31 * result + (project != null ? project.hashCode() : 0);
        result = 31 * result + active.hashCode();
        return result;
    }

    @Override
    public int compareTo(Activity activity) {
        return new CompareToBuilder().append(this.getId(), activity.getId())
                .append(this.getProject(), activity.getProject())
                .append(this.getDateStart(), activity.getDateStart())
                .append(this.getDateEnd(), activity.getDateEnd())
                .append(this.getAllottedHours(), activity.getAllottedHours()).toComparison();
    }

	@Override
	public String getFullName() {
		return getName();
	}

	@Override
	public Integer getPK() {
		return id;
	}
}

