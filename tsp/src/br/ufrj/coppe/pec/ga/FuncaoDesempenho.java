package br.ufrj.coppe.pec.ga;

import java.io.Serializable;

public interface FuncaoDesempenho extends Serializable {

	
	/** Quanto menor melhor */
	public static final short ORDEM_CRESCENTE=1;
	/** Quanto maior melhor */
	public static final short ORDEM_DECRESCENTE=-1;
	public double getDesempenho();
	public double calcular(Individuo individuo);
	public short getOrdem();
	/**
	 Release stuffs to GC
	 */
	public void clean();
	/**
	 * Returns the elapsed time to calculate the fitness value.
	 * @return
	 */
	public long getElapsedTime();
	
}
