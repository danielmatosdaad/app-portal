package com.app.cliente.adm.configura.tela;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;

import com.app.cliente.controle.sessao.SessaoAutenticada;

import br.app.barramento.integracao.dto.EnvioDTO;
import br.app.barramento.integracao.dto.RespostaDTO;
import br.app.barramento.integracao.dto.TipoAcao;
import br.app.barramento.integracao.exception.InfraEstruturaException;
import br.app.barramento.integracao.exception.NegocioException;
import br.app.servico.infra.integracao.dto.ComponenteDTO;
import br.app.servico.infra.integracao.dto.ComponenteTelaDTO;
import br.app.servico.infra.integracao.dto.IdentificadorDTO;
import br.app.servico.infra.integracao.dto.ObterConversaoCompositeDTO;
import br.app.servico.infra.integracao.dto.PropriedadeDTO;

@SessionScoped
@Named("confCpn")
public class ConfiguraComponenteTela implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;

	@Inject
	private SessaoAutenticada sessaoAutenticada;
	@Inject
	private ContextoConfiguraComponenteTela contexto;

	private Map<String, Object> parametros;

	public void init() {

		this.parametros = new LinkedHashMap<String, Object>();

		try {
			EnvioDTO envio = new EnvioDTO();
			ObterConversaoCompositeDTO requisicao = new ObterConversaoCompositeDTO();
			requisicao.setComposites(this.contexto.getComponentesEscolhidos());
			envio.setRequisicao(requisicao);
			envio.setEnvio(requisicao.getClass().getName());

			RespostaDTO respostaDTO = sessaoAutenticada.getConexao().executar(
					TipoAcao.CONVERTER_COMPOSITE_COMPONENTE_TELA, envio, "REPOSITORIO_CORPORATIVO", "CATALOGO_CPN_TLA");

			if (respostaDTO != null) {
				log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
				log.info("Mensagem" + respostaDTO.getMensagem().getErro());
				List<ComponenteTelaDTO> resultado = (List<ComponenteTelaDTO>) respostaDTO.getListaResultado();
				this.contexto.setComponenteTelaDTOs(resultado);

			}

		} catch (InfraEstruturaException | NegocioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String irParaPagina() {

		init();
		return "/adm/configuraTela/configuraComponente";
	}

	private void listarValoreRecebidos() {

		System.out.println("Parametros negociais recebido: ");
		for (Iterator<String> iterator = this.parametros.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			System.out.println("chave: " + key + " valor: " + this.parametros.get(key));
		}

	}

	public void buttonSalvarPropriedadeComponente(ActionEvent actionEvent) {

		listarValoreRecebidos();

		List<IdentificadorDTO> identificadoresComponenteDTO = new ArrayList<IdentificadorDTO>();

		if (this.contexto.getComponenteTelaDTOs() != null || (!this.contexto.getComponenteTelaDTOs().isEmpty())) {
			for (ComponenteTelaDTO componenteTelaDTO : this.contexto.getComponenteTelaDTOs()) {

				for (ComponenteDTO componenteDTO : componenteTelaDTO.getComponentes()) {

					for (PropriedadeDTO propriedadeDTO : componenteDTO.getPropriedades()) {

						String id = obterIdComponente(componenteDTO, propriedadeDTO);

						Object objeto = this.parametros.get(id);
						if (objeto instanceof String) {

							configurarPropriedade(propriedadeDTO, (String) objeto);
						}

						if (objeto instanceof String || objeto instanceof Number) {

							configurarPropriedade(propriedadeDTO, String.valueOf(objeto));
						}

						if (objeto instanceof IdentificadorDTO) {

							IdentificadorDTO identificador = (IdentificadorDTO) objeto;
							identificadoresComponenteDTO.add(identificador);
							configurarPropriedade(propriedadeDTO, identificador.getValor());

						}

					}
				}

			}

			this.contexto.setIdentificadores(identificadoresComponenteDTO);

		}

	}

	public String obterIdComponente(ComponenteDTO componenteDTO, PropriedadeDTO propriedadeDTO) {
		String id = componenteDTO.getNomeComponente().concat("-").concat(propriedadeDTO.getNome())
				.concat(String.valueOf(componenteDTO.getIdentificador()));
		System.out.println("###########" + id + "###################");
		return id;
	}

	private PropriedadeDTO configurarPropriedade(PropriedadeDTO propriedade, String valor) {

		propriedade.setValor(valor);

		return propriedade;
	}

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public String irPara() {

		return "/adm/configuraTela/visualizaComponente";
	}

	public void limpar(ActionEvent actionEvent) {

		this.parametros = new LinkedHashMap<String, Object>();
	}

	public Map<String, Object> getParametros() {
		return parametros;
	}

	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}

	public ContextoConfiguraComponenteTela getContexto() {
		return contexto;
	}

	public void setContexto(ContextoConfiguraComponenteTela contexto) {
		this.contexto = contexto;
	}

}
