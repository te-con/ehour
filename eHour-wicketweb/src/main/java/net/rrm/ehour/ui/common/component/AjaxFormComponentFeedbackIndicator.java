/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.common.component;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackCollector;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackIndicator;
import org.apache.wicket.model.Model;

import java.io.Serializable;
import java.util.List;


public class AjaxFormComponentFeedbackIndicator extends FormComponentFeedbackIndicator {
    private static final long serialVersionUID = 7840885174109746055L;
    private Component indicatorFor;

    public AjaxFormComponentFeedbackIndicator(String id, final Component indicatorFor) {
        super(id);

        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        this.indicatorFor = indicatorFor;
        setIndicatorFor(this.indicatorFor);

        add(new Label("errorText", new Model<Serializable>() {
            @Override
            public Serializable getObject() {
                List<FeedbackMessage> collect = new FeedbackCollector(getPage()).collect(getFeedbackMessageFilter());

                return !collect.isEmpty() ? collect.get(0).getMessage() : "";
            }
        }));
    }

    public void setIndicatorFor(Component indicatorFor) {
        super.setIndicatorFor(indicatorFor);

        this.indicatorFor = indicatorFor;
    }

    public boolean isIndicatingFor(Component other) {
        return indicatorFor == other;
    }


}
