package com.app.cliente.adm.perfil;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

import br.app.servico.infra.integracao.dto.PerfilDTO;


@Named(value="perfilConverter")
@RequestScoped
public class PerfilConverter implements Converter {

	@Inject
	private RepositorioPerfil repPerfil;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (context == null) {
			throw new NullPointerException("context");
		}
		if (component == null) {
			throw new NullPointerException("component");
		}

		if (value != null && value.trim().length() > 0) {
			try {

				PerfilDTO perfilSelecionado2 = repPerfil.get(value);
				return perfilSelecionado2;
			} catch (NumberFormatException e) {
				throw new ConverterException(
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
			}
		} else {
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
		if (context == null) {
			throw new NullPointerException("context");
		}
		if (component == null) {
			throw new NullPointerException("component");
		}

		if (object instanceof PerfilDTO) {

			PerfilDTO p = ((PerfilDTO) object);

			return String.valueOf(p.getId() != null ? p.getId() : 0);
		}

		return null;
	}
}
