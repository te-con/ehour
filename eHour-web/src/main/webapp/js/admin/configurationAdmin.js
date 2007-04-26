var inSubmit = false;

function initConfigAdmin()
{
	dojo.event.connect(dojo.byId('showTranslationsOnlyId'), "onclick", "updateLocales");
	dojo.event.connect(dojo.byId('noForce'), "onclick", "forceLocale");
	forceLocale(null);

	new dojo.io.FormBind({formNode: dojo.byId('configForm'),
	  						handler: localesReceived
						});
}

// prevent doubleclick (..)
dojo.lang.extend(dojo.io.FormBind, {onSubmit: function(/*DOMNode*/form)
									{
										if (inSubmit)
										{
											return false;
										}
	
										inSubmit = true;
										showLoadingData();
										return true;
									}});


// use browsers locale or force one
function forceLocale(evt)
{
	var noForce = dojo.byId('noForce').checked;

	dojo.byId('showTranslationsOnlyId').disabled = noForce;
	dojo.byId('languageId').disabled = noForce;
}

// update locales (show all or just translated
function updateLocales(evt)
{
	inSubmit = false;
	dojo.io.bind({url: 'updateLocales.do',
				  handler: localesReceived,
                  content: {showTranslationsOnly: dojo.byId('showTranslationsOnlyId').checked,
                  			noForce: dojo.byId('noForce').checked,
                  			currency: dojo.byId('currency').value,
                  			showTurnOver: dojo.byId('showTurnOver').checked,		
                  			fromForm: 'true'
                   			}
                    });		
}

//
function localesReceived(type, xml, evt)
{
	ajaxEventReceived(xml, true, {locale: "localeSpan"});
	initConfigAdmin();
	inSubmit = false;
	hideLoadingData();
}

dojo.addOnLoad(initConfigAdmin);