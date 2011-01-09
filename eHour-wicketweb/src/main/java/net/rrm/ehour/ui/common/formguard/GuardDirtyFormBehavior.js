// initialize namespace
if (!window.wicket)
{
    window.wicket = {};
}

if (!wicket.guardform)
{
    wicket.guardform = {};
}

wicket.guardform.serializedForm = "";
wicket.guardform.formId = "";

wicket.guardform.init = function(id)
{
    wicket.guardform.formId = id;
    wicket.guardform.clean();

    window.onbeforeunload = function()
    {
        if (wicket.guardform.isDirty())
        {
            return decodeURIComponent(wicket.guardform.prompt);
        } else {
            return;
        }
    }

};

wicket.guardform.promptForDirtyForm = function()
{
    if (wicket.guardform.isDirty())
    {
        return confirm(decodeURIComponent(wicket.guardform.prompt));
    }

    return true;
}

wicket.guardform.isDirty = function()
{
    var postClick = Wicket.Form.serialize(document.getElementById(wicket.guardform.formId));

    return postClick != wicket.guardform.serializedForm;
}

wicket.guardform.clean = function()
{
    wicket.guardform.serializedForm = Wicket.Form.serialize(document.getElementById(wicket.guardform.formId));
}

