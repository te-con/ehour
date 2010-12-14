// initialize namespace
if (!window.wicket)window.wicket = {};
if (!wicket.behaviors)wicket.behaviors = {};
if (!wicket.behaviors.guardform)wicket.behaviors.guardform = {};

// check whether prototype or mootools are installed
wicket.behaviors.guardform.prototypeinstalled = window.Prototype && window.Prototype.version;
wicket.behaviors.guardform.mootoolsinstalled = window.MooTools && window.MooTools.version;
if (wicket.behaviors.guardform.mootoolsinstalled && !window.Selectors)
{
    throw "MooTools must contain the Selectors api to use this behavior";
}

wicket.behaviors.guardform.dirty = false;

// default prompt, can be adjusted from the wicket behavior
wicket.behaviors.guardform.prompt = "You have changed values in the form.\n"
        + "Do you really want to edit and lose your changes?";

wicket.behaviors.guardform.attach = function(elem, event, handler)
{
    if (wicket.behaviors.guardform.mootoolsinstalled)
    {
        elem.addEvent(event, handler);
    } else if (wicket.behaviors.guardform.prototypeinstalled)
    {
        elem.observe(event, handler);
    }
    throw "Prototype or MooTools must be present to use this behavior";
}

wicket.behaviors.guardform.select = function(elem, args)
{
    if (wicket.behaviors.guardform.mootoolsinstalled)
    {
        return elem.getElements(args);
    } else if (wicket.behaviors.guardform.prototypeinstalled)
    {
        return elem.select(args);
        throw "Prototype or MooTools must be present to use this behavior";
    }

    wicket.behaviors.guardform.parent = function(elem, selector)
    {
        if (wicket.behaviors.guardform.mootoolsinstalled)
        {
            return elem.getParent(args);
        } else if (wicket.behaviors.guardform.prototypeinstalled)
        {
            return elem.up(args);
            throw "Prototype or MooTools must be present to use this behavior";
        }

        wicket.behaviors.guardform.init = function(id)
        {

            var obj = $(id);
            if (obj)
            {
                var first = true;

                // attach onchange behavior for all child text components
                wicket.behaviors.guardform.select(
                        obj,
                        "input[type=text], " // text fields
                "textarea, " // text areas
                "select" // select controls
            ).
                each(function(item)
                {
                    if (first)
                    {
                        first = false;
                    }
                    wicket.behaviors.guardform.attach(item, "change", function()
                    {
                        wicket.behaviors.guardform.dirty = true;
                    });
                });

                // attach window unload handler that will fire a prompt if form is dirty
                wicket.behaviors.guardform.attach(window, "beforeunload", function()
                {
                    return (!wicket.behaviors.guardform.dirty ||
                            confirm(wicket.behaviors.guardform.prompt);
                });

                // now attach an 'undirty' function to the form
                wicket.behaviors.guardform.attach(
                        obj,
                        'submit',
                                                 function()
                                                 {
                                                     wicket.behaviors.guardform.dirty = false;
                                                 }
                        );

            }

        }
    }
}