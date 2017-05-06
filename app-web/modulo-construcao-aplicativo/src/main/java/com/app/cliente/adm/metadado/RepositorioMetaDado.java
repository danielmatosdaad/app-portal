package com.app.cliente.adm.metadado;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
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
import br.app.servico.infra.integracao.dto.MetaDadoDTO;
import br.app.servico.infra.integracao.dto.ObterMetaDadoDTO;

@ApplicationScoped
@Named(value="repositorioMetaDado")
public class RepositorioMetaDado implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;

	@Inject
	private SessaoAutenticada sessaoAutenticada;

	private List<MetaDadoDTO> metadadoRepositorio;

	@PostConstruct
	public void init() {

		buscarTodosMetadados();

	}
	
	public void onMetaDaDoListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final MetaDadoDTO metaDado) {
		buscarTodosMetadados();
	}

	public void buscarTodosMetadados() {
		try {

			EnvioDTO envio = new EnvioDTO();
			envio.setEnvio(MetaDadoDTO.class.getName());

			RespostaDTO respostaDTO = sessaoAutenticada.getConexao().executar(TipoAcao.BUSCAR_TODOS, envio,
					"REPOSITORIO_CORPORATIVO", "CATALOGO_FNC");
			if (respostaDTO != null) {
				log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
				log.info("Mensagem" + respostaDTO.getMensagem().getErro());
				if (respostaDTO.getMensagem() == Mensagem.SUCESSO) {
					this.metadadoRepositorio = (List<MetaDadoDTO>) respostaDTO.getListaResultado();
				}
			}
		} catch (InfraEstruturaException | NegocioException e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}

	public MetaDadoDTO get(String id) {

		Long idBuscar = Long.valueOf(id);

		if (metadadoRepositorio != null && !metadadoRepositorio.isEmpty()) {

			for (MetaDadoDTO metaDadoDTO : metadadoRepositorio) {
				if (metaDadoDTO.getId().longValue() == idBuscar.longValue()) {
					return metaDadoDTO;
				}
			}
		}

		return null;
	}

	public void atualizarRepositorio() {

		buscarTodosMetadados();
	}

	public List<MetaDadoDTO> getMetadadoRepositorio() {
		return metadadoRepositorio;
	}

	public MetaDadoDTO buscarMetaDadoFuncionalidade(int numeroFuncionalidade, int numeroTela) {

		try {

			EnvioDTO envio = new EnvioDTO();
			envio.setEnvio(ObterMetaDadoDTO.class.getName());

			ObterMetaDadoDTO requisicao = new ObterMetaDadoDTO();
			requisicao.setNumeroFuncionalidade(numeroFuncionalidade);
			requisicao.setNumeroTela(numeroTela);
			envio.setRequisicao(requisicao);
			RespostaDTO respostaDTO = sessaoAutenticada.getConexao().executar(TipoAcao.BUSCAR_TODOS, envio,
					"REPOSITORIO_CORPORATIVO", "CATALOGO_FNC");
			if (respostaDTO != null) {
				log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
				log.info("Mensagem" + respostaDTO.getMensagem().getErro());
				if (respostaDTO.getMensagem() == Mensagem.SUCESSO) {
					this.metadadoRepositorio = (List<MetaDadoDTO>) respostaDTO.getListaResultado();
				}
			}
		} catch (InfraEstruturaException | NegocioException e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
		return null;
	}

}
