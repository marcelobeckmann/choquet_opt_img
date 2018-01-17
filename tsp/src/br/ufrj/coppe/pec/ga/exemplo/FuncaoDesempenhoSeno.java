package br.ufrj.coppe.pec.ga.exemplo;

import br.ufrj.coppe.pec.ga.FuncaoDesempenho;
import br.ufrj.coppe.pec.ga.Gene;
import br.ufrj.coppe.pec.ga.Individuo;

public class FuncaoDesempenhoSeno implements FuncaoDesempenho {

	private boolean calculado = false;

	private double desempenho = 0;

	/**
	 * Efetua o calculo de uma função senoide.
	 */
	public double calcular(Individuo individuo) {

		if (!calculado) {
			
			//Aqui está o miolo da função de fitness
			//------- inicio funcao
			Gene[] genes = individuo.getGenes();
			for (int i = 0; i < genes.length; ++i) {
				double x = genes[i].getValor();
				desempenho += (x * Math.sin(10 * Math.PI * x)) + 1;

			}
			//-------- fim funcao
			
			
			calculado=true;
		}
		return desempenho;
	}

	public double getDesempenho() {
		return desempenho;
	}
	public short getOrdem()
	{
		return ORDEM_DECRESCENTE;  //Quanto maior melhor
	}

	public void clean() {
		// TODO Auto-generated method stub
		
	}

	public long getElapsedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

}
