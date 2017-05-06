package com.app.cliente.modulo.construcaoaplicativo;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import br.app.modulo.infra.api.ResourceBundleMessages;



/**
 * Module specific extension of the {@link ResourceBundleMessages}.
 */
@ApplicationScoped
@Named
public class ModuloConstruncaoAplicativoMessages extends ResourceBundleMessages
{
	//~ Methods ----------------------------------------------------------------

	@Override
	protected String getBundleName()
	{
		return "construcaoaplicativo";
	}
}
