package com.app.cliente.modulo.manutencaoaplicativo;

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
public class ModuloManutencaoAplicativo extends AbstractModuleDescription implements Serializable
{
	//~ Instance fields --------------------------------------------------------

	@Inject
	private MessagesProxy msgs;

	//~ Methods ----------------------------------------------------------------

	@Override
	public String getName()
	{
		return msgs.get("manutencaoaplicativo.modName");
	}

	@Override
	public String getDescription()
	{
		return msgs.get("manutencaoaplicativo.modDesc");
	}

	@Override
	public String getPrefix()
	{
		return "manutencaoaplicativo";
	}

	@Override
	public String getUrl()
	{
		return "/manutencaoaplicativo/views/modulo_manutencao_aplicativo.xhtml";
	}
}
