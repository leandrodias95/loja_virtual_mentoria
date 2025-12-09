package jdev.mentoria.lojavirtual.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.PessoaFisica;

@Repository
public interface PessoaFisicaRepository extends CrudRepository<PessoaFisica, Long>{

	
	@Query(value=" select pf from PessoaFisica pf where pf.cpf = ?1")
	public  PessoaFisica existeCpfCadastrado(String cpf);
}
