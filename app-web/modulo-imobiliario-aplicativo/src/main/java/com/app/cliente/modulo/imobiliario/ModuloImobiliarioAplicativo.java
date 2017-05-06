package com.app.cliente.modulo.imobiliario;

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
public class ModuloImobiliarioAplicativo extends AbstractModuleDescription implements Serializable {
	// ~ Instance fields
	// --------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private MessagesProxy msgs;

	// ~ Methods
	// ----------------------------------------------------------------

	@Override
	public String getName() {
		try {
			return msgs.get("imobiliariaaplicativo.modName");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "?";
	}

	@Override
	public String getDescription() {

		try {
			return msgs.get("imobiliariaaplicativo.modDesc");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "?";
	}

	@Override
	public String getPrefix() {
		return "imobiliarioaplicativo";
	}

	@Override
	public String getUrl() {
		
		return "/imobiliarioaplicativo/views/index.html";
	}
}
