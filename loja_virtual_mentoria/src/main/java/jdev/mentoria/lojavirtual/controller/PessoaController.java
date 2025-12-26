package jdev.mentoria.lojavirtual.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jdev.mentoria.lojavirtual.ExceptionMentoriaJava;
import jdev.mentoria.lojavirtual.enums.TipoPessoa;
import jdev.mentoria.lojavirtual.model.Endereco;
import jdev.mentoria.lojavirtual.model.PessoaFisica;
import jdev.mentoria.lojavirtual.model.PessoaJuridica;
import jdev.mentoria.lojavirtual.model.dto.CepDTO;
import jdev.mentoria.lojavirtual.model.dto.ConsultaCnpjDTO;
import jdev.mentoria.lojavirtual.repository.EnderecoRepository;
import jdev.mentoria.lojavirtual.repository.PessoaFisicaRepository;
import jdev.mentoria.lojavirtual.repository.PessoaJuridicaRepository;
import jdev.mentoria.lojavirtual.service.PessoaUserService;
import jdev.mentoria.lojavirtual.service.ServiceContagemAcessoApi;
import jdev.mentoria.lojavirtual.util.ValidaCNPJ;
import jdev.mentoria.lojavirtual.util.ValidaCPF;

@RestController
public class PessoaController {
	
	@Autowired
	private PessoaJuridicaRepository pessoaRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoafisicaRepository;
	
	@Autowired
	private PessoaUserService pessoaUserService;
	
	@Autowired
	private EnderecoRepository enderecoRepository; 
	
	@Autowired
	private ServiceContagemAcessoApi serviceContagemAcessoApi;
	
	@ResponseBody
	@GetMapping(value= "**/consultaCep/{cep}")
	public ResponseEntity<CepDTO> consultaCep(@PathVariable("cep") String cep){
		CepDTO cepDTO = pessoaUserService.consultaCep(cep);
		return new ResponseEntity<CepDTO>(cepDTO, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value= "**/consultaCnpjReceitaWs/{cnpj}")
	public ResponseEntity<ConsultaCnpjDTO> consultaCnpj(@PathVariable("cnpj") String cnpj){
		ConsultaCnpjDTO consultaCnpjDTO = pessoaUserService.consultaReceitaWS(cnpj);
		return new ResponseEntity<ConsultaCnpjDTO>(consultaCnpjDTO, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value ="**/consultaPfNome/{nome}")
	public ResponseEntity<List<PessoaFisica>> consultaPfNome(@PathVariable("nome")String nome){
		List<PessoaFisica> fisicas = pessoafisicaRepository.pesquisaPorNomePF(nome.trim().toUpperCase());
		serviceContagemAcessoApi.atualizaAcessoEndPointPF();
		return new ResponseEntity<List<PessoaFisica>>(fisicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value ="**/consultaPfCpf/{cpf}")
	public ResponseEntity<List<PessoaFisica>> consultaPfCpf(@PathVariable("cpf")String cpf){
		List<PessoaFisica> fisicas = pessoafisicaRepository.existeCpfCadastradoList(cpf);
		return new ResponseEntity<List<PessoaFisica>>(fisicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value ="**/consultaNomePj/{nome}")
	public ResponseEntity<List<PessoaJuridica>> consultaNomePj(@PathVariable("nome")String nome){
		List<PessoaJuridica> juridicas = pessoaRepository.pesquisaPorNomePj(nome.trim().toUpperCase());
		return new ResponseEntity<List<PessoaJuridica>>(juridicas, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value ="**/consultaCnpjPj/{cnpj}")
	public ResponseEntity<List<PessoaJuridica>> consultaPorCnpj(@PathVariable("cnpj")String nome){
		List<PessoaJuridica> juridicas = pessoaRepository.existeCnpjCadastradoList(nome.trim());
		return new ResponseEntity<List<PessoaJuridica>>(juridicas, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@PostMapping(value = "**/salvarPj")
	public ResponseEntity<PessoaJuridica>salvarPj(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws ExceptionMentoriaJava{
		if(pessoaJuridica == null) {
			throw new ExceptionMentoriaJava("Pessoa Jurídica não pode ser null");
		}
		if(pessoaJuridica.getTipoPessoa() == null) {
			throw new ExceptionMentoriaJava("Informe o tipo jurídico ou fornecedor da loja");
		}
		
		if(pessoaJuridica.getId()== null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
			throw new ExceptionMentoriaJava("Já existe CNPJ cadastrado com o número: "+ pessoaJuridica.getCnpj());
		}
		
		
		if(pessoaJuridica.getId()== null && pessoaRepository.existeInscEstadualCadastrado(pessoaJuridica.getInscEstadual()) != null) {
			throw new ExceptionMentoriaJava("Já existe Inscrição Estadual cadastrado com o número: "+ pessoaJuridica.getInscEstadual());
		}
		
		if (!ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj())) {
			throw new ExceptionMentoriaJava("CNPJ: "+pessoaJuridica.getCnpj()+ " está inválido!");
		}
		if(pessoaJuridica.getId() == null || pessoaJuridica.getId() <= 0) {
			for (int p  = 0; p < pessoaJuridica.getEnderecos().size(); p++) {
				CepDTO cepDTO = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
				pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
				pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
				pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
				pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDTO.getLogradouro());
				pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
				
				
			}
			
		} else {
			for (int p  = 0; p < pessoaJuridica.getEnderecos().size(); p++) {
				Endereco enderecoTemp = enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();
				if(!enderecoTemp.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep())) {
					CepDTO cepDTO = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
					pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
					pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
					pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
					pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDTO.getLogradouro());
					pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
				}
			}
		}
		
		pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);
		
		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
		
	}
	

	@ResponseBody
	@PostMapping(value = "**/salvarPf")
	public ResponseEntity<PessoaFisica>salvarPf(@RequestBody PessoaFisica pessoaFisica) throws ExceptionMentoriaJava{
		if(pessoaFisica == null) {
			throw new ExceptionMentoriaJava("Pessoa Física não pode ser null");
		}
		if(pessoaFisica.getId()== null && pessoafisicaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
			throw new ExceptionMentoriaJava("Já existe CPF cadastrado com o número: "+ pessoaFisica.getCpf());
		}
		
		if(pessoaFisica.getTipoPessoa() == null) {
			pessoaFisica.setTipoPessoa(TipoPessoa.FISICA.name());
		}
		
		if (!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
			throw new ExceptionMentoriaJava("CPF: "+pessoaFisica.getCpf()+ " está inválido!");
		}
		
		pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica);
		
		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
		
	}

}
