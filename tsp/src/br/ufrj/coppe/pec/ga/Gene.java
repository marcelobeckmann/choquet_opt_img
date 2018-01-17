package br.ufrj.coppe.pec.ga;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Representa um gene, isto é um valor presente em um cromossomo.
 * @author beckmann
 *
 */
public class Gene implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2952370762219002359L;
	protected double valor;
	private Alelo alelo;
	public Gene()
	{
		
	}
	/** Localização ordinal que o gene se encontra no cromossomo, iniciada por 0 */ 
	//private int locus;
	private String stringBinaria;
	
	public Gene(double valorReal)  {
		this.valor = valorReal;
		validar();
		
	}
	
		
	public Gene(double valorReal,  Alelo alelo)  {
		
		this.alelo=alelo;
		this.valor = valorReal;
		validar();
		
	}
	public Gene(String stringBinaria,Alelo alelo) 
	{
    	this.alelo=alelo;
		this.stringBinaria=stringBinaria;
		this.valor= Integer.parseInt(stringBinaria, 2);
		validar();
		
	}
	private void validar() throws GeneException
	{
		if (alelo==null)
		{
			return ;
		}
		if (valor<alelo.getLimiteMinimo())
		{
			throw new GeneException("Valor do gene abaixo do mínimo permitido:" +  valor+"<"+alelo.getLimiteMinimo());
		}
		if (valor>alelo.getLimiteMaximo())
		{
			throw new GeneException("Valor do gene acima do máximo permitido:" + valor+">"+alelo.getLimiteMaximo());
			
		}
		
	}
	
//	Getters e setters
	/**
	 * Converte o valor Real do gene para uma String binária.
	 * @return String com o valor do gene representado com uma String de zeros e uns.
	 */
	public String getStringBinaria()
	{
			if (stringBinaria==null) {
				stringBinaria=Integer.toBinaryString((int)valor);
				//Preenche com zeros a esquerda até completar o tamanho do gene
				if (alelo!=null) {
					char[] zeros= new char[alelo.getTamanhoMaximoGene()-stringBinaria.length()];
					Arrays.fill(zeros,'0');
					stringBinaria= String.valueOf(zeros)+ stringBinaria;
				}
				
			}
			return stringBinaria;
	}
	
	public boolean equals(Object o)
	{
		if (o==null || !(o instanceof Gene) )
		{
			return false;
		}
		return this.valor==((Gene)o).getValor();
	}
	
	public double getValor() {
		return valor;
	}
	public Alelo getAlelo()
	{
		return alelo;
	}
	
	public String toString()
	{
		return String.valueOf(valor);
	}
	
	public boolean existeEm(Gene[] genes)
	{
		boolean contains=false;
		for (int i = 0; i < genes.length; i++) {
			
			Gene gene=genes[i];
			if (gene!=null && gene.equals(this))
			{
				contains=true;
				break;
			}
		}
		return contains;
	}
}
