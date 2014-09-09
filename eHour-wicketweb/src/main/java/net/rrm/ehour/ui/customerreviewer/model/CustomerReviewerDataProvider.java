package net.rrm.ehour.ui.customerreviewer.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.rrm.ehour.timesheet.dto.UserProjectStatus;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

public class CustomerReviewerDataProvider extends SortableDataProvider<UserProjectStatus, Date> {

	private static final long serialVersionUID = -1681462964159319312L;
	
	List<UserProjectStatus> statusses;
	
	public CustomerReviewerDataProvider(List<UserProjectStatus> statusses) {
		this.statusses = statusses;
	}

    @Override
    public Iterator<? extends UserProjectStatus> iterator(long first, long count) {
        return statusses.iterator();
    }

    @Override
	public long size() {
		return statusses.size();
	}

	@Override
	public IModel<UserProjectStatus> model(UserProjectStatus object) {
		return new CompoundPropertyModel<UserProjectStatus>(object);
	}

}
