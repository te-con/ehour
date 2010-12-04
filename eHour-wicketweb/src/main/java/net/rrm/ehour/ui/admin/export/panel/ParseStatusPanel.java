package net.rrm.ehour.ui.admin.export.panel;

import net.rrm.ehour.export.service.ParseStatus;
import net.rrm.ehour.persistence.export.dao.ExportType;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author thies (Thies Edeling - thies@te-con.nl)
 *         Created on: 12/3/10 - 5:30 PM
 */
public class ParseStatusPanel extends AbstractBasePanel<ParseStatus>
{
    public ParseStatusPanel(String id, IModel<ParseStatus> model)
    {
        super(id, model);

        initPanel();
    }

    private void initPanel() {
        ParseStatus parseStatus = getPanelModel().getObject();

        Map<ExportType,List<String>> errors = parseStatus.getErrors();

        add(createListView("errors", errors));
    }

    private ListView<Map.Entry<ExportType, List<String>>> createListView(String id, final Map<ExportType, List<String>> errors)
    {
        return new ListView<Map.Entry<ExportType, List<String>>>(id, new ArrayList<Map.Entry<ExportType, List<String>>>(errors.entrySet()))
            {
                @Override
                protected void populateItem(ListItem<Map.Entry<ExportType, List<String>>> entryListItem)
                {
                    Map.Entry<ExportType, List<String>> entry = entryListItem.getModelObject();

                    entryListItem.add(new Label("key", entry.getKey().name()));

                    ListView<String> msgsList = new ListView<String>("msgs", entry.getValue())
                    {
                        @Override
                        protected void populateItem(ListItem<String> item)
                        {
                            String modelObject = item.getModelObject();
                            item.add(new Label("msg", item.getModel()));
                        }
                    };


                    entryListItem.add(msgsList);
                }
            };
    }
}
