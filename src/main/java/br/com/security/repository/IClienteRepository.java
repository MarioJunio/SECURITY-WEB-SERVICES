package br.com.security.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import br.com.security.filters.FiltroConsulta;
import br.com.security.rest.stub.SyncCliente;

public interface IClienteRepository {
	
	int countByField(Pageable pageable, FiltroConsulta filtro);

	List<SyncCliente> findByField(Pageable pageable, FiltroConsulta filtro);
}
