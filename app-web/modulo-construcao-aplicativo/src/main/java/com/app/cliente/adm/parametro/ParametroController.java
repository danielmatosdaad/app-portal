package com.app.cliente.adm.parametro;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.slf4j.Logger;
import br.app.barramento.integracao.dto.EnvioDTO;
import br.app.barramento.integracao.dto.RespostaDTO;
import br.app.barramento.integracao.dto.TipoAcao;
import br.app.barramento.integracao.exception.InfraEstruturaException;
import br.app.barramento.integracao.exception.NegocioException;
import br.app.modulo.aplicacao.sessao.SessaoAplicacaoAutenticada;
import br.app.servico.infra.integracao.dto.ParametroDTO;

@Model
@Named(value = "parametroController")
public class ParametroController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	@Inject
	private Event<ParametroDTO> parametroEventSrc;

	@Produces
	@Named
	private ParametroDTO parametroDTO;


	@Inject
	private SessaoAplicacaoAutenticada sessaoAplicacaoAutenticada;


	@PostConstruct
	public void init() {

		try {
			sessaoAplicacaoAutenticada.init();
			inicializarParametroDTO();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info(e.getMessage());
		}

	}



	private void inicializarParametroDTO() {
		this.parametroDTO = new ParametroDTO();
	}

	public void registrar() throws Exception {
		try {

			if (parametroDTO != null) {
				log.info("Reginstrando parametro");
				parametroDTO.setDataInclusao(new Date());

				EnvioDTO registrarParametro = new EnvioDTO();
				registrarParametro.setRequisicao(parametroDTO);
				registrarParametro.setEnvio(parametroDTO.getClass().getName());
				RespostaDTO respostaDTO = sessaoAplicacaoAutenticada.getConexao().executar(TipoAcao.SALVAR,
						registrarParametro, "REPOSITORIO_CORPORATIVO", "CATALOGO_PMT");
				if (respostaDTO != null) {
					log.info("Mensagem" + respostaDTO.getMensagem().getCodigo());
					log.info("Mensagem" + respostaDTO.getMensagem().getErro());
				}
				log.info("Reginstrado com sucesso - id:" + respostaDTO);
				FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_INFO, "Registrado com sucesso!",
						"Registrado com sucesso.");
				facesContext.addMessage(null, m);
				parametroEventSrc.fire(parametroDTO);
			}

		} catch (InfraEstruturaException | NegocioException e) {
			String errorMessage = getRootErrorMessage(e);
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Nao foi posssivel registrar");
			facesContext.addMessage(null, m);
		}
	}

	private String getRootErrorMessage(Exception e) {
		// Default to general error message that registration failed.
		String errorMessage = "Algo nao funcionou bem. Para maiores informacoes consultar o log";
		if (e == null) {
			// This shouldn't happen, but return the default messages
			return errorMessage;
		}

		// Start with the exception and recurse to find the root cause
		Throwable t = e;
		while (t != null) {
			// Get the message from the Throwable class instance
			errorMessage = t.getLocalizedMessage();
			t = t.getCause();
		}
		// This is the root cause message
		return errorMessage;
	}

	public void onRowEdit(RowEditEvent event) {

		Object obj = event.getObject();
		System.out.println(obj);
		FacesMessage msg = new FacesMessage("Parametro Editado", "detalhe");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancel(RowEditEvent event) {
		Object obj = event.getObject();
		FacesMessage msg = new FacesMessage("Parametro Cancelado", "detalhe");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();

		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed",
					"Old: " + oldValue + ", New:" + newValue);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public ParametroDTO getParametroDTO() {
		return parametroDTO;
	}

	public void setParametroDTO(ParametroDTO parametroDTO) {
		this.parametroDTO = parametroDTO;
	}



}
