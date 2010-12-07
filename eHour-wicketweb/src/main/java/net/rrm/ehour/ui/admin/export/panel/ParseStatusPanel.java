package net.rrm.ehour.ui.admin.export.panel;

import net.rrm.ehour.export.service.ParseSession;
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
public class ParseStatusPanel extends AbstractBasePanel<ParseSession>
{
    public ParseStatusPanel(String id, IModel<ParseSession> model)
    {
        super(id, model);

        initPanel();
    }

    private void initPanel()
    {
        ParseSession parseStatus = getPanelModel().getObject();

        Map<ExportType, List<String>> errors = parseStatus.getErrors();

        add(createErrorsList("errors", errors));
        add(createInsertionsList("insertions", parseStatus.getInsertions()));
    }


    private ListView<ExportType> createInsertionsList(String id, final Map<ExportType, Integer> insertions)
    {
        return new ListView<ExportType>(id, new ArrayList<ExportType>(insertions.keySet()))
        {
            @Override
            protected void populateItem(ListItem<ExportType> entryListItem)
            {
                ExportType type = entryListItem.getModelObject();

                entryListItem.add(new Label("key", type.name()));
                entryListItem.add(new Label("insertions", insertions.get(type).toString()));
            }
        };

    }

    private ListView<ExportType> createErrorsList(String id, final Map<ExportType, List<String>> errors)
    {
        return new ListView<ExportType>(id, new ArrayList<ExportType>(errors.keySet()))
        {
            @Override
            protected void populateItem(ListItem<ExportType> entryListItem)
            {
                ExportType entry = entryListItem.getModelObject();

                entryListItem.add(new Label("key", entry.name()));

                ListView<String> msgsList = new ListView<String>("msgs", errors.get(entry))
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
