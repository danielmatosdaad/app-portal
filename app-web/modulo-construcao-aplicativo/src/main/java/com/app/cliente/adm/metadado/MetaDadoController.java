package com.app.cliente.adm.metadado;

import java.io.Serializable;
import java.util.Date;

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
import br.app.servico.infra.integracao.dto.MetaDadoDTO;
import br.app.servico.infra.integracao.dto.RegistroAuditoriaDTO;

@Model
public class MetaDadoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private SessaoAutenticada sessaoAutenticada;

	@Inject
	private Event<MetaDadoDTO> metadadoEventSrc;

	@Produces
	@Named
	private MetaDadoDTO metaDadoDTO;

	@PostConstruct
	public void initMetaDadoDTO() {

		this.metaDadoDTO = criarMetaDado();
	}

	private MetaDadoDTO criarMetaDado() {

		RegistroAuditoriaDTO r = new RegistroAuditoriaDTO();
		r.setDataCadastro(new Date());

		MetaDadoDTO m = new MetaDadoDTO();
		m.setRegistroAuditoria(r);
		return m;
	}

	public void registrar() throws Exception {
		try {

			if (metaDadoDTO != null) {
				log.info("Registrando");

				EnvioDTO requisicao = new EnvioDTO();
				requisicao.setRequisicao(metaDadoDTO);
				requisicao.setEnvio(metaDadoDTO.getClass().getName());
				RespostaDTO respostaDTO = sessaoAutenticada.getConexao().executar(TipoAcao.SALVAR, requisicao,
						"REPOSITORIO_CORPORATIVO", "CATALOGO_FNC");
				if (respostaDTO != null) {
					log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
					log.info("Mensagem" + respostaDTO.getMensagem().getErro());
					log.info("Reginstrado com sucesso - id:" + respostaDTO.getResultado().getId());
					log.info("Reginstrado com sucesso");
					facesContext.addMessage(null, new FacesMessage("Sucesso",  "Transacao realizada com sucesso") );
					metadadoEventSrc.fire(metaDadoDTO);
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

}
