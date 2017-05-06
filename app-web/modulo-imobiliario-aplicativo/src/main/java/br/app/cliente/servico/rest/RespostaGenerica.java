package br.app.cliente.servico.rest;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class RespostaGenerica implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean status;
	private String mesagem;
	private String codigoErro;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMesagem() {
		return mesagem;
	}

	public void setMesagem(String mesagem) {
		this.mesagem = mesagem;
	}

	public String getCodigoErro() {
		return codigoErro;
	}

	public void setCodigoErro(String codigoErro) {
		this.codigoErro = codigoErro;
	}
	
	@Override
	public String toString() {
		return status + "|" + mesagem + "|" + codigoErro;
	}

}
