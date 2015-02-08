package net.rrm.ehour.ui.admin.backup.restore;

import net.rrm.ehour.backup.domain.ParseSession;
import net.rrm.ehour.backup.service.backup.BackupEntity;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import org.apache.wicket.Component;
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
    private static final long serialVersionUID = 5985903285180288975L;

    public ParseStatusPanel(String id, IModel<ParseSession> model)
    {
        super(id, model);

        initPanel();
    }

    private void initPanel()
    {
        ParseSession session = getPanelModel().getObject();

        Map<BackupEntity, List<String>> errors = session.getErrors();

        add(createGlobalErrors("globalError", session));

        ListView<BackupEntity> errorsView = createErrorsList("errors", errors);
        errorsView.setVisible(!errors.isEmpty());
        add(errorsView);


        ListView<BackupEntity> insertionsView = createInsertionsList("insertions", session.getInsertions());
        insertionsView.setVisible(!session.getInsertions().isEmpty());
        add(insertionsView);
    }

    private Component createGlobalErrors(String id, ParseSession session) {
        Label label = new Label(id, session.getGlobalErrorMessage());
        label.setVisible(session.isGlobalError());
        return label;
    }


    private ListView<BackupEntity> createInsertionsList(String id, final Map<BackupEntity, Integer> insertions)
    {
        return new ListView<BackupEntity>(id, new ArrayList<>(insertions.keySet()))
        {
            @Override
            protected void populateItem(ListItem<BackupEntity> entryListItem)
            {
                BackupEntity type = entryListItem.getModelObject();

                entryListItem.add(new Label("key", type.name()));
                entryListItem.add(new Label("insertions", insertions.get(type).toString()));
            }
        };

    }

    private ListView<BackupEntity> createErrorsList(String id, final Map<BackupEntity, List<String>> errors)
    {
        return new ListView<BackupEntity>(id, new ArrayList<>(errors.keySet()))
        {
            @Override
            protected void populateItem(ListItem<BackupEntity> entryListItem)
            {
                BackupEntity entry = entryListItem.getModelObject();

                entryListItem.add(new Label("key", entry.name()));

                ListView<String> msgsList = new ListView<String>("msgs", errors.get(entry))
                {
                    @Override
                    protected void populateItem(ListItem<String> item)
                    {
                        item.add(new Label("msg", item.getModel()));
                    }
                };

                entryListItem.add(msgsList);
            }
        };
    }
}
