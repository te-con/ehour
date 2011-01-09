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
    wicket.guardform.serializedForm = Wicket.Form.serialize(document.getElementById(id));
};

wicket.guardform.promptForDirtyForm = function()
{
    var postClick = Wicket.Form.serialize(document.getElementById(wicket.guardform.formId));

    if (postClick != wicket.guardform.serializedForm) {
        return confirm(decodeURIComponent(wicket.guardform.prompt));
    }

    return true;
}

