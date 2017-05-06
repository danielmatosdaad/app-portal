package com.app.cliente.adm.perfil;

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
import br.app.servico.infra.integracao.dto.PerfilDTO;

@ApplicationScoped
@Named
public class RepositorioPerfil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;

	@Inject
	private SessaoAutenticada sessaoAutenticada;

	private List<PerfilDTO> listaPerfil;

	

	@PostConstruct
	public void init() {

		buscaTodosPerfils();

	}

	public void onPerfilListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final PerfilDTO perfil) {
		atualizarRepositorio();
	}


	public void buscaTodosPerfils() {

		try {

			EnvioDTO envio = new EnvioDTO();

			envio.setEnvio(PerfilDTO.class.getName());

			RespostaDTO respostaDTO = sessaoAutenticada.getConexao().executar(TipoAcao.BUSCAR_TODOS, envio,
					"REPOSITORIO_CORPORATIVO", "CATALOGO_FNC");
			if (respostaDTO != null) {
				log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
				log.info("Mensagem" + respostaDTO.getMensagem().getErro());
				if (respostaDTO.getMensagem() == Mensagem.SUCESSO) {
					listaPerfil = (List<PerfilDTO>) respostaDTO.getListaResultado();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			
		}

	}

	public PerfilDTO get(String id) {
		buscaTodosPerfils();
		Long idBuscar = Long.valueOf(id);

		if (listaPerfil != null && !listaPerfil.isEmpty()) {

			PerfilDTO perfilDTO = listaPerfil.get(0);
			if (perfilDTO.getId().longValue() == idBuscar.longValue()) {
				return perfilDTO;
			}

			if (perfilDTO.getPerfilFilhos() != null && !perfilDTO.getPerfilFilhos().isEmpty()) {

				PerfilDTO perfilFilho = get(perfilDTO.getPerfilFilhos(), idBuscar);

				if (perfilFilho != null) {

					return perfilFilho;
				}
			}
		}

		return null;
	}

	PerfilDTO get(List<PerfilDTO> filhos, Long idBuscar) {

		if (listaPerfil != null && !listaPerfil.isEmpty()) {

			for (PerfilDTO perfilDTO : filhos) {
				if (perfilDTO.getId().longValue() == idBuscar.longValue()) {
					return perfilDTO;
				}

				if (perfilDTO.getPerfilFilhos() != null && !perfilDTO.getPerfilFilhos().isEmpty()) {

					PerfilDTO perfilFilho = get(perfilDTO.getPerfilFilhos(), idBuscar);

					if (perfilFilho != null) {

						return perfilFilho;
					}
				}
			}

		}

		return null;
	}

	public void atualizarRepositorio() {
		buscaTodosPerfils();
	}

	private void configurarSubPerfisSelectItem(PerfilDTO perfilDTO, List items) {
		if (perfilDTO != null) {
			items.add(new SelectItem(perfilDTO, perfilDTO.getNomePerfil()));
			if (perfilDTO.getPerfilFilhos() != null && !perfilDTO.getPerfilFilhos().isEmpty()) {
				for (PerfilDTO filhos : perfilDTO.getPerfilFilhos()) {
					configurarSubPerfisSelectItem(filhos, items);
				}

			}

		}
	}

	public List getListaPerfilSelectItem() {
		List items = new ArrayList();

		if (this.listaPerfil != null && !this.listaPerfil.isEmpty()) {
			configurarSubPerfisSelectItem(listaPerfil.get(0), items);
		}
		return items;
	}

	public List getSelectItemPerfil() {
		List items = new ArrayList();

		if (this.getListaPerfil() != null) {
			for (PerfilDTO dto : this.getListaPerfil()) {
				items.add(new SelectItem(dto, dto.getNomePerfil()));
			}

		}
		return items;
	}


	public List<PerfilDTO> getListaPerfil() {
		return listaPerfil;
	}

	public void setListaPerfil(List<PerfilDTO> listaPerfil) {
		this.listaPerfil = listaPerfil;
	}
	
	
}
