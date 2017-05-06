package com.app.cliente.adm.identificador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
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
import br.app.servico.infra.integracao.dto.IdentificadorDTO;
import br.app.servico.infra.integracao.dto.TipoIdentificadorDTO;

@Model
public class IndentificadoresController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private Event<IdentificadorDTO> identificadorEventSrc;

	@Produces
	@Named
	private IdentificadorDTO identificadorDTO;

	private List<IdentificadorDTO> listaIdentificador;

	@Inject
	private SessaoAutenticada sessaoAutenticada;

	@PostConstruct
	public void initIdentificadorDTO() {

		this.identificadorDTO = criarIdentificador();
	}

	private IdentificadorDTO criarIdentificador() {
		return new IdentificadorDTO();
	}

	public void registrar() throws Exception {
		try {

			if (identificadorDTO != null) {
				log.info("Reginstrando Identificador");
				identificadorDTO.setDataInclusao(new Date());

				EnvioDTO envio = new EnvioDTO();
				envio.setRequisicao(identificadorDTO);
				envio.setEnvio(identificadorDTO.getClass().getName());
				RespostaDTO respostaDTO = sessaoAutenticada.getConexao().executar(TipoAcao.SALVAR, envio,
						"REPOSITORIO_CORPORATIVO", "CATALOGO_FNC");
				if (respostaDTO != null) {
					log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
					log.info("Mensagem" + respostaDTO.getMensagem().getErro());
					if (respostaDTO.getMensagem().equals(Mensagem.SUCESSO)) {
						log.info("Reginstrado com sucesso - id:" + respostaDTO.getResultado().getId());
						facesContext.addMessage(null, new FacesMessage("Sucesso",  "Transacao realizada com sucesso") );
						identificadorEventSrc.fire(identificadorDTO);
					} else {
						FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Requisicao nao atendida",
								"Tivemos problemas em executar a transacao, tente novamente mais tarde");
						facesContext.addMessage(null, m);

					}

				}

			}

		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Nao foi posssivel registrar");
			facesContext.addMessage(null, m);
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

	public List getListaTipoIdentificadores() {
		List items = new ArrayList();
		for (TipoIdentificadorDTO tipo : TipoIdentificadorDTO.values()) {
			items.add(new SelectItem(tipo, tipo.getTexto()));
		}
		return items;
	}

	public List<IdentificadorDTO> getListaIdentificador() {
		return listaIdentificador;
	}

	public void setListaIdentificador(List<IdentificadorDTO> listaIdentificador) {
		this.listaIdentificador = listaIdentificador;
	}

	public void onRowEdit(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Identificador Edited", "y");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edit Cancelled", "x");
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

	public IdentificadorDTO getIdentificadorDTO() {
		return identificadorDTO;
	}

	public void setIdentificadorDTO(IdentificadorDTO identificadorDTO) {
		this.identificadorDTO = identificadorDTO;
	}

}
