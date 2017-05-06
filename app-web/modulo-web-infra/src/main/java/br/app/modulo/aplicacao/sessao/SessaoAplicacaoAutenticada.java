package br.app.modulo.aplicacao.sessao;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.app.seguranca.api.ProxySegurancaDelegate;
import org.app.seguranca.integracao.IProxySegurancaConexao;

import br.app.barramento.autenticacao.dto.AutenticacaoEnvio;
import br.app.barramento.autenticacao.dto.TipoAutenticacao;
import br.app.barramento.integracao.dto.EnvioDTO;
import br.app.barramento.integracao.dto.RespostaDTO;
import br.app.barramento.integracao.dto.TipoAcao;
import br.app.barramento.integracao.exception.InfraEstruturaException;
import br.app.barramento.integracao.exception.NegocioException;
import br.app.modulo.infra.builder.AutenticacaoBuilder;

@ApplicationScoped
@Named
public class SessaoAplicacaoAutenticada implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private IProxySegurancaConexao proxySegurancaConexao;
	private AutenticacaoEnvio autenticacao;
	private Boolean autenticado;

	private String ipPorta = "127.0.0.1";
	private String hasDispositivo = "mac-daniel";

	public SessaoAplicacaoAutenticada() {

	}

	@PostConstruct
	public void init() {

		try {

			if (this.proxySegurancaConexao == null) {
				this.autenticacao = AutenticacaoBuilder.construtor().comNomeIdentificadorAutenticacao("aplicativo.imob")
						.comSenha("imob").comBrownser("").comIdentificadorDispotivo(hasDispositivo).comIpPorta(ipPorta)
						.comDatahora(new Date()).comTipoAutenticacao(TipoAutenticacao.APLICATIVO).contruir();

				this.proxySegurancaConexao = ProxySegurancaDelegate.getInstancia().getServico();
				this.proxySegurancaConexao.autenticacaoAutorizacao(autenticacao);
				this.autenticado = Boolean.TRUE;
			} else if (this.autenticado == Boolean.FALSE) {
				this.proxySegurancaConexao.autenticacaoAutorizacao(autenticacao);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public IProxySegurancaConexao getConexao() throws InfraEstruturaException {
		if (this.autenticado == null || this.autenticado == true && this.proxySegurancaConexao != null) {
			return this.proxySegurancaConexao;
		} else {
			throw new InfraEstruturaException(-1, "Coxexao Encerrada");
		}
	}

	public RespostaDTO executar(TipoAcao acao, EnvioDTO envio, String nomeRepositorio, String nomeCatalogo)
			throws NegocioException, InfraEstruturaException {
		try {
			return getConexao().executar(acao, envio, nomeRepositorio, nomeCatalogo);
		} catch (Exception e) {

			// caso o servidor caiu e foi restardado pode
			// ter a execao Could not find EJB
			// tentar reconectar e reexecutar
			if (e instanceof javax.ejb.NoSuchEJBException) {
				this.init();
				return getConexao().executar(acao, envio, nomeRepositorio, nomeCatalogo);
			}
			throw e;
		}

	}

	public String getIpPorta() {
		return ipPorta;
	}

	public void setIpPorta(String ipPorta) {
		this.ipPorta = ipPorta;
	}

	public String getHasDispositivo() {
		return hasDispositivo;
	}

	public void setHasDispositivo(String hasDispositivo) {
		this.hasDispositivo = hasDispositivo;
	}

}
