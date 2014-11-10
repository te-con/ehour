package net.rrm.ehour.domain;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTimeZone;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TEAM")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Team  extends DomainObject<Integer, Team> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TEAM_ID")
    private Integer teamId;

    @Column(name = "NAME", nullable = false, length = 512)
    @NotNull
    private String name;

    @Column(name = "CODE", nullable = false, length = 512)
    @JoinColumn(name = "CODE")
    private Team code;

    @ManyToOne(optional = true)
    @JoinColumn(name = "MANAGER_USER_ID")
    private User manager;

    @Column(name = "timezone", nullable = true)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTimeZoneAsString")
    private DateTimeZone timeZone;

    @ManyToOne(optional = true)
    @JoinColumn(name = "PARENT_TEAM_ID")
    private Team parentTeam;

    @Override
    public Integer getPK() {
        return getTeamId();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Team)) {
            return false;
        }

        Team o = (Team) object;

        return new EqualsBuilder()
                .append(getCode(), o.getCode())
                .append(getName(), o.getName())
                .append(getManager(), o.getManager())
                .append(getTimeZone(), o.getTimeZone())
                .append(getParentTeam(), o.getParentTeam())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getCode())
                .append(getName())
                .append(getManager())
                .append(getTimeZone())
                .append(getParentTeam())
                .toHashCode();
    }

    @Override
    public int compareTo(Team o) {
        return new CompareToBuilder()
                .append(getCode(), o.getCode())
                .append(getName(), o.getName())
                .append(getManager(), o.getManager())
                .append(getTimeZone(), o.getTimeZone())
                .append(getParentTeam(), o.getParentTeam())
                .toComparison();
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team getCode() {
        return code;
    }

    public void setCode(Team code) {
        this.code = code;
    }

    public Team getParentTeam() {
        return parentTeam;
    }

    public void setParentTeam(Team parentTeam) {
        this.parentTeam = parentTeam;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public DateTimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(DateTimeZone timeZone) {
        this.timeZone = timeZone;
    }
}
