package net.rrm.ehour.ui.admin.content.assignables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.ui.admin.content.assignees.tree.AssigneeTreeNode;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;

import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created on Feb 7, 2010 4:45:13 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class AssignablesPanel extends AbstractAjaxPanel<Void>
{
	private static final long serialVersionUID = -7159173007695522322L;

	@SpringBean
	private CustomerService customerService;
	
	public AssignablesPanel(String id)
	{
		super(id);
		
		TreeModel model = createTreeModel();
		
		LinkTree tree = new LinkTree("tree", model);
		tree.setRootLess(true);
		add(tree);
		
	}
	
	private TreeModel createTreeModel()
	{
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
		
		addNodesToRoot(rootNode);
		
		DefaultTreeModel model = new DefaultTreeModel(rootNode);
		return model;
	}

	private void addNodesToRoot(MutableTreeNode rootNode)
	{
		List<Customer> customers = customerService.getCustomers();
		
		for (Customer customer : customers)
		{
			AssigneeTreeNode<Customer> customerNode = new AssigneeTreeNode<Customer>(customer);
			
			rootNode.insert(customerNode, 0);
			
			customer.getProjects();
			
			List<Project> projects = new ArrayList<Project>(customer.getProjects());
			Collections.sort(projects);
			
			for (Project project : projects)
			{
				AssigneeTreeNode<Project> projectNode = new AssigneeTreeNode<Project>(project);
				customerNode.add(projectNode);
			}
		}
	}	
}
