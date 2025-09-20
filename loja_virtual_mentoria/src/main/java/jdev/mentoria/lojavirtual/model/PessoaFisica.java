package jdev.mentoria.lojavirtual.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;


@Entity
@Table(name = "pessoa_fisica")
public class PessoaFisica extends Pessoa {


	private static final long serialVersionUID = 1L;


	@Column(nullable = false)
	private String cpf;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	@OneToMany(mappedBy = "pessoaFisica", orphanRemoval = true, cascade = CascadeType.ALL, fetch =FetchType.LAZY) //orphanRemoval = se apaga uma pessoa apaga os endereços
	private List<Endereco> enderecos =  new ArrayList<Endereco>(); 


	public String getCpf() {
		return cpf;
	}


	public void setCpf(String cpf) {
		this.cpf = cpf;
	}


	public Date getDataNascimento() {
		return dataNascimento;
	}


	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}


	public List<Endereco> getEnderecos() {
		return enderecos;
	}


	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}


}

