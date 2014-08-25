package net.rrm.ehour.domain;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "ACTIVITY")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Activity extends DomainObject<Integer, Activity> {

 private static final long serialVersionUID = -6070312413971626368L;

 @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ACTIVITY_ID")
    private Integer id;

    @Column(name = "NAME", length = 255)
    @NotNull
 private String name;

 @Column(name = "DATE_START")
 @NotNull
 private Date dateStart;

 @Column(name = "DATE_END")
 @NotNull
 private Date dateEnd;

 @Column(name = "ALLOTTED_HOURS")
 @NotNull
 private Float allottedHours;

 @ManyToOne
    @JoinColumn(name = "USER_ID")
 private User assignedUser;
 
 @ManyToOne
 @JoinColumn(name = "PROJECT_ID")
 private Project project;
 
 public Integer getId() {
  return id;
 }

 public void setId(Integer id) {
  this.id = id;
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
  return new EqualsBuilder().append(this.getId(), other.getId()).isEquals();
 }

 @Override
 public Integer getPK() {
  return id;
 }

 @Override
 public int hashCode() {
  return new HashCodeBuilder().append(name).append(dateStart).append(dateEnd).append(allottedHours).toHashCode();
 }

 @Override
 public int compareTo(Activity activity) {
  return new CompareToBuilder().append(this.getId(), activity.getId()).toComparison();
 }
}
