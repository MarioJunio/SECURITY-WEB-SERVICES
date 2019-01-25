package br.com.security.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.security.filters.FiltroConsulta;
import br.com.security.rest.stub.SyncCliente;

@Repository
public class ClienteRepositoryImpl implements IClienteRepository {

	@PersistenceContext
	private EntityManager entityManager;

	private String createConcat(String campo) {

		String concat = "concat(";
		String[] tokens = campo.split("\\+");

		if (tokens.length > 1) {

			for (String token : tokens)
				concat += token + ",";

			concat = concat.substring(0, concat.length() - 1) + ")";

		} else {
			concat += campo + ")";
		}

		return concat;
	}

	@Override
	public int countByField(Pageable pageable, FiltroConsulta filtro) {

		String countJPQL;
		Query query;
		String campo;

		if (filtro.getCampo().endsWith("ativo") && !filtro.getStatus().equals("ambos")) {
			countJPQL = "select count(a.id) from Cliente a where a.ativo = :ativo";
			query = entityManager.createQuery(countJPQL);
			query.setParameter("ativo", filtro.getStatus().equals("ativo") ? true : false);
		} else if (filtro.getCampo().endsWith("ativo") && filtro.getStatus().equals("ambos")) {
			countJPQL = "select count(a.id) from Cliente a";
			query = entityManager.createQuery(countJPQL);
		} else {
			campo = createConcat(filtro.getCampo());
			countJPQL = "select count(a.id) from Cliente a where " + campo + " like :value";
			query = entityManager.createQuery(countJPQL.replace("<domain>", "a"));
			query.setParameter("value", String.format("%%%s%%", filtro.getValor()));
		}

		return ((Number) query.getSingleResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SyncCliente> findByField(Pageable pageable, FiltroConsulta filtro) {

		String jpql;
		Query query;
		String campo;

		String orderBy = " order by a.nome asc";

		String select = "select new br.com.security.rest.stub.SyncCliente(a.id, a.nome, a.email, a.tipo, a.cpf, a.cnpj, a.telefone1, a.telefone2, a.logradouro, a.numero, a.bairro, a.cep, a.cidade.nome, a.cidade.uf, a.ativo) ";

		if (filtro.getCampo().endsWith("ativo") && !filtro.getStatus().equals("ambos")) {
			jpql = select + "from Cliente a where a.ativo = :ativo" + orderBy;
			query = entityManager.createQuery(jpql);
			query.setParameter("ativo", filtro.getStatus().equals("ativo") ? true : false);
		} else if (filtro.getCampo().endsWith("ativo") && filtro.getCampo().equals("ambos")) {
			jpql = select + "from Cliente a" + orderBy;
			query = entityManager.createQuery(jpql);
		} else {
			campo = createConcat(filtro.getCampo());
			jpql = select + "from Cliente a where " + campo + " like :value" + orderBy;
			query = entityManager.createQuery(jpql.replace("<domain>", "a"));
			query.setParameter("value", String.format("%%%s%%", filtro.getValor()));
		}

		int offset = pageable.getPageNumber() * pageable.getPageSize();

		return query.setFirstResult(offset).setMaxResults(pageable.getPageSize()).getResultList();
	}

}
