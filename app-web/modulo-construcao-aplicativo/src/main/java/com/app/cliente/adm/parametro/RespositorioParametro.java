package com.app.cliente.adm.parametro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;

import com.app.cliente.controle.sessao.SessaoAutenticada;

import br.app.barramento.integracao.dto.EnvioDTO;
import br.app.barramento.integracao.dto.Mensagem;
import br.app.barramento.integracao.dto.RespostaDTO;
import br.app.barramento.integracao.dto.TipoAcao;
import br.app.barramento.integracao.exception.InfraEstruturaException;
import br.app.barramento.integracao.exception.NegocioException;
import br.app.modulo.aplicacao.sessao.SessaoAplicacaoAutenticada;
import br.app.servico.infra.integracao.dto.ParametroDTO;
import br.app.servico.infra.integracao.dto.TipoParametroDTO;

@ApplicationScoped
@Named(value = "respositorioParametro")
public class RespositorioParametro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	private List<ParametroDTO> parametroRepositorio;

	@Inject
	private SessaoAplicacaoAutenticada sessaoAplicacaoAutenticada;

	private List<SelectItem> listaTipoParametros;

	@PostConstruct
	public void init() {

		try {

			buscaTodosParametrosServico();
			for (TipoParametroDTO tipo : TipoParametroDTO.values()) {
				if (listaTipoParametros == null) {
					listaTipoParametros = new ArrayList<SelectItem>();
				}

				listaTipoParametros.add(new SelectItem(tipo, tipo.getTexto()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info(e.getMessage());
		}

	}
	
	public void onParametroListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final ParametroDTO parametro) {
		buscaTodosParametrosServico();
	}
	

	public void buscaTodosParametrosServico() {
		try {

			EnvioDTO envio = new EnvioDTO();

			envio.setEnvio(ParametroDTO.class.getName());

			RespostaDTO respostaDTO = sessaoAplicacaoAutenticada.getConexao().executar(TipoAcao.BUSCAR_TODOS, envio,
					"REPOSITORIO_CORPORATIVO", "CATALOGO_PMT");
			if (respostaDTO != null) {
				log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
				log.info("Mensagem" + respostaDTO.getMensagem().getErro());
				if (respostaDTO.getMensagem() == Mensagem.SUCESSO) {
					parametroRepositorio = (List<ParametroDTO>) respostaDTO.getListaResultado();
				}
			}

		} catch (InfraEstruturaException | NegocioException e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}



	private String getRootErrorMessage(Exception e) {
		// Default to general error message that registration failed.
		String errorMessage = "Algo nao funcionou bem. Para maiores informacoes consultar o log";
		if (e == null) {
			// This shouldn't happen, but return the default messages
			return errorMessage;
		}

		// Start with the exception and recurse to find the root cause
		Throwable t = e;
		while (t != null) {
			// Get the message from the Throwable class instance
			errorMessage = t.getLocalizedMessage();
			t = t.getCause();
		}
		// This is the root cause message
		return errorMessage;
	}

	public List<SelectItem> getListaTipoParametros() {

		return listaTipoParametros;
	}

	public void onRowEdit(RowEditEvent event) {

		Object obj = event.getObject();
		System.out.println(obj);
		FacesMessage msg = new FacesMessage("Parametro Editado", "detalhe");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancel(RowEditEvent event) {
		Object obj = event.getObject();
		FacesMessage msg = new FacesMessage("Parametro Cancelado", "detalhe");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed",
					"Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void setListaTipoParametros(List<SelectItem> listaTipoParametros) {
		this.listaTipoParametros = listaTipoParametros;
	}

	public List<ParametroDTO> getParametroRepositorio() {
		return parametroRepositorio;
	}

	public void setParametroRepositorio(List<ParametroDTO> parametroRepositorio) {
		this.parametroRepositorio = parametroRepositorio;
	}
	

}
