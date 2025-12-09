package jdev.mentoria.lojavirtual;

import jdev.mentoria.lojavirtual.util.ValidaCNPJ;
import jdev.mentoria.lojavirtual.util.ValidaCPF;

public class TesteCPFCNPJ {
	
	public static void main(String[] args) {
		boolean isCnpj  = ValidaCNPJ.isCNPJ("03.770.007/0001-80");
		boolean isCpf  = ValidaCPF.isCPF("428.516.898-78");
		System.out.println("Cnpj válido: "+isCnpj);
		System.out.println("Cpf válido: "+isCpf);
	}

}
