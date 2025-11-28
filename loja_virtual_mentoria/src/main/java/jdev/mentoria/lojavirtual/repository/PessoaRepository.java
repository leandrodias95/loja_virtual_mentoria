package jdev.mentoria.lojavirtual.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.Pessoa;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;

@Repository
public interface PessoaRepository extends CrudRepository<Pessoa, Long>{

	@Query(value=" select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public  PessoaJuridica existeCnpjCadastrado(String cnpj);
}
