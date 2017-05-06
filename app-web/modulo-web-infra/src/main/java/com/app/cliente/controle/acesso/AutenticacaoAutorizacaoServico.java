package com.app.cliente.controle.acesso;

import java.util.Collection;

import org.app.seguranca.integracao.IProxySegurancaConexao;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class AutenticacaoAutorizacaoServico extends UsernamePasswordAuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	IProxySegurancaConexao conexao;

	public AutenticacaoAutorizacaoServico(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities, IProxySegurancaConexao conexao) {
		super(principal, credentials, authorities);

		this.conexao = conexao;
	}

	public IProxySegurancaConexao getConexao() {
		return conexao;
	}

}
