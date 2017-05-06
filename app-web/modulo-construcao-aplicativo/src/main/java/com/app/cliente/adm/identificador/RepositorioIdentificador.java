package com.app.cliente.adm.identificador;

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
import br.app.servico.infra.integracao.dto.IdentificadorDTO;
import br.app.servico.infra.integracao.dto.PerfilDTO;
import br.app.servico.infra.integracao.dto.TipoIdentificadorDTO;

@ApplicationScoped
@Named("repIdentificador")
public class RepositorioIdentificador implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;

	@Inject
	private SessaoAutenticada sessaoAutenticada;

	private List<IdentificadorDTO> listaIdentificador;

	@PostConstruct
	public void init() {

		buscarTodosIndentificadores();

	}
	public void onIdentificadorListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final IdentificadorDTO perfil) {
		buscarTodosIndentificadores();
	}
	
	public List getListaTipoIdentificadores() {
		List items = new ArrayList();
		for (TipoIdentificadorDTO tipo : TipoIdentificadorDTO.values()) {
			items.add(new SelectItem(tipo, tipo.getTexto()));
		}
		return items;
	}

	public void buscarTodosIndentificadores() {

		try {

			EnvioDTO envio = new EnvioDTO();
			envio.setEnvio(IdentificadorDTO.class.getName());

			RespostaDTO respostaDTO = sessaoAutenticada.getConexao().executar(TipoAcao.BUSCAR_TODOS, envio,
					"REPOSITORIO_CORPORATIVO", "CATALOGO_FNC");
			if (respostaDTO != null) {
				log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
				log.info("Mensagem" + respostaDTO.getMensagem().getErro());
				if (respostaDTO.getMensagem() == Mensagem.SUCESSO) {
					this.listaIdentificador = (List<IdentificadorDTO>) respostaDTO.getListaResultado();
				}
			}

		} catch (InfraEstruturaException | NegocioException e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}

	public IdentificadorDTO get(String id) {
		buscarTodosIndentificadores();
		Long idBuscar = Long.valueOf(id);

		if (listaIdentificador != null && !listaIdentificador.isEmpty()) {

			IdentificadorDTO IdentificadorDTO = listaIdentificador.get(0);
			if (IdentificadorDTO.getId().longValue() == idBuscar.longValue()) {
				return IdentificadorDTO;
			}

		}

		return null;
	}

	public IdentificadorDTO get(List<IdentificadorDTO> filhos, Long idBuscar) {

		if (listaIdentificador != null && !listaIdentificador.isEmpty()) {

			for (IdentificadorDTO IdentificadorDTO : filhos) {
				if (IdentificadorDTO.getId().longValue() == idBuscar.longValue()) {
					return IdentificadorDTO;
				}
			}

		}

		return null;
	}

	public void atualizarRepositorio() {
		buscarTodosIndentificadores();
	}

	public List<IdentificadorDTO> getListaIdentificador() {
		return listaIdentificador;
	}

	public void setListaIdentificador(List<IdentificadorDTO> listaIdentificador) {
		this.listaIdentificador = listaIdentificador;
	}

	public List getListaIdentificadores() {

		List items = new ArrayList();
		if (this.listaIdentificador != null) {

			for (IdentificadorDTO identificadorDTO : listaIdentificador) {

				items.add(new SelectItem(identificadorDTO, identificadorDTO.getValor()));
			}
		}

		return items;
	}

}
