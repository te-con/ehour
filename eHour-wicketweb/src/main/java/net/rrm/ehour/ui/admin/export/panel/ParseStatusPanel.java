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

    private void initPanel()
    {
        ParseStatus parseStatus = getPanelModel().getObject();

        Map<ExportType, List<String>> errors = parseStatus.getErrors();

        add(createErrorsList("errors", errors));
        add(createInsertionsList("insertions", parseStatus.getInsertions()));
    }


    private ListView<Map.Entry<ExportType, Integer>> createInsertionsList(String id, Map<ExportType, Integer> insertions)
    {
        return new ListView<Map.Entry<ExportType, Integer>>(id, new ArrayList<Map.Entry<ExportType, Integer>>(insertions.entrySet()))
        {
            @Override
            protected void populateItem(ListItem<Map.Entry<ExportType, Integer>> entryListItem)
            {
                Map.Entry<ExportType, Integer> entry = entryListItem.getModelObject();

                entryListItem.add(new Label("key", entry.getKey().name()));
                entryListItem.add(new Label("insertions", entry.getValue().toString()));
            }
        };

    }

    private ListView<Map.Entry<ExportType, List<String>>> createErrorsList(String id, final Map<ExportType, List<String>> errors)
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
