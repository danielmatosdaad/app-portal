package com.app.cliente.adm.identificador;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

import br.app.servico.infra.integracao.dto.IdentificadorDTO;


@Named(value = "identificadorDtoConverter")
@RequestScoped
public class IdentificadorDtoConverter implements Converter {
	
	@Inject
	private RepositorioIdentificador repositorioIdentificador;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (context == null) {
			throw new NullPointerException("context");
		}
		if (component == null) {
			throw new NullPointerException("component");
		}

		if (value != null && !value.equalsIgnoreCase("") && value.trim().length() > 0) {

			List<IdentificadorDTO> identificadores = repositorioIdentificador.getListaIdentificador();
			for (IdentificadorDTO identificadorDTO : identificadores) {

				if (identificadorDTO.getValor().equals(value)) {

					return identificadorDTO;
				}
			}
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unknown value",
					"The car is unknown!");
			throw new ConverterException(message);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object obj) {
		if (context == null) {
			throw new NullPointerException("context");
		}
		if (component == null) {
			throw new NullPointerException("component");
		}
		if (obj instanceof IdentificadorDTO) {
			return "" + ((IdentificadorDTO) obj).getValor();
		} else {
			return "Indefinido";
		}
	}

}
