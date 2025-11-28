package jdev.mentoria.lojavirtual;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import jdev.mentoria.lojavirtual.controller.PessoaController;
import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojaVirtualMentoriaApplication.class)
public class TestePessoaUsuario extends TestCase {
	
	
	@Autowired
	private PessoaController pessoaController;
	
	@Test
	public void testCadPessoa() throws ExceptionMentoriaJava {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		
		pessoaJuridica.setCnpj(""+Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setNome("Leandro");
		pessoaJuridica.setEmail("leandrobatista192@gmail.com");
		pessoaJuridica.setTelefone("17997569612");
		pessoaJuridica.setInscEstadual("5556565656565656");
		pessoaJuridica.setRazaoSocial("Empresa Teste");
		pessoaJuridica.setNomeFantasia("Empresa Teste");
		
		pessoaController.salvarPj(pessoaJuridica); 
		
		/*PessoaFisica pessoaFisica = new PessoaFisica();
		
		pessoaFisica.setCpf("12345678988");
		pessoaFisica.setNome("Leandro");
		pessoaFisica.setEmail("leandrobatista192@gmail.com");
		pessoaFisica.setTelefone("17997569612");
		pessoaFisica.setEmpresa(pessoaFisica);*/
		
	}

}
