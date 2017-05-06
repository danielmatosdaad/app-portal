package com.app.cliente.adm.perfil;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.app.cliente.controle.sessao.SessaoAutenticada;
import com.app.cliente.resource.util.MensagemUtil;

import br.app.barramento.integracao.dto.EnvioDTO;
import br.app.barramento.integracao.dto.Mensagem;
import br.app.barramento.integracao.dto.RespostaDTO;
import br.app.barramento.integracao.dto.TipoAcao;
import br.app.servico.infra.integracao.dto.PerfilDTO;

@Model
public class PerfilController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private Event<PerfilDTO> perfilEventSrc;

	@Produces
	@Named
	private PerfilDTO perfilPaiDTO;

	@Produces
	@Named
	private PerfilDTO perfilDTO;

	@Inject
	private SessaoAutenticada sessaoAutenticada;

	@PostConstruct
	public void init() {

		this.perfilDTO = criarPerfil();
		this.perfilPaiDTO = criarPerfil();
	}

	private PerfilDTO criarPerfil() {
		return new PerfilDTO();
	}

	public void registrar() throws Exception {
		try {

			if (sessaoAutenticada == null) {
				FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sessao indisponivel!",
						"Sessao indisponivel!");
				facesContext.addMessage(null, m);
				return;
			}

			if (perfilDTO != null) {
				log.info("Reginstrando perfil");
				if (this.perfilPaiDTO != null && this.perfilPaiDTO.getId() != null) {
					perfilDTO.setPerfilPai(this.perfilPaiDTO);
				} else {

					perfilDTO.setPerfilPai(null);
				}
				EnvioDTO envio = new EnvioDTO();
				envio.setRequisicao(perfilDTO);
				envio.setEnvio(perfilDTO.getClass().getName());
				RespostaDTO respostaDTO = sessaoAutenticada.getConexao().executar(TipoAcao.SALVAR, envio,
						"REPOSITORIO_CORPORATIVO", "CATALOGO_FNC");
				if (respostaDTO != null) {
					log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
					log.info("Mensagem" + respostaDTO.getMensagem().getErro());
					if (respostaDTO.getMensagem().equals(Mensagem.SUCESSO)) {
						log.info("Reginstrado com sucesso - id:" + respostaDTO.getResultado().getId());
						log.info("Reginstrado com sucesso - id:" + perfilDTO.getId());
						facesContext.addMessage(null, new FacesMessage("Sucesso", "Transacao realizada com sucesso"));
						perfilEventSrc.fire(perfilDTO);
					} else {
						MensagemUtil.addMessage(respostaDTO.getMensagem().getErro());
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

	public PerfilDTO getPerfilPaiDTO() {
		return perfilPaiDTO;
	}

	public void setPerfilPaiDTO(PerfilDTO perfilPaiDTO) {
		this.perfilPaiDTO = perfilPaiDTO;
	}

	public PerfilDTO getPerfilDTO() {
		return perfilDTO;
	}

	public void setPerfilDTO(PerfilDTO perfilDTO) {
		this.perfilDTO = perfilDTO;
	}

}
