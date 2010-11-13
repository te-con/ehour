package net.rrm.ehour.ui.admin.export.page;

import net.rrm.ehour.ui.admin.AbstractAdminPage;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;

/**
 * User: thies
 * Date: Nov 12, 2010
 * Time: 10:54:38 PM
 */
public class ExportPage extends AbstractAdminPage<Void>
{
    public ExportPage()
    {
        super(new ResourceModel("admin.export.title"), "admin.export.help.header", "admin.export.help.body");

        add(new Link<Void>("exportLink")
        {
            @Override
            public void onClick()
            {
                ResourceReference exportReference = new ResourceReference(ExportDatabase.ID_EXPORT_DB);

                exportReference.bind(getApplication());
                CharSequence url = getRequestCycle().urlFor(exportReference);

                getRequestCycle().setRequestTarget(new RedirectRequestTarget(url.toString()));
            }
        });
    }
}
