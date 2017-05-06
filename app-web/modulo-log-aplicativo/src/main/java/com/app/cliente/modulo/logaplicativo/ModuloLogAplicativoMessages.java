package com.app.cliente.modulo.logaplicativo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import br.app.modulo.infra.api.ResourceBundleMessages;



/**
 * Module specific extension of the {@link ResourceBundleMessages}.
 */
@ApplicationScoped
@Named
public class ModuloLogAplicativoMessages extends ResourceBundleMessages
{
	//~ Methods ----------------------------------------------------------------

	@Override
	protected String getBundleName()
	{
		return "logaplicativo";
	}
}
