package jdev.mentoria.lojavirtual.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jdev.mentoria.lojavirtual.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
	
	
	@Query("select u from Usuario u left join fetch u.acessos where u.login = :login")
	Usuario findUserByLogin(@Param("login") String login);

	@Query(value = "select u from Usuario u where u.pessoa.id = ?1 or u.login = ?2")
	Usuario findUserByPessoa(Long id, String email);

	@Query(value= "select constraint_name from information_schema.constraint_column_usage\n"
			+ " where table_name = 'usuarios_acesso' and column_name = 'acesso_id'\n"
			+ "and constraint_name <> 'unique_acesso_user';", nativeQuery = true)
	String consultaConstraintAcesso();

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO usuarios_acesso (usuario_id, acesso_id) VALUES (?1, (SELECT id FROM acesso WHERE descricao = 'ROLE_USER'))", nativeQuery = true)
	void insereAcessoUser(Long id);
	
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO usuarios_acesso (usuario_id, acesso_id) VALUES (?1, (SELECT id FROM acesso WHERE descricao = '?2'))", nativeQuery = true)
	void insereAcessoUserPj(Long id, String acesso);
	
	@Query("select u from Usuario u where u.dataAtualSenha <= current_date - 90")
	List<Usuario> usuarioSenhaVencida(); 

}