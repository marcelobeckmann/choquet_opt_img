package br.ufrj.coppe.pec.ga;

import java.io.Serializable;

public class Alelo implements Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1739793417392627709L;
	private int limiteMinimo= Integer.MIN_VALUE;
	private int limiteMaximo= Integer.MAX_VALUE;
	private int tamanhoMaximoGene=16;
	public Alelo()
	{}
	public Alelo(int limiteMinimo, int limiteMaximo) {
		this.limiteMinimo = limiteMinimo;
		this.limiteMaximo = limiteMaximo;
		//TODO Melhorar isso!!
		tamanhoMaximoGene =Integer.toBinaryString(limiteMaximo).length();
	}
	public int getLimiteMaximo() {
		return limiteMaximo;
	}
	public int getLimiteMinimo() {
		return limiteMinimo;
	}
	public int getTamanhoMaximoGene() {
		return tamanhoMaximoGene;
	}


}
