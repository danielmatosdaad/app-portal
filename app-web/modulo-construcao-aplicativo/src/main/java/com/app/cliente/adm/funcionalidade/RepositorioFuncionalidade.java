package com.app.cliente.adm.funcionalidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.app.cliente.controle.sessao.SessaoAutenticada;

import br.app.barramento.integracao.dto.EnvioDTO;
import br.app.barramento.integracao.dto.Mensagem;
import br.app.barramento.integracao.dto.RespostaDTO;
import br.app.barramento.integracao.dto.TipoAcao;
import br.app.barramento.integracao.exception.InfraEstruturaException;
import br.app.barramento.integracao.exception.NegocioException;
import br.app.servico.infra.integracao.dto.FuncionalidadeDTO;

@ApplicationScoped
@Named
public class RepositorioFuncionalidade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;

	@Inject
	private SessaoAutenticada sessaoAutenticada;

	private List<FuncionalidadeDTO> repositorioFuncionalidade;

	@PostConstruct
	public void init() {

		buscarTodasFuncionalidades();

	}

	public void onFireEvenChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final FuncionalidadeDTO dto)
			throws InfraEstruturaException, NegocioException {
		buscarTodasFuncionalidades();
	}

	public void buscarTodasFuncionalidades() {

		try {

			EnvioDTO envio = new EnvioDTO();
			envio.setEnvio(FuncionalidadeDTO.class.getName());

			RespostaDTO respostaDTO = sessaoAutenticada.getConexao().executar(TipoAcao.BUSCAR_TODOS, envio,
					"REPOSITORIO_CORPORATIVO", "CATALOGO_FNC");
			if (respostaDTO != null) {
				log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
				log.info("Mensagem" + respostaDTO.getMensagem().getErro());
				if (respostaDTO.getMensagem() == Mensagem.SUCESSO) {
					this.repositorioFuncionalidade = (List<FuncionalidadeDTO>) respostaDTO.getListaResultado();
				}
			}

		} catch (InfraEstruturaException | NegocioException e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}

	public List<FuncionalidadeDTO> getrepositorioFuncionalidade() {
		return repositorioFuncionalidade;
	}

	public void setrepositorioFuncionalidade(List<FuncionalidadeDTO> repositorioFuncionalidade) {
		this.repositorioFuncionalidade = repositorioFuncionalidade;
	}

	public FuncionalidadeDTO get(String id) {
		buscarTodasFuncionalidades();
		Long idBuscar = Long.valueOf(id);

		if (repositorioFuncionalidade != null && !repositorioFuncionalidade.isEmpty()) {

			FuncionalidadeDTO funcionalidadeDTO = repositorioFuncionalidade.get(0);
			if (funcionalidadeDTO.getId().longValue() == idBuscar.longValue()) {
				return funcionalidadeDTO;
			}

			if (funcionalidadeDTO.getFuncionalidadeFilhos() != null
					&& !funcionalidadeDTO.getFuncionalidadeFilhos().isEmpty()) {

				FuncionalidadeDTO funcionalidadeFilho = get(funcionalidadeDTO.getFuncionalidadeFilhos(), idBuscar);

				if (funcionalidadeFilho != null) {

					return funcionalidadeFilho;
				}
			}
		}

		return null;
	}

	public FuncionalidadeDTO get(List<FuncionalidadeDTO> filhos, Long idBuscar) {

		if (repositorioFuncionalidade != null && !repositorioFuncionalidade.isEmpty()) {

			for (FuncionalidadeDTO funcionalidadeDTO : filhos) {
				if (funcionalidadeDTO.getId().longValue() == idBuscar.longValue()) {
					return funcionalidadeDTO;
				}

				if (funcionalidadeDTO.getFuncionalidadeFilhos() != null
						&& !funcionalidadeDTO.getFuncionalidadeFilhos().isEmpty()) {

					FuncionalidadeDTO funcionalidadeFilho = get(funcionalidadeDTO.getFuncionalidadeFilhos(), idBuscar);

					if (funcionalidadeFilho != null) {

						return funcionalidadeFilho;
					}
				}
			}

		}

		return null;
	}

	public void atualizarRepositorio() {
		buscarTodasFuncionalidades();
	}

	public List<FuncionalidadeDTO> getRepositorioFuncionalidade() {
		return repositorioFuncionalidade;
	}

	public void setRepositorioFuncionalidade(List<FuncionalidadeDTO> repositorioFuncionalidade) {
		this.repositorioFuncionalidade = repositorioFuncionalidade;
	}

	public List getSelectItemFuncionalidade() {
		List items = new ArrayList();

		if (this.repositorioFuncionalidade != null) {
			if (this.getRepositorioFuncionalidade() != null) {
				for (FuncionalidadeDTO dto : this.getRepositorioFuncionalidade()) {
					items.add(new SelectItem(dto, dto.getNomeFuncionalidade()));
				}
			}

		}

		return items;
	}

}
