package net.rrm.ehour.ui.customerreviewer.model;

import net.rrm.ehour.domain.ApprovalStatus;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CustomerReviewerDataProvider extends SortableDataProvider<ApprovalStatus, Date> {

	private static final long serialVersionUID = -1681462964159319312L;
	
	List<ApprovalStatus> approvalStatuses;
	
	public CustomerReviewerDataProvider(List<ApprovalStatus> approvalStatuses) {
		this.approvalStatuses = approvalStatuses;
	}

	@Override
	public Iterator<? extends ApprovalStatus> iterator(long first, long count) {
		return approvalStatuses.iterator();
	}

	@Override
	public long size() {
		return approvalStatuses.size();
	}

	@Override
	public IModel<ApprovalStatus> model(ApprovalStatus object) {
		return new CompoundPropertyModel<ApprovalStatus>(object);
	}

}
