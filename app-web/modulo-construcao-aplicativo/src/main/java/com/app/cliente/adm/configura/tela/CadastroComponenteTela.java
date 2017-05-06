package com.app.cliente.adm.configura.tela;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.app.cliente.controle.sessao.SessaoAutenticada;

import br.app.barramento.integracao.dto.EnvioDTO;
import br.app.barramento.integracao.dto.RespostaDTO;
import br.app.barramento.integracao.dto.TipoAcao;
import br.app.servico.infra.integracao.dto.FuncionalidadeDTO;
import br.app.servico.infra.integracao.dto.GrupoFuncionalidadeDTO;
import br.app.servico.infra.integracao.dto.MetaDadoDTO;
import br.app.servico.infra.integracao.dto.PerfilDTO;
import br.app.servico.infra.integracao.dto.ProcessoConfiguracaoDTO;
import br.app.servico.infra.integracao.dto.RegistroAuditoriaDTO;

@Named(value = "cadCpnTela")
@RequestScoped
public class CadastroComponenteTela implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private ContextoConfiguraComponenteTela contexto;

	@Named
	private MetaDadoDTO metaDadoDTO;

	@Named
	private FuncionalidadeDTO funcionalidadeRegistro;

	@Named
	private PerfilDTO perfilRegistro;

	@Named
	private GrupoFuncionalidadeDTO grupoFuncionalidadeRegistro;

	@Inject
	private SessaoAutenticada sessaoAutenticada;

	@Named
	private ProcessoConfiguracaoDTO processoConfiguracaoDTO;

	@Inject
	private Event<MetaDadoDTO> metadadoEventSrc;

	@Inject
	private Event<FuncionalidadeDTO> funcionalidadeEventSrc;

	@PostConstruct
	public void initMetaDadoDTO() {
		this.metaDadoDTO = criarMetaDado();
		this.funcionalidadeRegistro = new FuncionalidadeDTO();
		this.grupoFuncionalidadeRegistro = new GrupoFuncionalidadeDTO();
		this.perfilRegistro = new PerfilDTO();
		this.processoConfiguracaoDTO = new ProcessoConfiguracaoDTO();
		this.metaDadoDTO.setXml(this.contexto.getOutputComponenteTelaUI());
	}

	private MetaDadoDTO criarMetaDado() {

		RegistroAuditoriaDTO r = new RegistroAuditoriaDTO();
		r.setDataCadastro(new Date());

		MetaDadoDTO m = new MetaDadoDTO();
		m.setRegistroAuditoria(r);
		return m;
	}

	public String registrar() throws Exception {
		try {

			if (metaDadoDTO != null) {
				log.info("Reginstrando metaDado");
				this.metaDadoDTO.setIdentificadores(this.contexto.getIdentificadores());
				this.metaDadoDTO
						.setXml(this.contexto.getResultadoConvercaoComponenteUI().getComponenteXml().get(0).toString());
				this.metaDadoDTO.setXhtml(this.contexto.getOutputComponenteTelaUI());
				this.processoConfiguracaoDTO.setMetadadoDTO(metaDadoDTO);
				this.processoConfiguracaoDTO.setFuncionalidadeDTO(funcionalidadeRegistro);
				this.processoConfiguracaoDTO.setGrupoFuncionalidadeDTO(grupoFuncionalidadeRegistro);
				this.processoConfiguracaoDTO.setPerfilDTO(perfilRegistro);
				this.processoConfiguracaoDTO.setIdentificadoresDTO(this.contexto.getIdentificadores());

				EnvioDTO requisicao = new EnvioDTO();
				requisicao.setRequisicao(this.processoConfiguracaoDTO);
				requisicao.setEnvio(ProcessoConfiguracaoDTO.class.getName());

				RespostaDTO respostaDTO = sessaoAutenticada.getConexao().executar(TipoAcao.SALVAR_TELA_UI, requisicao,
						"REPOSITORIO_CORPORATIVO", "CATALOGO_FNC");

				if (respostaDTO != null) {
					log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
					log.info("Mensagem" + respostaDTO.getMensagem().getErro());
					log.info("Reginstrado com sucesso" + this.processoConfiguracaoDTO);
					facesContext.addMessage(null, new FacesMessage("Sucesso",  "Transacao realizada com sucesso") );
					metadadoEventSrc.fire(this.metaDadoDTO);
					funcionalidadeEventSrc.fire(this.funcionalidadeRegistro);
				}

			}
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registrado com sucesso!",
					"Registrado com sucesso.");
			facesContext.addMessage(null, m);
			initMetaDadoDTO();
			return "/adm/configuraTela/sucessoCriacaoTela";
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Nao foi posssivel registrar");
			facesContext.addMessage(null, m);
		}

		return null;
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

	public MetaDadoDTO getMetaDadoDTO() {
		return metaDadoDTO;
	}

	public void setMetaDadoDTO(MetaDadoDTO metaDadoDTO) {
		this.metaDadoDTO = metaDadoDTO;
	}

	public ContextoConfiguraComponenteTela getContexto() {
		return contexto;
	}

	public void setContexto(ContextoConfiguraComponenteTela contexto) {
		this.contexto = contexto;
	}

	public FuncionalidadeDTO getFuncionalidadeRegistro() {
		return funcionalidadeRegistro;
	}

	public void setFuncionalidadeRegistro(FuncionalidadeDTO funcionalidadeRegistro) {
		this.funcionalidadeRegistro = funcionalidadeRegistro;
	}

	public PerfilDTO getPerfilRegistro() {
		return perfilRegistro;
	}

	public void setPerfilRegistro(PerfilDTO perfilRegistro) {
		this.perfilRegistro = perfilRegistro;
	}

	public GrupoFuncionalidadeDTO getGrupoFuncionalidadeRegistro() {
		return grupoFuncionalidadeRegistro;
	}

	public void setGrupoFuncionalidadeRegistro(GrupoFuncionalidadeDTO grupoFuncionalidadeRegistro) {
		this.grupoFuncionalidadeRegistro = grupoFuncionalidadeRegistro;
	}

}
