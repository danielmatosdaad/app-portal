package com.app.cliente.adm.funcionalidade;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

import br.app.servico.infra.integracao.dto.FuncionalidadeDTO;


@Named("funcionalidadeConverter")
@RequestScoped
public class FuncionalidadeConverter implements Converter {

	@Inject
	private RepositorioFuncionalidade repositorioFuncionalidade;

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

				FuncionalidadeDTO FuncionalidadeSelecionado2 = repositorioFuncionalidade.get(value);
				return FuncionalidadeSelecionado2;
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

		if (object instanceof FuncionalidadeDTO) {

			FuncionalidadeDTO p = ((FuncionalidadeDTO) object);

			return String.valueOf(p.getId() != null ? p.getId() : 0);
		}

		return null;
	}
}
