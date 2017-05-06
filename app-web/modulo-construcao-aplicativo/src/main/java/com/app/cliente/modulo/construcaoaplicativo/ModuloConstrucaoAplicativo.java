package com.app.cliente.modulo.construcaoaplicativo;

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
public class ModuloConstrucaoAplicativo extends AbstractModuleDescription implements Serializable
{
	//~ Instance fields --------------------------------------------------------

	@Inject
	private MessagesProxy msgs;

	//~ Methods ----------------------------------------------------------------

	@Override
	public String getName()
	{
		return msgs.get("construcaoaplicativo.modName");
	}

	@Override
	public String getDescription()
	{
		return msgs.get("construcaoaplicativo.modDesc");
	}

	@Override
	public String getPrefix()
	{
		return "construcaoaplicativo";
	}

	@Override
	public String getUrl()
	{
		return "/adm/home.xhtml";
	}
}
