package net.rrm.ehour.ui.admin.content.tree;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LabelIconPanel;
import org.apache.wicket.model.IModel;

/**
 * Created on Feb 9, 2010 1:29:30 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class ContentNodePanel extends LabelIconPanel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs the panel.
	 * 
	 * @param id
	 *            component id
	 * @param model
	 *            model that is used to access the TreeNode
	 * @param tree
	 */
	public ContentNodePanel(String id, IModel<Object> model, BaseTree tree)
	{
		super(id, model, tree);
	}

	@SuppressWarnings("serial")
	@Override
	protected void addComponents(final IModel<Object> model, final BaseTree tree)
	{
		BaseTree.ILinkCallback callBackLinkClick = new BaseTree.ILinkCallback()
		{
			public void onClick(AjaxRequestTarget target)
			{
				onNodeLinkClicked(model.getObject(), tree, target);
			}
		};

		MarkupContainer link = tree.newLink("iconLink", callBackLinkClick);
		add(link);
		link.add(newImageComponent("icon", tree, model));

		
		BaseTree.ILinkCallback callBackLinkSelected = new BaseTree.ILinkCallback()
		{
			public void onClick(AjaxRequestTarget target)
			{
				onNodeLinkSelected(model.getObject(), tree, target);
			}
		};
		
		link = tree.newLink("selectNode", callBackLinkSelected);
		add(link);
		link.add(newContentComponent("content", tree, model));
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
