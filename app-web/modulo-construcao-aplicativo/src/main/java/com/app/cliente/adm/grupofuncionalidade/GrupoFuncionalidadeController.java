package com.app.cliente.adm.grupofuncionalidade;

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

import br.app.barramento.integracao.dto.EnvioDTO;
import br.app.barramento.integracao.dto.RespostaDTO;
import br.app.barramento.integracao.dto.TipoAcao;
import br.app.servico.infra.integracao.dto.GrupoFuncionalidadeDTO;


@Model
public class GrupoFuncionalidadeController {

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private Event<GrupoFuncionalidadeDTO> grupoFuncionalidadeEventSrc;

	@Produces
	@Named
	private GrupoFuncionalidadeDTO grupoFuncionalidadeDTO;

	@Inject
	private SessaoAutenticada sessaoAutenticada;

	@PostConstruct
	public void initGrupoFuncionalidadeDTO() {
		this.grupoFuncionalidadeDTO = criarGrupoFuncionalidade();
	}

	private GrupoFuncionalidadeDTO criarGrupoFuncionalidade() {
		return new GrupoFuncionalidadeDTO();
	}

	public void registrar() throws Exception {
		try {

			if (grupoFuncionalidadeDTO != null) {
				log.info("Registrando grupo");

				EnvioDTO registrarParametro = new EnvioDTO();
				registrarParametro.setRequisicao(grupoFuncionalidadeDTO);
				registrarParametro.setEnvio(grupoFuncionalidadeDTO.getClass().getName());
				RespostaDTO respostaDTO = sessaoAutenticada.getConexao().executar(TipoAcao.SALVAR, registrarParametro,
						"REPOSITORIO_CORPORATIVO", "CATALOGO_FNC");
				if (respostaDTO != null) {
					log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
					log.info("Mensagem" + respostaDTO.getMensagem().getErro());
					log.info("Reginstrado com sucesso - id:" + respostaDTO.getResultado().getId());
					log.info("Reginstrado com sucesso");
					facesContext.addMessage(null, new FacesMessage("Sucesso",  "Transacao realizada com sucesso") );
					grupoFuncionalidadeEventSrc.fire(grupoFuncionalidadeDTO);
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

	public GrupoFuncionalidadeDTO getGrupoFuncionalidadeDTO() {
		return grupoFuncionalidadeDTO;
	}

	public void setGrupoFuncionalidadeDTO(GrupoFuncionalidadeDTO grupoFuncionalidadeDTO) {
		this.grupoFuncionalidadeDTO = grupoFuncionalidadeDTO;
	}

}
