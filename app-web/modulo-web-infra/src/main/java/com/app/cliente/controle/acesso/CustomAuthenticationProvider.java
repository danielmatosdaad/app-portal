package com.app.cliente.controle.acesso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.app.seguranca.api.ProxySegurancaDelegate;
import org.app.seguranca.integracao.IProxySegurancaConexao;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.app.barramento.autenticacao.dto.AutenticacaoEnvio;
import br.app.barramento.autenticacao.dto.TipoAutenticacao;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String username = authentication.getName();
		String password = (String) authentication.getCredentials();
		System.out.println("User:" + username);
		System.out.println("password:" + password);
		System.out.println("password:" + authentication.getDetails());

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

		String useragent = RequestContextHolderUtil.getRequest().getHeader("User-Agent");
		String ipAdress = RequestContextHolderUtil.getRemoteAddr();
		AutenticacaoEnvio autenticacaoEnvio = criaAutenticacaoEnvio(username, password, useragent, ipAdress);
		try {
			IProxySegurancaConexao proxySegurancaConexao = ProxySegurancaDelegate.getInstancia().getServico();
			proxySegurancaConexao.autenticacaoAutorizacao(autenticacaoEnvio);

			Collection<GrantedAuthority> autorizacaoes = new ArrayList<GrantedAuthority>();
			GrantedAuthority autorizacao = new GrantedAuthorityImpl("ROLE_ADMIN");
			autorizacaoes.add(autorizacao);
			UserDetailsImpl user = new UserDetailsImpl(username, password, autorizacaoes);
			return new AutenticacaoAutorizacaoServico(user, password, autorizacaoes, proxySegurancaConexao);
		} catch (Exception e) {
			throw new BadCredentialsException(e.getMessage());
		}
	}

	private AutenticacaoEnvio criaAutenticacaoEnvio(String username, String password, String useragent,
			String ipAdress) {
		AutenticacaoEnvio envio = new AutenticacaoEnvio();
		envio.setNomeIdentificadorAutenticacao(username);
		envio.setSenha(password);
		envio.setBrownser(useragent);
		envio.setIpporta(ipAdress);
		envio.setDatahora(new Date());
		envio.setIdentificadorDispotivo("macos");
		envio.setTipoAutenticacao(TipoAutenticacao.USUARIO);
		return envio;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
