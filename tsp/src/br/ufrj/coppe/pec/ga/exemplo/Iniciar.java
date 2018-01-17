package br.ufrj.coppe.pec.ga.exemplo;

import br.ufrj.coppe.pec.ga.Ecosistema;

public class Iniciar {

	
	public static void main(String[] args) {
		
		
		Ecosistema ecosistema= new EcosistemaSeno();
		
		
		ecosistema.setPopulacaoInicial(5);
		ecosistema.setPopulacaoMaxima(5);
		ecosistema.setPercentualMutacao(5);
		ecosistema.setPercentualCruzamento(40);
		ecosistema.setMaximoGeracoes(100);
		
		
		Thread t= new Thread(ecosistema);
		
		
		
		t.start();
		
		

	}

}
