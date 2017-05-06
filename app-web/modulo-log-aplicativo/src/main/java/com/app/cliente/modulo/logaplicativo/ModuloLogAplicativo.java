package com.app.cliente.modulo.logaplicativo;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.app.modulo.infra.api.AbstractModuleDescription;
import br.app.modulo.infra.bean.MessagesProxy;



/**
 * Module specific implementation of the {@link ModuleDescription}.
 */
@ApplicationScoped
@Named
public class ModuloLogAplicativo extends AbstractModuleDescription implements Serializable
{
	//~ Instance fields --------------------------------------------------------

	@Inject
	private MessagesProxy msgs;

	//~ Methods ----------------------------------------------------------------

	@Override
	public String getName()
	{
		return msgs.get("logaplicativo.modName");
	}

	@Override
	public String getDescription()
	{
		return msgs.get("logaplicativo.modDesc");
	}

	@Override
	public String getPrefix()
	{
		return "logaplicativo";
	}

	@Override
	public String getUrl()
	{
		return "/logaplicativo/views/modulo_log_aplicativo.xhtml";
	}
}
