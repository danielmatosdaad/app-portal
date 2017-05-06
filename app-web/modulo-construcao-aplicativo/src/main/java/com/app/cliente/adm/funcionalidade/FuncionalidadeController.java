package com.app.cliente.adm.funcionalidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.app.cliente.adm.grupofuncionalidade.RepositorioGrupoFuncionalidade;
import com.app.cliente.adm.metadado.RepositorioMetaDado;
import com.app.cliente.adm.perfil.RepositorioPerfil;
import com.app.cliente.controle.sessao.SessaoAutenticada;

import br.app.barramento.integracao.dto.EnvioDTO;
import br.app.barramento.integracao.dto.RespostaDTO;
import br.app.barramento.integracao.dto.TipoAcao;
import br.app.barramento.integracao.exception.InfraEstruturaException;
import br.app.barramento.integracao.exception.NegocioException;
import br.app.servico.infra.integracao.dto.FuncionalidadeDTO;
import br.app.servico.infra.integracao.dto.GrupoFuncionalidadeDTO;
import br.app.servico.infra.integracao.dto.MetaDadoDTO;
import br.app.servico.infra.integracao.dto.PerfilDTO;

@Model
public class FuncionalidadeController implements Serializable {

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
	private RepositorioGrupoFuncionalidade repositorioGrupoFuncionalidade;

	@Inject
	private RepositorioMetaDado repositorioMetaDado;

	@Inject
	private RepositorioPerfil repositorioPerfil;

	@Inject
	private RepositorioFuncionalidade repositorioFuncionalidade;

	@Produces
	@Named
	private FuncionalidadeDTO funcionalidadeRegistro;

	@Named
	private PerfilDTO perfilRegistro;

	@Named
	private GrupoFuncionalidadeDTO grupoFuncionalidadeRegistro;

	@Named
	private MetaDadoDTO metaDadoRegistro;

	@Inject
	private Event<FuncionalidadeDTO> funcionalidadeEventSrc;

	public void onFireEvenChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final FuncionalidadeDTO dto)
			throws InfraEstruturaException, NegocioException {
		initFuncionalidadeDTO();
	}

	@PostConstruct
	private void initFuncionalidadeDTO() throws InfraEstruturaException, NegocioException {
		this.funcionalidadeRegistro = criarFuncionalidade();

		this.repositorioGrupoFuncionalidade.atualizarRepositorio();
		this.repositorioMetaDado.atualizarRepositorio();
		this.repositorioPerfil.atualizarRepositorio();
		this.repositorioFuncionalidade.atualizarRepositorio();
	}

	private FuncionalidadeDTO criarFuncionalidade() {
		return new FuncionalidadeDTO();
	}

	public void registrar() throws Exception {
		try {

			if (sessaoAutenticada == null) {
				log.debug("Sessao indisponivel");
				return;
			}

			if (funcionalidadeRegistro != null) {
				log.info("Registrando");

				this.funcionalidadeRegistro.setPerfil(this.perfilRegistro);
				this.funcionalidadeRegistro.setGrupoFuncionalidade(this.grupoFuncionalidadeRegistro);
				this.metaDadoRegistro.setFuncionalidade(this.funcionalidadeRegistro);
				List<MetaDadoDTO> listaMetadado = new ArrayList<MetaDadoDTO>();
				listaMetadado.add(this.metaDadoRegistro);

				this.funcionalidadeRegistro.setMetadados(listaMetadado);

				EnvioDTO requisicao = new EnvioDTO();
				requisicao.setRequisicao(funcionalidadeRegistro);
				requisicao.setEnvio(funcionalidadeRegistro.getClass().getName());
				RespostaDTO respostaDTO = sessaoAutenticada.getConexao().executar(TipoAcao.SALVAR, requisicao,
						"REPOSITORIO_CORPORATIVO", "CATALOGO_FNC");
				if (respostaDTO != null) {
					log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
					log.info("Mensagem" + respostaDTO.getMensagem().getErro());
					log.info("Mensagem" + respostaDTO.getMensagem().getExcecao().getMessage());
					log.info("Reginstrado com sucesso - id:" + respostaDTO.getResultado().getId());
					
					log.info("Reginstrado com sucesso");
					FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registrado com sucesso!",
							"Registrado com sucesso.");
					facesContext.addMessage(null, m);
					funcionalidadeEventSrc.fire(this.funcionalidadeRegistro);
				}

			}

		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Nao foi posssivel registrar");
			facesContext.addMessage(null, m);
		}
	}

	public void registrarFuncionalidadeSemRelacionamentos() {

		try {

			EnvioDTO requisicao = new EnvioDTO();
			requisicao.setRequisicao(funcionalidadeRegistro);
			requisicao.setEnvio(funcionalidadeRegistro.getClass().getName());
			RespostaDTO respostaDTO = sessaoAutenticada.getConexao().executar(TipoAcao.SALVAR_SEM_RELACIONAMENTO,
					requisicao, "REPOSITORIO_CORPORATIVO", "CATALOGO_FNC");

			if (respostaDTO != null) {
				log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
				log.info("Mensagem" + respostaDTO.getMensagem().getErro());
				log.info("Reginstrado com sucesso - id:" + respostaDTO.getResultado().getId());
				FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registrado com sucesso!",
						"Registrado com sucesso.");
				facesContext.addMessage(null, m);
				funcionalidadeEventSrc.fire(this.funcionalidadeRegistro);
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

	public List getSelectItemPerfil() {
		List items = new ArrayList();

		if (this.repositorioPerfil != null) {

			if (this.repositorioPerfil.getListaPerfil() != null) {
				for (PerfilDTO dto : this.repositorioPerfil.getListaPerfil()) {
					items.add(new SelectItem(dto, dto.getNomePerfil()));
				}

			}
		}

		return items;
	}

	public List getSelectItemMetaDado() {
		List items = new ArrayList();

		if (this.repositorioMetaDado != null) {
			if (repositorioMetaDado.getMetadadoRepositorio() != null) {

				for (MetaDadoDTO dto : repositorioMetaDado.getMetadadoRepositorio()) {
					items.add(new SelectItem(dto, dto.getNomeTela()));
				}
			}

		}

		return items;
	}

	public List getSelectItemGrupoFuncionalidade() {
		List items = new ArrayList();

		if (this.repositorioGrupoFuncionalidade != null) {
			if (this.repositorioGrupoFuncionalidade.getGrupoFuncionalidadeRepositorio() != null) {
				for (GrupoFuncionalidadeDTO dto : this.repositorioGrupoFuncionalidade
						.getGrupoFuncionalidadeRepositorio()) {
					items.add(new SelectItem(dto, dto.getNomeGrupoFuncionalidade()));
				}
			}

		}

		return items;
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

	public MetaDadoDTO getMetaDadoRegistro() {
		return metaDadoRegistro;
	}

	public void setMetaDadoRegistro(MetaDadoDTO metaDadoRegistro) {
		this.metaDadoRegistro = metaDadoRegistro;
	}

	public RepositorioGrupoFuncionalidade getRepositorioGrupoFuncionalidade() {
		return repositorioGrupoFuncionalidade;
	}

	public void setRepositorioGrupoFuncionalidade(RepositorioGrupoFuncionalidade repositorioGrupoFuncionalidade) {
		this.repositorioGrupoFuncionalidade = repositorioGrupoFuncionalidade;
	}

	public RepositorioMetaDado getRepositorioMetaDado() {
		return repositorioMetaDado;
	}

	public void setRepositorioMetaDado(RepositorioMetaDado repositorioMetaDado) {
		this.repositorioMetaDado = repositorioMetaDado;
	}

	public RepositorioPerfil getRepositorioPerfil() {
		return repositorioPerfil;
	}

	public void setRepositorioPerfil(RepositorioPerfil repositorioPerfil) {
		this.repositorioPerfil = repositorioPerfil;
	}

	public RepositorioFuncionalidade getRepositorioFuncionalidade() {
		return repositorioFuncionalidade;
	}

	public void setRepositorioFuncionalidade(RepositorioFuncionalidade repositorioFuncionalidade) {
		this.repositorioFuncionalidade = repositorioFuncionalidade;
	}

}
