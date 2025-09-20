package jdev.mentoria.lojavirtual.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "cup_desc")
@SequenceGenerator(name = "seq_cup_desc", sequenceName = "seq_cup_desc", allocationSize = 1, initialValue = 1)
public class CupDesc implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cup_desc")
	private Long id;
	
	@Column(nullable = false)
	private BigDecimal codDesc; 
	
	private BigDecimal valorRealDesc; 
	
	private BigDecimal valorPorcentDesc; 
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date dataValidadeCupom;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public BigDecimal getCodDesc() {
		return codDesc;
	}


	public void setCodDesc(BigDecimal codDesc) {
		this.codDesc = codDesc;
	}


	public BigDecimal getValorRealDesc() {
		return valorRealDesc;
	}


	public void setValorRealDesc(BigDecimal valorRealDesc) {
		this.valorRealDesc = valorRealDesc;
	}


	public BigDecimal getValorPorcentDesc() {
		return valorPorcentDesc;
	}


	public void setValorPorcentDesc(BigDecimal valorPorcentDesc) {
		this.valorPorcentDesc = valorPorcentDesc;
	}


	public Date getDataValidadeCupom() {
		return dataValidadeCupom;
	}


	public void setDataValidadeCupom(Date dataValidadeCupom) {
		this.dataValidadeCupom = dataValidadeCupom;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CupDesc other = (CupDesc) obj;
		return Objects.equals(id, other.id);
	}
	
	

}
