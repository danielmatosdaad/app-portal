package br.app.modulo.infra.builder;

import java.util.Date;

import br.app.barramento.autenticacao.dto.AutenticacaoEnvio;
import br.app.barramento.autenticacao.dto.TipoAutenticacao;

public class AutenticacaoBuilder {

	private Long idetificadorAutenticacao;
	private String nomeIdentificadorAutenticacao;
	private String senha;
	private String ipporta;
	private String identificadorDispotivo;
	private String brownser;
	private Date datahora;
	private TipoAutenticacao tipoAutenticacao;

	public static AutenticacaoBuilder construtor() {

		return new AutenticacaoBuilder();
	}

	public AutenticacaoBuilder comIdetificadorAutenticacao(Long idetificadorAutenticacao) {

		this.idetificadorAutenticacao = idetificadorAutenticacao;
		return this;
	}

	public AutenticacaoBuilder comNomeIdentificadorAutenticacao(String nomeIdentificadorAutenticacao) {

		this.nomeIdentificadorAutenticacao = nomeIdentificadorAutenticacao;
		return this;
	}

	public AutenticacaoBuilder comSenha(String senha) {

		this.senha = senha;
		return this;
	}

	public AutenticacaoBuilder comIpPorta(String ipporta) {

		this.ipporta = ipporta;
		return this;
	}

	public AutenticacaoBuilder comIdentificadorDispotivo(String identificadorDispotivo) {

		this.identificadorDispotivo = identificadorDispotivo;
		return this;
	}

	public AutenticacaoBuilder comBrownser(String brownser) {

		this.brownser = brownser;
		return this;
	}

	public AutenticacaoBuilder comDatahora(Date datahora) {

		this.datahora = datahora;
		return this;
	}

	public AutenticacaoBuilder comTipoAutenticacao(TipoAutenticacao tipoAutenticacao) {

		this.tipoAutenticacao = tipoAutenticacao;
		return this;
	}

	public AutenticacaoEnvio contruir() {

		return new AutenticacaoEnvio(idetificadorAutenticacao, nomeIdentificadorAutenticacao, senha, ipporta,
				identificadorDispotivo, brownser, datahora, tipoAutenticacao);
	}
}
