package com.app.cliente.adm.configura.tela;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.DragDropEvent;
import org.slf4j.Logger;

import com.app.cliente.controle.sessao.SessaoAutenticada;
import com.app.cliente.resource.util.TransferirDados;

import br.app.barramento.integracao.dto.EnvioDTO;
import br.app.barramento.integracao.dto.RespostaDTO;
import br.app.barramento.integracao.dto.TipoAcao;
import br.app.barramento.integracao.exception.InfraEstruturaException;
import br.app.servico.infra.integracao.dto.CompositeDTO;
import br.app.servico.infra.integracao.dto.CompositeInterfaceDTO;
import br.app.servico.infra.integracao.dto.ObterConversaoComponenteTelaDTO;
import br.projeto.bean.ComponenteBean;
import br.projeto.util.ComponentesRegistrados;

@SessionScoped
@Named("escCpnTela")
public class EscolhaComponenteTela implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ContextoConfiguraComponenteTela contexto;

	@Inject
	private FacesContext facesContext;

	private List<CompositeInterfaceDTO> componenteTelaRegistrados;

	private CompositeInterfaceDTO removerCompositeEscolhido;

	@Inject
	private SessaoAutenticada sessaoAutenticada;

	@Inject
	private Logger log;

	@PostConstruct
	public void init() throws Exception {

		try {
			List<ComponenteBean> lista = ComponentesRegistrados.buscarComponentes();
			List<File> listaFile = new ArrayList<>();
			if(lista!=null && !lista.isEmpty()){
				for (ComponenteBean componenteBean : lista) {
					listaFile.add(componenteBean.getStream());
				}

				this.componenteTelaRegistrados = new ArrayList<CompositeInterfaceDTO>();
				ObterConversaoComponenteTelaDTO obterConversao = new ObterConversaoComponenteTelaDTO();
				obterConversao.setFiles(listaFile);

				EnvioDTO requisicao = new EnvioDTO();
				requisicao.setRequisicao(obterConversao);
				requisicao.setEnvio(obterConversao.getClass().getName());

				RespostaDTO respostaDTO = sessaoAutenticada.getConexao().executar(TipoAcao.CONVERTER_ARQUIVO_COMPONENTE_TELA,
						requisicao, "REPOSITORIO_CORPORATIVO", "CATALOGO_CPN_TLA");

				if (respostaDTO != null) {
					log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
					log.info("Mensagem" + respostaDTO.getMensagem().getErro());

					List<CompositeDTO> composites = (List<CompositeDTO>) respostaDTO.getListaResultado();
					for (CompositeDTO compositeDTO : composites) {
						this.componenteTelaRegistrados.add(compositeDTO.getInterfaces());
					}
					log.info("Reginstrado com sucesso");
					FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registrado com sucesso!",
							"Registrado com sucesso.");
					facesContext.addMessage(null, m);
				}
				
			}
		

		} catch (Exception e) {
			log.info("Mensagem" + e.getMessage());
			e.printStackTrace();
		}
	
	}

	public void onFaceletDrop(DragDropEvent ddEvent) {
		CompositeInterfaceDTO escolhido = ((CompositeInterfaceDTO) ddEvent.getData());
		CompositeInterfaceDTO novo;
		try {
			novo = TransferirDados.transferir(escolhido, CompositeInterfaceDTO.class);
			this.contexto.getComponentesEscolhidos().add(novo);
		} catch (InfraEstruturaException e) {
			e.printStackTrace();
		}

	}

	public void buttonActionRemoverComponente(ActionEvent actionEvent) {

		List<CompositeInterfaceDTO> faceletEscolhido = this.contexto.getComponentesEscolhidos();
		for (Iterator<CompositeInterfaceDTO> i = faceletEscolhido.iterator(); i.hasNext();) {
			CompositeInterfaceDTO composite = i.next();

			if (this.removerCompositeEscolhido != null) {

				if (composite.getNome().equals(this.removerCompositeEscolhido.getNome())) {

					i.remove();

				}

			}
		}
	}

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public String reiniciar() throws Exception {

		this.contexto.limpar();
		init();

		return "/adm/configuraTela/escolhaComponente";
	}

	public ContextoConfiguraComponenteTela getContexto() {
		return contexto;
	}

	public void setContexto(ContextoConfiguraComponenteTela contexto) {
		this.contexto = contexto;
	}

	public List<CompositeInterfaceDTO> getComponenteTelaRegistrados() {
		return componenteTelaRegistrados;
	}

	public void setComponenteTelaRegistrados(List<CompositeInterfaceDTO> componenteTelaRegistrados) {
		this.componenteTelaRegistrados = componenteTelaRegistrados;
	}

	public CompositeInterfaceDTO getRemoverCompositeEscolhido() {
		return removerCompositeEscolhido;
	}

	public void setRemoverCompositeEscolhido(CompositeInterfaceDTO removerCompositeEscolhido) {
		this.removerCompositeEscolhido = removerCompositeEscolhido;
	}

}
