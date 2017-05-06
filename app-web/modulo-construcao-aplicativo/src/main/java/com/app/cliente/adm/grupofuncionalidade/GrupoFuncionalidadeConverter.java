package com.app.cliente.adm.grupofuncionalidade;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

import br.app.servico.infra.integracao.dto.GrupoFuncionalidadeDTO;


@Named("grupoFuncionalidadeConverter")
@RequestScoped
public class GrupoFuncionalidadeConverter implements Converter {

	@Inject
	private RepositorioGrupoFuncionalidade repositorioGrupoFuncionalidade;

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

				GrupoFuncionalidadeDTO dto = repositorioGrupoFuncionalidade.get(value);
				return dto;
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

		if (object instanceof GrupoFuncionalidadeDTO) {

			GrupoFuncionalidadeDTO p = ((GrupoFuncionalidadeDTO) object);

			return String.valueOf(p.getId() != null ? p.getId().toString() : "0");
		}

		return null;
	}
}
