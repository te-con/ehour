package net.rrm.ehour.ui.admin.content.assignables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.ui.admin.content.tree.AssigneeTreeNode;
import net.rrm.ehour.ui.admin.content.tree.ContentTree;
import net.rrm.ehour.ui.admin.content.tree.NodeType;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;

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
	
	@SpringBean
	private ProjectService projectService;
	
	public AssignablesPanel(String id)
	{
		super(id);
		
		TreeModel model = createTreeModel();
		
		ContentTree tree = new ContentTree("tree", model);
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

	@SuppressWarnings("serial")
	private void addNodesToRoot(MutableTreeNode rootNode)
	{
		List<Customer> customers = customerService.getCustomers();
		
		for (Customer customer : customers)
		{
			AssigneeTreeNode<Customer> customerNode = new AssigneeTreeNode<Customer>(customer, NodeType.CUSTOMER);
			
			rootNode.insert(customerNode, 0);
			
			customer.getProjects();
			
			List<Project> projects = new ArrayList<Project>(customer.getProjects());
			Collections.sort(projects);
			
			for (Project project : projects)
			{
				AssigneeTreeNode<Project> projectNode = new AssigneeTreeNode<Project>(project, NodeType.CUSTOMER)
				{
					@Override
					public Set<?> getSelectedNodeObjects()
					{
						Integer projectId = ((Project)getUserObject()).getPK();
						
						try
						{
							Project project = projectService.getProject(projectId);
							return project.getProjectAssignments();
							
						} catch (ObjectNotFoundException e)
						{
							e.printStackTrace();
						}
						
						return super.getSelectedNodeObjects();
					}
				};
				
				customerNode.add(projectNode);
			}
		}
	}	
}
