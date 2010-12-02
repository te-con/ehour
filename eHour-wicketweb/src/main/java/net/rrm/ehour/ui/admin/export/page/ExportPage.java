package net.rrm.ehour.ui.admin.export.page;

import net.rrm.ehour.export.service.ImportException;
import net.rrm.ehour.export.service.ImportService;
import net.rrm.ehour.export.service.ParseStatus;
import net.rrm.ehour.ui.admin.AbstractAdminPage;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * User: thies
 * Date: Nov 12, 2010
 * Time: 10:54:38 PM
 */
public class ExportPage extends AbstractAdminPage<Void>
{
    @SpringBean(name = "importService")
    private ImportService importService;

    private FileUploadField file;

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

        add(addUploadForm("form"));
    }

    private Form<?> addUploadForm(String id)
    {
        Form<?> form = new Form<Void>(id);
        form.setMultiPart(true);

        add(form);

        // create the file upload field
        form.add(file = new FileUploadField("file"));

        // create the ajax button used to submit the form
        form.add(new AjaxButton("ajaxSubmit")
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
                String contentType = file.getFileUpload().getContentType();

                if (contentType.contains("text"))
                {
                    byte[] bytes = file.getFileUpload().getBytes();
                    String xmlData = new String(bytes);

                    try
                    {
                        ParseStatus status = importService.prepareImportDatabase(xmlData);
                    } catch (ImportException e)
                    {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }

        });

        return form;
    }


}
