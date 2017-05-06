package com.app.cliente.controle.login;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import com.app.cliente.controle.sessao.SessaoAutenticada;

@ManagedBean(name = "controlelogin")
@RequestScoped
public class LoginControl {

	@ManagedProperty("#{sessaoAutenticada}")
	private SessaoAutenticada sessaoAutenticada;
	
	public void autenticar(){
		
	}
}
