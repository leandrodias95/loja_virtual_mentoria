package jdev.mentoria.lojavirtual;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import jdev.mentoria.lojavirtual.controller.PessoaController;
import jdev.mentoria.lojavirtual.enums.TipoEndereco;
import jdev.mentoria.lojavirtual.model.Endereco;
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
		
		Endereco endereco1  =  new Endereco(); 
		endereco1.setBairro("jd dias");
		endereco1.setCep("15505189");
		endereco1.setComplemento("casa cinza");
		endereco1.setEmpresa(pessoaJuridica);
		endereco1.setPessoa(pessoaJuridica);
		endereco1.setRuaLogra("Arizona");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("sp");
		endereco1.setCidade("Votuporanga");
		endereco1.setNumero("4569");
		
		
		Endereco endereco2  =  new Endereco(); 
		endereco2.setBairro("jd dias");
		endereco2.setCep("15505189");
		endereco2.setComplemento("casa cinza");
		endereco2.setEmpresa(pessoaJuridica);
		endereco2.setPessoa(pessoaJuridica);
		endereco2.setRuaLogra("Arizona");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("sp");
		endereco2.setCidade("Fernandópolis");
		endereco2.setNumero("4856");
		
		pessoaJuridica.getEnderecos().add(endereco2);
		pessoaJuridica.getEnderecos().add(endereco1);
		
		pessoaJuridica = pessoaController.salvarPj(pessoaJuridica).getBody(); 
		assertEquals(true, pessoaJuridica.getId() > 0 );
		
		for(Endereco endereco : pessoaJuridica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0 );
		}
		
		
		assertEquals(2, pessoaJuridica.getEnderecos().size());
		
		/*PessoaFisica pessoaFisica = new PessoaFisica();
		
		pessoaFisica.setCpf("12345678988");
		pessoaFisica.setNome("Leandro");
		pessoaFisica.setEmail("leandrobatista192@gmail.com");
		pessoaFisica.setTelefone("17997569612");
		pessoaFisica.setEmpresa(pessoaFisica);*/
		
	}

}
