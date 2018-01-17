package br.ufrj.coppe.pec.ga;

import java.io.Serializable;

public interface Individuo extends Serializable, Comparable {

	public String getCromossomo();
	
	public Gene[] getGenes();

	public FuncaoDesempenho getFuncaoDesempenho();
	
	public void setFuncaoDesempenho(FuncaoDesempenho funcao);

	public double getDesempenhoAcumulado();

	public void setDesempenhoAcumulado(double desempenho);

	public int getNumeroGeracao();
	
	public void setNumeroGeracao(int numeroGeracao);

	public Individuo cruzar(Individuo pair);

	public Individuo mutar();


}