package net.rrm.ehour.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "APPROVAL_STATUSES")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("serial")
public class ApprovalStatus extends DomainObject<Integer, ApprovalStatus> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "APPROVALSTATUS_ID")
	private Integer id;

	@Column(name = "DATE_START")
	private Date startDate;

	@Column(name = "DATE_END")
	private Date endDate;

	@ManyToOne
	@JoinColumn(name = "ACTIVITY_ID")
	private Activity activity;

    @Enumerated(EnumType.STRING)	
    @Column(name = "STATUS")
	private ApprovalStatusType status;

    @Column(name = "COMMENT_TEXT", length = 2048)
	private String comment;
	
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
		ApprovalStatus other = (ApprovalStatus) obj;
		return new EqualsBuilder().append(this.getId(), other.getId()).append(this.getStartDate(), other.getStartDate()).append(
				this.getEndDate(), other.getEndDate()).append(this.getActivity(), other.getActivity()).append(this.getStatus(),
				other.getStatus()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).append(startDate).append(endDate).append(activity).append(status).toHashCode();
	}

	@Override
	public Integer getPK() {
		return id;
	}

	@Override
	public int compareTo(ApprovalStatus other) {
		return new CompareToBuilder().append(this.getId(), other.getId()).append(this.getActivity(), other.getActivity()).append(
				this.getStartDate(), other.getStartDate()).append(this.getEndDate(), other.getEndDate()).append(this.getStatus(),
				other.getStatus()).toComparison();
	}

	@Override
	public String getFullName() {
		return getId().toString();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public ApprovalStatusType getStatus() {
		return status;
	}

	public void setStatus(ApprovalStatusType currentStatus) {
		this.status = currentStatus;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String coment) {
		this.comment = coment;
	}
}
