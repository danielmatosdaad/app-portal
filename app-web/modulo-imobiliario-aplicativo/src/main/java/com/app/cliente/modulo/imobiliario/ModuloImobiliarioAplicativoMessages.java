package com.app.cliente.modulo.imobiliario;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import br.app.modulo.infra.api.ResourceBundleMessages;



/**
 * Module specific extension of the {@link ResourceBundleMessages}.
 */
@ApplicationScoped
@Named
public class ModuloImobiliarioAplicativoMessages extends ResourceBundleMessages
{
	//~ Methods ----------------------------------------------------------------

	@Override
	protected String getBundleName()
	{
		return "imobiliarioaplicativo";
	}
}
