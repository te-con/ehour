package net.rrm.ehour.ui.common.panel.entryselector;

import net.rrm.ehour.exception.ObjectNotFoundException;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

import java.util.List;

public abstract class EntrySelectorListView<T> extends ListView<T> {
    private ListItem<T> selectedItem = null;

    public EntrySelectorListView(String id, List<? extends T> list) {
        super(id, list);
    }

    @Override
    protected final void populateItem(final ListItem<T> item) {
        item.setOutputMarkupId(true);

        item.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                item.add(AttributeModifier.replace("class", "selectedRow"));
                target.add(item);

                if (selectedItem != null) {
                    selectedItem.add(AttributeModifier.replace("class", "filterRow"));
                    target.add(selectedItem);
                }

                selectedItem = item;

                try {
                    onClick(item, target);
                } catch (ObjectNotFoundException e) {
                }
            }
        });

        onPopulate(item, item.getModel());
    }

    protected abstract void onPopulate(ListItem<T> item, IModel<T> itemModel);

    protected abstract void onClick(ListItem<T> item, AjaxRequestTarget target) throws ObjectNotFoundException;
}
