package com.app.cliente.adm.identificador;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;

import br.app.servico.infra.integracao.dto.TipoIdentificadorDTO;



@Named(value = "tipoIdentificadorConverter")
@RequestScoped
public class TipoIdentificadorConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (context == null) {
			throw new NullPointerException("context");
		}
		if (component == null) {
			throw new NullPointerException("component");
		}

		TipoIdentificadorDTO tipoIdentificador = null;
		if (value != null && !value.equalsIgnoreCase("") && value.trim().length() > 0) {
			tipoIdentificador = TipoIdentificadorDTO.get(Integer.valueOf(value));
			if (tipoIdentificador == null) {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unknown value",
						"The car is unknown!");
				throw new ConverterException(message);
			}
		}
		return tipoIdentificador;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (context == null) {
			throw new NullPointerException("context");
		}
		if (component == null) {
			throw new NullPointerException("component");
		}
		if (obj instanceof TipoIdentificadorDTO) {
			return "" + ((TipoIdentificadorDTO) obj).getValue();
		} else {
			return "" + TipoIdentificadorDTO.INDEFINIDO.getValue();
		}
	}

}
