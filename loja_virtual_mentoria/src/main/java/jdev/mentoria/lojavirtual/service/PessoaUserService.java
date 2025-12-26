package jdev.mentoria.lojavirtual.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.Usuario;
import jdev.mentoria.lojavirtual.model.dto.CepDTO;
import jdev.mentoria.lojavirtual.model.dto.ConsultaCnpjDTO;
import jdev.mentoria.lojavirtual.repository.PessoaFisicaRepository;
import jdev.mentoria.lojavirtual.repository.PessoaJuridicaRepository;
import jdev.mentoria.lojavirtual.repository.UsuarioRepository;

@Service
public class PessoaUserService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PessoaJuridicaRepository pessoaRepository; 
	
	@Autowired
	private JdbcTemplate jdbcTemplate; 
	
	@Autowired
	private ServiceEnvioEmail serviceEnvioEmail; 
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository; 
	
	
	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica pessoaJuridica) {
		//pessoaJuridica = pessoaRepository.save(pessoaJuridica);
		for(int i= 0; i < pessoaJuridica.getEnderecos().size(); i++) { 
			pessoaJuridica.getEnderecos().get(i).setPessoa(pessoaJuridica);
			pessoaJuridica.getEnderecos().get(i).setEmpresa(pessoaJuridica);
		}
		Usuario usuarioPj = usuarioRepository.findUserByPessoa(pessoaJuridica.getId(), pessoaJuridica.getEmail()); 
		pessoaJuridica = pessoaRepository.save(pessoaJuridica);
		if(usuarioPj == null) {
			String constriant = usuarioRepository.consultaConstraintAcesso(); 
			if(constriant != null) {
				jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint "+constriant+";commit;"); 
			}
			usuarioPj =  new Usuario();
			usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPj.setEmpresa(pessoaJuridica);
			usuarioPj.setPessoa(pessoaJuridica);
			usuarioPj.setLogin(pessoaJuridica.getEmail());
			
			String senha = "123"; //+Calendar.getInstance().getTimeInMillis();
			String senhaCript = new BCryptPasswordEncoder().encode(senha);
			usuarioPj.setSenha(senhaCript);
			usuarioPj = usuarioRepository.save(usuarioPj);
			usuarioRepository.insereAcessoUser(usuarioPj.getId());
			usuarioRepository.insereAcessoUserPj(usuarioPj.getId(), "ROLE_ADMIN");
			StringBuilder messageHtml =  new StringBuilder(); 	
			messageHtml.append("<h2>Segue abaixo seus dados de acesso a loja virtual</h2>"); 
			messageHtml.append("<b>Login: </b>"+pessoaJuridica.getEmail()+"<br/>"); 
			messageHtml.append("<b>Senha: </b>"+senha+"<br/><br/>"); 
			messageHtml.append("Obrigado!"); 

			
			try {
			serviceEnvioEmail.enviarEmailHtml("Acesso Gerado para Loja Virtual", messageHtml.toString(), pessoaJuridica.getEmail());
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return pessoaJuridica; 
	}
	
	public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {
		for(int i= 0; i < pessoaFisica.getEnderecos().size(); i++) { 
			pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica);
			pessoaFisica.getEnderecos().get(i).setEmpresa(pessoaFisica);
		}
		Usuario usuarioPj = usuarioRepository.findUserByPessoa(pessoaFisica.getId(), pessoaFisica.getEmail()); 
		pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);
		if(usuarioPj == null) {
			String constriant = usuarioRepository.consultaConstraintAcesso(); 
			if(constriant != null) {
				jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint "+constriant+";commit;"); 
			}
			usuarioPj =  new Usuario();
			usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
			usuarioPj.setEmpresa(pessoaFisica);
			usuarioPj.setPessoa(pessoaFisica);
			usuarioPj.setLogin(pessoaFisica.getEmail());
			
			String senha = "123"; //+Calendar.getInstance().getTimeInMillis();
			String senhaCript = new BCryptPasswordEncoder().encode(senha);
			usuarioPj.setSenha(senhaCript);
			usuarioPj = usuarioRepository.save(usuarioPj);
			usuarioRepository.insereAcessoUser(usuarioPj.getId());
			StringBuilder messageHtml =  new StringBuilder(); 	
			messageHtml.append("<h2>Segue abaixo seus dados de acesso a loja virtual</h2>"); 
			messageHtml.append("<b>Login: </b>"+pessoaFisica.getEmail()+"<br/>"); 
			messageHtml.append("<b>Senha: </b>"+senha+"<br/><br/>"); 
			messageHtml.append("Obrigado!"); 

			
			try {
			serviceEnvioEmail.enviarEmailHtml("Acesso Gerado para Loja Virtual", messageHtml.toString(), pessoaFisica.getEmail());
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return pessoaFisica; 
	}
	
	public CepDTO consultaCep(String cep) {
		return new RestTemplate().getForEntity("https://viacep.com.br/ws/"+cep+"/json/", CepDTO.class).getBody();
	}
	
	public ConsultaCnpjDTO consultaReceitaWS(String cnpj) {
		return new RestTemplate().getForEntity("https://receitaws.com.br/v1/cnpj/"+cnpj+"", ConsultaCnpjDTO.class).getBody();
		
	}
}
