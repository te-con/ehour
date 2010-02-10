package net.rrm.ehour.ui.admin.content.tree;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LabelIconPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Created on Feb 9, 2010 1:29:30 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class ContentNodePanel extends LabelIconPanel
{
	private static final long serialVersionUID = 1L;

	public ContentNodePanel(String id, IModel<Object> model, BaseTree tree)
	{
		super(id, model, tree);
	}

	@Override
	protected void addComponents(final IModel<Object> model, final BaseTree tree)
	{
		add(createNodeClickLink("iconLink", model, tree));
		add(createNodeSelectLink("selectNode", model, tree));
		add(createSelectCheckBox("select", model, tree));
	}

	@SuppressWarnings("serial")
	private MarkupContainer createSelectCheckBox(String id, final IModel<Object> model, final BaseTree tree)
	{
		AssigneeTreeNode<?> node = (AssigneeTreeNode<?>)model.getObject();
		
		Fragment fragment;
		
		if (node.isSelectable())
		{
			fragment = new Fragment(id, "selectable", this);
			
			AjaxCheckBox checkBox = new AjaxCheckBox("selected", new PropertyModel<Boolean>(model, "selected"))
			{
				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					
				}
			};
			
			fragment.add(checkBox);
		}
		else
		{
			fragment = new Fragment(id, "notSelectable", this);
		}
		
		return fragment;
	}
	
	@SuppressWarnings("serial")
	private MarkupContainer createNodeSelectLink(String id, final IModel<Object> model, final BaseTree tree)
	{
		MarkupContainer link;
		BaseTree.ILinkCallback callBackLinkSelected = new BaseTree.ILinkCallback()
		{
			public void onClick(AjaxRequestTarget target)
			{
				onNodeLinkSelected(model.getObject(), tree, target);
			}
		};
		
		link = tree.newLink(id, callBackLinkSelected);
		link.add(newContentComponent("content", tree, model));
		return link;
	}

	@SuppressWarnings("serial")
	private MarkupContainer createNodeClickLink(String id, final IModel<Object> model, final BaseTree tree)
	{
		BaseTree.ILinkCallback callBackLinkClick = new BaseTree.ILinkCallback()
		{
			public void onClick(AjaxRequestTarget target)
			{
				onNodeLinkClicked(model.getObject(), tree, target);
			}
		};

		MarkupContainer link = tree.newLink(id, callBackLinkClick);
		link.add(newImageComponent("icon", tree, model));
		return link;
	}

	/**
	 * Handler invoked when the link is clicked. By default makes the node selected
	 * 
	 * @param node
	 * @param tree
	 * @param target
	 */
	protected void onNodeLinkClicked(Object node, BaseTree tree, AjaxRequestTarget target)
	{
		tree.getTreeState().selectNode(node, !tree.getTreeState().isNodeSelected(node));
		tree.updateTree(target);
	}

	/**
	 * Callback when a node is selected (the text itself is clicked)
	 * @param node
	 * @param tree
	 * @param target
	 */
	protected void onNodeLinkSelected(Object node, BaseTree tree, AjaxRequestTarget target)
	{
		onNodeLinkClicked(node, tree, target);
	}
}
