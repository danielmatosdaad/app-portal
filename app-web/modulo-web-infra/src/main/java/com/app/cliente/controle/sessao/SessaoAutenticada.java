package com.app.cliente.controle.sessao;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.app.seguranca.integracao.IProxySegurancaConexao;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.app.cliente.controle.acesso.AutenticacaoAutorizacaoServico;
import com.app.cliente.controle.acesso.UserDetailsImpl;

import br.app.barramento.integracao.exception.InfraEstruturaException;

@Named(value = "sessaoAutenticadaMB")
@SessionScoped
public class SessaoAutenticada implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Crendencial credencial;
	AutenticacaoAutorizacaoServico autenticacaoAutorizacaoServico;

	public SessaoAutenticada() {
		credencial = new Crendencial();
		SecurityContext context = SecurityContextHolder.getContext();
		if (context instanceof SecurityContext) {
			Authentication authentication = context.getAuthentication();
			if (authentication instanceof Authentication) {
				credencial.setNomeAutenticacao(((UserDetailsImpl) authentication.getPrincipal()).getUsername());
				credencial.setSenhaAutenticacao(((UserDetailsImpl) authentication.getPrincipal()).getPassword());
				this.autenticacaoAutorizacaoServico = (AutenticacaoAutorizacaoServico) authentication;
			}
		}
	}

	public Crendencial getCredencial() {
		return credencial;
	}

	public void setCredencial(Crendencial credencial) {
		this.credencial = credencial;
	}

	public IProxySegurancaConexao getConexao() throws InfraEstruturaException {
		if (this.autenticacaoAutorizacaoServico == null || this.autenticacaoAutorizacaoServico.getConexao() == null) {
			throw new InfraEstruturaException(-1, "Coxexao Encerrada");
		}
		return this.autenticacaoAutorizacaoServico.getConexao();
	}

}
