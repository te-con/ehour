/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.common.form;

import net.rrm.ehour.persistence.value.ImageLogo;
import org.apache.log4j.Logger;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.util.ListModel;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created on Apr 22, 2009, 7:02:12 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public abstract class ImageUploadForm extends Form<Void> {
    private static final long serialVersionUID = 808442352504816831L;
    private FileUploadField fileUploadField;

    private static final Logger LOGGER = Logger.getLogger(ImageUploadForm.class);

    public ImageUploadForm(String id) {
        super(id);

        setMultiPart(true);
        add(fileUploadField = new FileUploadField("fileInput", new ListModel<FileUpload>()));

        add(new SubmitLink("uploadSubmit"));
    }

    /**
     * @see org.apache.wicket.markup.html.form.Form#onSubmit()
     */
    @Override
    protected void onSubmit() {
        final FileUpload upload = fileUploadField.getFileUpload();

        if (upload != null) {
            try {
                ImageLogo logo = parseImageLogo(upload);

                uploadImage(logo);
            } catch (Exception e) {
                LOGGER.warn("While uploading new image: " + e.getMessage());
                uploadImageError();
            }
        }
    }

    protected abstract void uploadImage(ImageLogo logo);

    protected abstract void uploadImageError();


    private ImageLogo parseImageLogo(FileUpload upload) throws IOException, ImageReadException {
        byte[] bytes = getBytes(upload);

        ImageLogo logo = new ImageLogo();
        logo.setImageData(bytes);

        Dimension imageSize = Sanselan.getImageSize(bytes);

        logo.setWidth((int) imageSize.getWidth());
        logo.setHeight((int) imageSize.getHeight());
        logo.setImageType(upload.getClientFileName().substring(upload.getClientFileName().lastIndexOf(".") + 1));

        return logo;
    }

    private byte[] getBytes(FileUpload upload) throws IOException {
        try (ByteArrayOutputStream bout = new ByteArrayOutputStream();
             InputStream in = new BufferedInputStream(upload.getInputStream())) {
            int b;

            while ((b = in.read()) != -1) {
                bout.write(b);
            }

            return bout.toByteArray();
        }
    }
}