package net.rrm.ehour.ui.common.panel.entryselector;

import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.common.decorator.LoadingSpinnerDecorator;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

import java.util.List;

import static net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorData.EntrySelectorRow;

public abstract class EntrySelectorListView extends ListView<EntrySelectorRow> {
    private final EntrySelectorPanel.ClickHandler clickHandler;
    private ListItem<EntrySelectorRow> selectedItem = null;

    public EntrySelectorListView(String id, List<? extends EntrySelectorRow> list, EntrySelectorPanel.ClickHandler clickHandler) {
        super(id, list);
        this.clickHandler = clickHandler;
    }

    @Override
    protected final void populateItem(final ListItem<EntrySelectorRow> item) {
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
                    clickHandler.onClick(item.getModelObject(), target);
                } catch (ObjectNotFoundException e) {
                }
            }

            @Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);

                attributes.getAjaxCallListeners().add(new LoadingSpinnerDecorator());
            }
        });

        onPopulate(item, item.getModel());
    }

    protected abstract void onPopulate(ListItem<EntrySelectorRow> item, IModel<EntrySelectorRow> itemModel);
}
