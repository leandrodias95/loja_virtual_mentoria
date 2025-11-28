package jdev.mentoria.lojavirtual;

import java.util.Calendar;
import java.util.List;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jdev.mentoria.lojavirtual.controller.AcessoController;
import jdev.mentoria.lojavirtual.model.Acesso;
import jdev.mentoria.lojavirtual.repository.AcessoRepository;
import junit.framework.TestCase;

@SpringBootTest(classes = LojaVirtualMentoriaApplication.class)
class LojaVirtualMentoriaApplicationTests extends TestCase {

	
	@Autowired
	private AcessoController acessoController;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	@Autowired
	private WebApplicationContext wac;
	
	@Test
	public void testRestApiCadastroAcesso() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder =  MockMvcBuilders.webAppContextSetup(this.wac); //cria um builder mock
		MockMvc mockMvc = builder.build(); 
		Acesso acesso =  new Acesso();
		acesso.setDescricao("ROLE_COMPRADOR" + Calendar.getInstance().getTimeInMillis());
		ObjectMapper objectMapper = new ObjectMapper(); //convert em json
		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.post("/salvarAcesso")
						 .content(objectMapper.writeValueAsString(acesso))
						 .accept(MediaType.APPLICATION_JSON)
						 .contentType(MediaType.APPLICATION_JSON));
		
		System.out.println("Retorno da api: "+retornoApi.andReturn().getResponse().getContentAsString());
		/*Converter o retorno da api para um objeto de acesso*/
		Acesso objetoRetorno = objectMapper.readValue(retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);
		assertEquals(acesso.getDescricao(), objetoRetorno.getDescricao());
		
	}
	

	
	
	@Test
	public void testCadastraAcesso() throws ExceptionMentoriaJava {
		
		Acesso acesso = new Acesso();
		String descAcesso = "ROLE_ADMIN" + Calendar.getInstance().getTimeInMillis();
		acesso.setDescricao(descAcesso);
		acesso = acessoController.salvarAcesso(acesso).getBody();
		assertEquals(true, acesso.getId()>0);
		assertEquals(descAcesso, acesso.getDescricao());
		Acesso acesso2 = acessoRepository.findById(acesso.getId()).get();
		assertEquals(acesso.getId(), acesso2.getId());
		
		/*Teste de delete*/
		
		acessoRepository.deleteById(acesso2.getId());
		acessoRepository.flush();
		Acesso acesso3 = acessoRepository.findById(acesso2.getId()).orElse(null); 
		assertEquals(true, acesso3 == null);
		
		/*Teste de query*/
		
		acesso =  new Acesso(); 
		acesso.setDescricao("ROLE_ALUNO");
		acesso = acessoController.salvarAcesso(acesso).getBody();
		List<Acesso> acessos  = acessoRepository.buscarAcessoDesc("Aluno".trim().toUpperCase());
		assertEquals(1, acessos.size());
		acessoRepository.deleteById(acesso.getId());
	}
	
	
	@Test
	public void testRestApiDeleteAcesso() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder =  MockMvcBuilders.webAppContextSetup(this.wac); //cria um builder mock
		MockMvc mockMvc = builder.build(); 
		Acesso acesso =  new Acesso();
		acesso.setDescricao("ROLE_TESTE_DELETE");
		acesso = acessoRepository.save(acesso);
		ObjectMapper objectMapper = new ObjectMapper(); //convert em json
		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.post("/deleteAcesso")
						 .content(objectMapper.writeValueAsString(acesso))
						 .accept(MediaType.APPLICATION_JSON)
						 .contentType(MediaType.APPLICATION_JSON));
		
		System.out.println("Retorno da api: "+retornoApi.andReturn().getResponse().getContentAsString());
		System.out.println("Status de retorno: "+retornoApi.andReturn().getResponse().getStatus());
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
	}
	
	@Test
	public void testRestApiDeletePorIDAcesso() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder =  MockMvcBuilders.webAppContextSetup(this.wac); //cria um builder mock
		MockMvc mockMvc = builder.build(); 
		Acesso acesso =  new Acesso();
		acesso.setDescricao("ROLE_TESTE_DELETE_ID");
		acesso = acessoRepository.save(acesso);
		ObjectMapper objectMapper = new ObjectMapper(); //convert em json
		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.delete("/deleteAcessoPorId/"+acesso.getId())
						 .accept(MediaType.APPLICATION_JSON)
						 .contentType(MediaType.APPLICATION_JSON));
		
		System.out.println("Retorno da api: "+retornoApi.andReturn().getResponse().getContentAsString());
		System.out.println("Status de retorno: "+retornoApi.andReturn().getResponse().getStatus());
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
	}
	
	@Test
	public void testRestApiObterAcessoId() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder =  MockMvcBuilders.webAppContextSetup(this.wac); //cria um builder mock
		MockMvc mockMvc = builder.build(); 
		Acesso acesso =  new Acesso();
		acesso.setDescricao("ROLE_TESTE_OBTER_ID");
		acesso = acessoRepository.save(acesso);
		ObjectMapper objectMapper = new ObjectMapper(); //convert em json
		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.get("/obterAcesso/"+acesso.getId())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON));
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		Acesso acessoRetorno = objectMapper.readValue(retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);
		assertEquals(acesso.getDescricao(), acessoRetorno.getDescricao());
		assertEquals(acesso.getId(), acessoRetorno.getId());
	}
	
	@Test
	public void testRestApiObterAcessoDesc() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder =  MockMvcBuilders.webAppContextSetup(this.wac); //cria um builder mock
		MockMvc mockMvc = builder.build(); 
		Acesso acesso =  new Acesso();
		acesso.setDescricao("ROLE_TESTE_OBTER_LIST");
		acesso = acessoRepository.save(acesso);
		ObjectMapper objectMapper = new ObjectMapper(); //convert em json
		ResultActions retornoApi = mockMvc
				.perform(MockMvcRequestBuilders.get("/buscarPorDesc/OBTER_LIST")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON));
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		List<Acesso> retornoApiList = objectMapper.readValue(retornoApi.andReturn().getResponse().getContentAsString(), 
				new TypeReference<List<Acesso>>() {});
		assertEquals(acesso.getDescricao(), retornoApiList.get(0).getDescricao());
		acessoRepository.deleteById(acesso.getId()); 
		
	}

}