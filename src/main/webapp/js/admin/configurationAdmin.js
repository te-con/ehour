function initConfigAdmin()
{
	dojo.event.connect(dojo.byId('showTranslationsOnly'), "onclick", "updateLocales");
}

function updateLocales(evt)
{
	dojo.io.bind({url: 'updateLocales.do',
				  handler: localesReceived,
                  content: {showTranslationsOnly: dojo.byId('showTranslationsOnly').checked
                   			}
                    });		
}

function localesReceived(type, xml, evt)
{
	ajaxEventReceived(xml, true, {locale: "localeSpan"});
}

dojo.addOnLoad(initConfigAdmin);