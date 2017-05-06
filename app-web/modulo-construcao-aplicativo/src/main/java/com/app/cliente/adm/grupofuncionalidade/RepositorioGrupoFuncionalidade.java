package com.app.cliente.adm.grupofuncionalidade;

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
import br.app.servico.infra.integracao.dto.GrupoFuncionalidadeDTO;

@ApplicationScoped
@Named
public class RepositorioGrupoFuncionalidade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;

	private List<GrupoFuncionalidadeDTO> grupoFuncionalidadeRepositorio;

	@Inject
	private SessaoAutenticada sessaoAutenticada;

	@PostConstruct
	public void init() {

		buscarTodosGrupoFuncionalidade();

	}

	public void buscarTodosGrupoFuncionalidade() {
		try {

			EnvioDTO envio = new EnvioDTO();
			envio.setEnvio(GrupoFuncionalidadeDTO.class.getName());

			RespostaDTO respostaDTO = sessaoAutenticada.getConexao().executar(TipoAcao.BUSCAR_TODOS, envio,
					"REPOSITORIO_CORPORATIVO", "CATALOGO_FNC");
			if (respostaDTO != null) {
				log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
				log.info("Mensagem" + respostaDTO.getMensagem().getErro());
				if (respostaDTO.getMensagem() == Mensagem.SUCESSO) {
					this.grupoFuncionalidadeRepositorio = (List<GrupoFuncionalidadeDTO>) respostaDTO
							.getListaResultado();
				}
			}

		} catch (InfraEstruturaException | NegocioException e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}

	public GrupoFuncionalidadeDTO get(String id) {

		Long idBuscar = Long.valueOf(id);

		if (grupoFuncionalidadeRepositorio != null && !grupoFuncionalidadeRepositorio.isEmpty()) {

			for (GrupoFuncionalidadeDTO GrupoFuncionalidadeDTO : grupoFuncionalidadeRepositorio) {
				if (GrupoFuncionalidadeDTO.getId().longValue() == idBuscar.longValue()) {
					return GrupoFuncionalidadeDTO;
				}
			}
		}

		return null;
	}

	public void onGrupoFuncionalidadeListChanged(
			@Observes(notifyObserver = Reception.IF_EXISTS) final GrupoFuncionalidadeDTO grupoFuncionalidadeDTO) {
		atualizarRepositorio();
	}

	public void atualizarRepositorio() {

		buscarTodosGrupoFuncionalidade();
	}

	public List<GrupoFuncionalidadeDTO> getGrupoFuncionalidadeRepositorio() {
		return grupoFuncionalidadeRepositorio;
	}

	public List getSelectItemGrupoFuncionalidade() {
		List items = new ArrayList();

		if (this.getGrupoFuncionalidadeRepositorio() != null) {
			for (GrupoFuncionalidadeDTO dto : this.getGrupoFuncionalidadeRepositorio()) {
				items.add(new SelectItem(dto, dto.getNomeGrupoFuncionalidade()));
			}
		}

		return items;
	}

}
