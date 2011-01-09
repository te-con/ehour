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

wicket.guardform.prompt = "You have changed values in the form.\n"
        + "Do you really want to edit and lose your changes?";

wicket.guardform.init = function(id)
{
    wicket.guardform.formId = id;
    wicket.guardform.serializedForm = Wicket.Form.serialize(document.getElementById(id));
};

wicket.guardform.promptForDirtyForm = function()
{
    var postClick = Wicket.Form.serialize(document.getElementById(wicket.guardform.formId));


    if (postClick != wicket.guardform.serializedForm) {
        return confirm("Data was entered without saving; do you want to leave the page - killing data?");
    }

    return true;
}

