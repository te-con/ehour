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

package net.rrm.ehour.ui.admin.config.panel;

import net.rrm.ehour.ui.common.component.ImageResource;
import net.rrm.ehour.value.ImageLogo;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;

/**
 * Created on Apr 22, 2009, 4:14:39 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class SkinConfigPanel extends AbstractConfigPanel
{
	private static final long serialVersionUID = -1274285277029402888L;

	public SkinConfigPanel(String id, IModel model)
	{
		super(id, model);
	}

	@Override
	protected void addFormComponents(Form form)
	{
		Image img = createPreviewImage();
		form.add(img);
	}

	private Image createPreviewImage()
	{
		final ImageLogo excelLogo = getConfigService().getExcelLogo();
		
		int width = excelLogo.getWidth();
		double divideBy = width / 350d;
		double height = (double)excelLogo.getHeight() / divideBy;
		
		Image img = new Image("excelImage");
		img.add(new SimpleAttributeModifier("width", "350"));
		img.add(new SimpleAttributeModifier("height", Integer.toString((int) height)));
		
		img.setImageResource(new ImageResource(excelLogo));
		return img;
	}
}
