package br.app.modulo.infra.common;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import br.app.modulo.infra.api.ResourceBundleMessages;

/**
 * Module specific extension of the {@link ova.api.ResourceBundleMessages}.
 */
@ApplicationScoped
@Named
public class JsfToolkitMessages extends ResourceBundleMessages
{
	//~ Methods ----------------------------------------------------------------

	@Override
	protected String getBundleName()
	{
		return "jsftoolkit";
	}
}
