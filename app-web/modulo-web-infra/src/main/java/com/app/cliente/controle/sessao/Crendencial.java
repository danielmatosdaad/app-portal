package com.app.cliente.controle.sessao;

import java.io.Serializable;

public class Crendencial implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nomeAutenticacao;
	private String senhaAutenticacao;

	public String getNomeAutenticacao() {
		return nomeAutenticacao;
	}

	public void setNomeAutenticacao(String nomeAutenticacao) {
		this.nomeAutenticacao = nomeAutenticacao;
	}

	public String getSenhaAutenticacao() {
		return senhaAutenticacao;
	}

	public void setSenhaAutenticacao(String senhaAutenticacao) {
		this.senhaAutenticacao = senhaAutenticacao;
	}

}
