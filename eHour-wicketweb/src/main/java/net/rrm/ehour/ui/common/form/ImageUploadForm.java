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

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import net.rrm.ehour.value.ImageLogo;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;

/**
 * Created on Apr 22, 2009, 7:02:12 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public abstract class ImageUploadForm extends Form
{
	private static final long serialVersionUID = 808442352504816831L;
	private FileUploadField fileUploadField;

    public ImageUploadForm(String id, IModel model)
    {
        super(id, model);

        setMultiPart(true);
        add(fileUploadField = new FileUploadField("fileInput"));
    }

    /**
     * @see org.apache.wicket.markup.html.form.Form#onSubmit()
     */
    @Override
    protected void onSubmit()
    {
        final FileUpload upload = fileUploadField.getFileUpload();
        
        if (upload != null)
        {
        	try
			{
				ImageLogo logo = parseImageLogo(upload);
				
				uploadImage(logo);
			} catch (IOException e)
			{
				uploadImageError();
			}
        }
    }

    protected abstract void uploadImage(ImageLogo logo);
    
    protected abstract void uploadImageError();  
    
    
    private ImageLogo parseImageLogo(FileUpload upload) throws IOException
    {
    	byte[] bytes = getBytes(upload);
    	
    	Image img = Toolkit.getDefaultToolkit().createImage(bytes);
    	
    	ImageLogo logo = new ImageLogo();
    	logo.setImageData(bytes);
    	
    	Observer widthObserver = new Observer();
    	Observer heightObserver = new Observer();
    	
    	
    	logo.setWidth(img.getWidth(widthObserver));
    	logo.setHeight(img.getHeight(heightObserver));
    	logo.setImageType(upload.getClientFileName().substring(upload.getClientFileName().lastIndexOf(".") + 1));
    	waitForObservers(widthObserver, heightObserver);
    	return logo;
    }
    
    private void waitForObservers(Observer... observers)
    {
		boolean allDone = true;

		do
		{
			allDone = true;
			
			for (Observer observer : observers)
			{
	    		allDone &= observer.done; 
			}
			
			if (!allDone)
			{
				try
				{
					Thread.sleep(300);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		while (!allDone);
    }
    
    private byte[] getBytes(FileUpload upload) throws IOException
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
		InputStream in = new BufferedInputStream(upload.getInputStream());
		int b;
		
		while ((b = in.read()) != -1)
		{
			bout.write(b);
		}
		
		return bout.toByteArray();
    }

    
	private class Observer implements ImageObserver
	{
		boolean done = false;

		public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height)
		{
			if (infoflags == ALLBITS)
			{
				System.out.println(infoflags);
				done = true;
				return true;
			}
			
			return false;
		}

	}

}	