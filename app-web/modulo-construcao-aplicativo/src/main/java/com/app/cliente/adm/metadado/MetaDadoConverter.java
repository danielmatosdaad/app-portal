package com.app.cliente.adm.metadado;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

import br.app.servico.infra.integracao.dto.MetaDadoDTO;


@Named("metaDadoConverter")
@RequestScoped
public class MetaDadoConverter implements Converter {

	@Inject
	private RepositorioMetaDado repositorioMetaDado;

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

				MetaDadoDTO dto = repositorioMetaDado.get(value);
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

		if (object instanceof MetaDadoDTO) {

			MetaDadoDTO p = ((MetaDadoDTO) object);

			return String.valueOf(p.getId() != null ? p.getId() : 0);
		}

		return null;
	}
}
