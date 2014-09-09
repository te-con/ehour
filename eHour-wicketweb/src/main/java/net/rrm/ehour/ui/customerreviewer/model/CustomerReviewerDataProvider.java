package net.rrm.ehour.ui.customerreviewer.model;

import net.rrm.ehour.domain.Activity;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CustomerReviewerDataProvider extends SortableDataProvider<Activity, Date> {

	private static final long serialVersionUID = -1681462964159319312L;
	
	List<Activity> activities;
	
	public CustomerReviewerDataProvider(List<Activity> activities) {
		this.activities = activities;
	}

    @Override
    public Iterator<? extends Activity> iterator(long first, long count) {
        return activities.iterator();
    }

    @Override
	public long size() {
		return activities.size();
	}

	@Override
	public IModel<Activity> model(Activity object) {
		return new CompoundPropertyModel<Activity>(object);
	}

}
