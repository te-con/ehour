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

package net.rrm.ehour.ui.test;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.apache.wicket.Component;
import org.apache.wicket.IEventDispatcher;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

import java.util.List;

/**
 * WicketTester with some enhancements:
 * - uses a StrictFormTester
 * - WICKET-254 inclusion
 */

public class StrictWicketTester extends WicketTester {

    private final EventInterceptor interceptor;

    public StrictWicketTester(WebApplication webApplication) {
        super(webApplication);

        interceptor = new EventInterceptor();
        webApplication.getFrameworkSettings().add(interceptor);
    }

    @Override
    public FormTester newFormTester(String path, boolean fillBlankString) {
        return new StrictFormTester(path, (Form<?>) getComponentFromLastRenderedPage(path), this, fillBlankString);
    }

    public void clickAjaxCheckBoxToEnable(String path) {
        modifyAjaxCheckBox(path, true);
    }

    public void disableAjaxCheckBox(String path) {
        modifyAjaxCheckBox(path, false);
    }

    private void modifyAjaxCheckBox(String path, Boolean enable) {
        AjaxCheckBox component = (AjaxCheckBox) getComponentFromLastRenderedPage(path);
        getRequest().getPostParameters().setParameterValue(component.getInputName(), enable.toString());
        executeAjaxEvent(component, "click");
    }

    public Optional<EventInterceptor.InterceptedEvent> findEvent(Class payloadClass) {
        return interceptor.findEvent(payloadClass);
    }

    public static class EventInterceptor implements IEventDispatcher {

        private List<InterceptedEvent> interceptedEvents = Lists.newArrayList();

        @Override
        public void dispatchEvent(Object sink, IEvent<?> event, Component component) {
            interceptedEvents.add(new InterceptedEvent(sink, event, component));
        }

        public Optional<InterceptedEvent> findEvent(Class payloadClass) {
            for (InterceptedEvent interceptedEvent : interceptedEvents) {
                if (interceptedEvent.event.getPayload().getClass().equals(payloadClass)) {
                    return Optional.of(interceptedEvent);
                }
            }

            return Optional.absent();
        }

        public static class InterceptedEvent {
            public final Object sink;
            public final IEvent<?> event;
            public final Component component;

            public InterceptedEvent(Object sink, IEvent<?> event, Component component) {
                this.sink = sink;
                this.event = event;
                this.component = component;
            }
        }
    }

}
