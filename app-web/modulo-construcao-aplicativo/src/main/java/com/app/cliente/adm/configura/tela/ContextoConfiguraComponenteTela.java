package com.app.cliente.adm.configura.tela;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import br.app.servico.infra.integracao.dto.ComponenteTelaDTO;
import br.app.servico.infra.integracao.dto.CompositeInterfaceDTO;
import br.app.servico.infra.integracao.dto.IdentificadorDTO;
import br.app.servico.infra.integracao.dto.ResultadoConvercaoComponenteUI;

@Named(value = "ctxConfCpnTela")
@SessionScoped
public class ContextoConfiguraComponenteTela implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private List<CompositeInterfaceDTO> componentesEscolhidos;

	private List<ComponenteTelaDTO> componenteTelaDTOs;

	private List<IdentificadorDTO> identificadores;

	ResultadoConvercaoComponenteUI resultadoConvercaoComponenteUI;

	private String outputComponenteTelaUI;

	@PostConstruct
	public void init() {

		this.componentesEscolhidos = new ArrayList<CompositeInterfaceDTO>();
	}

	public List<CompositeInterfaceDTO> getComponentesEscolhidos() {
		return componentesEscolhidos;
	}

	public void setComponentesEscolhidos(List<CompositeInterfaceDTO> componentesEscolhidos) {
		this.componentesEscolhidos = componentesEscolhidos;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOutputComponenteTelaUI() {
		return outputComponenteTelaUI;
	}

	public void setOutputComponenteTelaUI(String outputComponenteTelaUI) {
		this.outputComponenteTelaUI = outputComponenteTelaUI;
	}

	public List<ComponenteTelaDTO> getComponenteTelaDTOs() {
		return componenteTelaDTOs;
	}

	public void setComponenteTelaDTOs(List<ComponenteTelaDTO> componenteTelaDTOs) {
		this.componenteTelaDTOs = componenteTelaDTOs;
	}

	public void limpar() {
		if (this.componentesEscolhidos != null) {

			this.componentesEscolhidos.clear();
		}

		if (this.componenteTelaDTOs != null) {
			this.componenteTelaDTOs.clear();
		}

		if (this.outputComponenteTelaUI != null) {

			this.outputComponenteTelaUI = "";
		}

	}

	public List<IdentificadorDTO> getIdentificadores() {
		return identificadores;
	}

	public void setIdentificadores(List<IdentificadorDTO> identificadores) {
		this.identificadores = identificadores;
	}

	public ResultadoConvercaoComponenteUI getResultadoConvercaoComponenteUI() {
		return resultadoConvercaoComponenteUI;
	}

	public void setResultadoConvercaoComponenteUI(ResultadoConvercaoComponenteUI resultadoConvercaoComponenteUI) {
		this.resultadoConvercaoComponenteUI = resultadoConvercaoComponenteUI;
	}

}
