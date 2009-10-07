package net.rrm.ehour.ui.common.component;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;

public class EditableDateLabel extends AjaxEditableLabel
{
	private static final long serialVersionUID = -4192780218559385857L;

	private IModel infiniteModel;
	
	public EditableDateLabel(String id, IModel dateModel, IModel infiniteModel)
	{
		super(id, dateModel);
		
		this.infiniteModel = infiniteModel;
	}
	
//	@Override
//	protected FormComponent newEditor(MarkupContainer parent, String componentId, IModel model)
//	{
//		TextField editor = new TextField(componentId, model)
//		{
//			private static final long serialVersionUID = 1L;
//
//			public IConverter getConverter(Class type)
//			{
//				IConverter c = AjaxEditableLabel.this.getConverter(type);
//				return c != null ? c : super.getConverter(type);
//			}
//
//			protected void onModelChanged()
//			{
//				super.onModelChanged();
//				AjaxEditableLabel.this.onModelChanged();
//			}
//
//			protected void onModelChanging()
//			{
//				super.onModelChanging();
//				AjaxEditableLabel.this.onModelChanging();
//			}
//		};
//		editor.setOutputMarkupId(true);
//		editor.setVisible(false);
//		editor.add(new EditorAjaxBehavior());
//		return editor;
//	}	

}
