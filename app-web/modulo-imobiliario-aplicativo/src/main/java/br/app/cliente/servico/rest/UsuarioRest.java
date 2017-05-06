package br.app.cliente.servico.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import br.app.barramento.integracao.dto.EnvioDTO;
import br.app.barramento.integracao.dto.RespostaDTO;
import br.app.barramento.integracao.dto.TipoAcao;
import br.app.corporativo.integracao.databuilder.ContatoBuilder;
import br.app.corporativo.integracao.databuilder.SenhaBuilder;
import br.app.corporativo.integracao.databuilder.UsuarioBuilder;
import br.app.corporativo.integracao.dto.ContatoDTO;
import br.app.corporativo.integracao.dto.SenhaDTO;
import br.app.corporativo.integracao.dto.StatusSenhaDTO;
import br.app.corporativo.integracao.dto.StatusUsuarioDTO;
import br.app.corporativo.integracao.dto.TipoContatoDTO;
import br.app.corporativo.integracao.dto.UsuarioDTO;
import br.app.modulo.aplicacao.sessao.SessaoAplicacaoAutenticada;

@Path("/imobiliarioaplicativo/views/restful-services/imobiliario/usuario")
@Consumes({ "application/xml", "application/json" })
@Produces({ "application/xml", "application/json" })
@RequestScoped
@Named
public class UsuarioRest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private SessaoAplicacaoAutenticada sessaoAplicacaoAutenticada;

	public void init() {
		System.out.println("init UsuarioRest");
	}

	@POST // utilizando apenas o verbo POST
	public Response registrar(@QueryParam("tipoUsuario") String tipoUsuario, @QueryParam("nome") String nome,
			@QueryParam("sobrenome") String sobrenome, @QueryParam("telefone") String telefone,
			@QueryParam("sobrevoce") String sobrevoce, @QueryParam("email") String email,
			@QueryParam("senha") String senha, @QueryParam("genero") String genero,
			@QueryParam("termoLido") String termoLido) {
		try {

			ContatoDTO contatoEmail = ContatoBuilder.umaInstancia().comTipoContato(TipoContatoDTO.EMAIL).comValor(email)
					.construir();
			ContatoDTO contatoTelefone = ContatoBuilder.umaInstancia().comTipoContato(TipoContatoDTO.CELULAR)
					.comValor(telefone).construir();

			List<ContatoDTO> contatos = new ArrayList<ContatoDTO>();
			contatos.add(contatoTelefone);
			contatos.add(contatoEmail);

			SenhaDTO senhaAcesso = SenhaBuilder.umaInstancia().comHashSenha(senha).comHashSenha(senha)
					.comStatusSenha(StatusSenhaDTO.ATIVO).construir();

			List<SenhaDTO> senhas = new ArrayList<SenhaDTO>();
			senhas.add(senhaAcesso);

			UsuarioDTO usuarioDTO = UsuarioBuilder.umaInstancia().comNome(nome).comSobrenome(sobrenome)
					.comStatusUsuario(StatusUsuarioDTO.ATIVO).comLogin(email).comInitChave("AAAAAAAAAAAAAAAA")
					.comChave("BBBBBBBBBB").comCpf("00217353100").comDataNascimento("13/05/1987").comContatos(contatos)
					.comSenhas(senhas).construir();

			EnvioDTO registrarParametro = new EnvioDTO();
			registrarParametro.setRequisicao(usuarioDTO);
			registrarParametro.setEnvio(usuarioDTO.getClass().getName());

			RespostaDTO respostaDTO = sessaoAplicacaoAutenticada.getConexao().executar(TipoAcao.SALVAR,
					registrarParametro, "REPOSITORIO_CORPORATIVO", "CATALOGO_USR");

			if (respostaDTO != null) {
				System.out.println(respostaDTO.getMensagem().getCodigo());
				System.out.println("Mensagem" + respostaDTO.getMensagem().getErro());
			}

			RespostaGenerica reposta = new RespostaGenerica();

			return Response.ok(reposta).build();
		} catch (Exception e) {
		}

		return Response.serverError().build();

	}
}
