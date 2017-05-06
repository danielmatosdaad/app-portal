package com.app.cliente.modulo.monitoracaoaplicativo;

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
public class ModuloMonitoracaoAplicativo extends AbstractModuleDescription implements Serializable
{
	//~ Instance fields --------------------------------------------------------

	@Inject
	private MessagesProxy msgs;

	//~ Methods ----------------------------------------------------------------

	@Override
	public String getName()
	{
		return msgs.get("monitoracaoaplicativo.modName");
	}

	@Override
	public String getDescription()
	{
		return msgs.get("monitoracaoaplicativo.modDesc");
	}

	@Override
	public String getPrefix()
	{
		return "monitoracaoaplicativo";
	}

	@Override
	public String getUrl()
	{
		return "/monitoracaoaplicativo/views/modulo_monitoracao_aplicativo.xhtml";
	}
}
