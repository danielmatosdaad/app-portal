package com.app.cliente.resource.util;

import org.modelmapper.ModelMapper;

import br.app.barramento.integracao.exception.InfraEstruturaException;


public class TransferirDados {

	@SuppressWarnings("unchecked")
	public static <T> T transferir(Object objeto, Class<T> destino) throws InfraEstruturaException {
		ModelMapper mapper = new ModelMapper();
		return mapper.map(objeto, destino);

	}
}
