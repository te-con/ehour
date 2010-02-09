package net.rrm.ehour.ui.admin.content.tree;

import javax.swing.tree.TreeModel;

import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.markup.html.tree.WicketTreeModel;
import org.apache.wicket.model.IModel;

/**
 * Created on Feb 9, 2010 1:26:31 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class ContentTree extends LinkTree
{
	private static final long serialVersionUID = 3601647907643211669L;

	public ContentTree(String id, TreeModel model)
	{
		super(id, new WicketTreeModel());
		setModelObject(model);
	}
	
	@Override
	protected Component newNodeComponent(String id, IModel<Object> model)
	{
		return new ContentNodePanel(id, model, ContentTree.this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onNodeLinkClicked(Object node, BaseTree tree, AjaxRequestTarget target)
			{
				super.onNodeLinkClicked(node, tree, target);
				ContentTree.this.onNodeLinkClicked(node, tree, target);
			}
			
			@Override
			protected void onNodeLinkSelected(Object node, BaseTree tree, AjaxRequestTarget target)
			{
				super.onNodeLinkSelected(node, tree, target);
				ContentTree.this.onNodeLinkClicked(node, tree, target);
			}
			

			@Override
			protected Component newContentComponent(String componentId, BaseTree tree,
				IModel<Object> model)
			{
				return new Label(componentId, getNodeTextModel(model));
			}
		};
	}
	
	protected void onNodeLinkClicked(Object node, BaseTree tree, AjaxRequestTarget target)
	{
		PayloadAjaxEvent<Object> event = new PayloadAjaxEvent<Object>(TreeNodeEventType.NODE_SELECTED, node);
		EventPublisher.publishAjaxEvent(this, event);
	}
}
