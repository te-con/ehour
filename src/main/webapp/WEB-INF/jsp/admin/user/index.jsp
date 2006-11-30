<%@ page contentType="text/html; charset=ASCII"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
	
	function bindSubmitForm()
	{ 
		new dojo.io.FormBind({
    						formNode: dojo.byId('bindSubmitForm'),
    						handler: userListReceived
							});
	}
	
	function userListReceivedFromFilter(type, xml, evt)
	{
		if (type == 'error')
		{	
    		alert("<fmt:message key="errors.ajax.general" />");
		}
		else
		{
			dojo.byId('listUsersSpan').innerHTML = xml;
			dojo.byId('filterInput').focus();

		}
	}

	function userListReceived(type, xml, evt)
	{
		userListReceivedFromFilter(type, xml, evt);
		
		showAddForm();
	}


	function editUser(editId)
	{
        dojo.io.bind({
                       url: 'getUser.do',
                       handler: formChanged,
                       content: {userId: editId}
                    });  
                    
		return false;    
	}
	
	function showAddForm()
	{
        dojo.io.bind({
                       url: 'addUserForm.do',
                       handler: formChanged
                    });  
                    
		return false;    
	}		
		
    function formChanged(type, xml, evt)
    {
    	if (type == 'error')
    	{
    		alert("<fmt:message key="errors.ajax.general" />");
    		return;
    	}
    	else
    	{
			dojo.byId('userFormSpan').innerHTML = xml;

			dojo.byId("filterForm").value = dojo.byId('filterInput').value;
			dojo.byId("inActiveForm").value = dojo.byId('hideInactive').checked;
			
			// rebind
			bindSubmitForm();
		}
    }

	function init()
	{
		dojo.event.connect(dojo.byId('filterInput'), "onkeyup", "filterKeyUp");
		dojo.event.connect(dojo.byId('hideInactive'), "onclick", "filterKeyUp");
		bindSubmitForm();
	}
	
	function filterKeyUp(evt)
	{
		var filterInput = dojo.byId('filterInput').value;
		
		dojo.byId("filterForm").value = dojo.byId('filterInput').value;
		dojo.byId("inActiveForm").value = dojo.byId('hideInactive').checked;
		
		dojo.io.bind({url: 'index.do',
					  handler: userListReceivedFromFilter,
                      content: {filterPattern: filterInput,
                      			hideInactive: dojo.byId('hideInactive').checked,
                      			fromForm: '1'}
                      });		
	}

	dojo.addOnLoad(init);
</script>

<table CLASS="contentTable" CELLSPACING=2>
	<tr>
		<td>
			<table CLASS="contentTable" CELLSPACING=2 width="100%">
				<tr>
					<Td>&nbsp;</Td>					<Td>&nbsp;</Td>					<Td>&nbsp;</Td>
				</tr>
				<tr>
					<td valign="top"><fmt:message key="admin.user.filter" />:</td>
					<td><form><input class="normtxt" type="text" name="filter"
								size="30" id="filterInput"></form>
					</td>
				</tr>

				<tr>
					<td colspan="2" valign="top"><fmt:message key="admin.user.hideInactive" />:
					<input class="normtxt" type="checkbox" id="hideInactive" name="hideInactive" checked></td>
				</tr>
			</table>
		</td>
		
		<td>
			&nbsp;
		</td>
	</tr>
	
	<tr>
		<td>
			<div class="userScroll">
				<span id="listUsersSpan">
					<tiles:insert page="listUsers.jsp" />
				</span>
			</div>
		</td>
		
		<td valign="top" rowspan="2">
			<span id="userFormSpan">
				<tiles:insert page="/eh/admin/user/addUserForm.do" />
			</span>
		</td>
	</tr>
	<tr>
		<td align=right>
			<a href="" onClick="return showAddForm()"><fmt:message key="admin.user.addUser" /></a>
		</td>
	</tr>
</table>
