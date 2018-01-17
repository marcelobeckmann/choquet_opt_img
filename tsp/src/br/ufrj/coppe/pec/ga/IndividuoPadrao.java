package br.ufrj.coppe.pec.ga;


import java.util.*;

public  class IndividuoPadrao implements Individuo {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3473671681141374264L;
	protected Gene[] genes;
	protected String cromossomo;
	protected int numeroGeracao;
	private double desempenhoAcumulado;
	protected FuncaoDesempenho funcaoDesempenho;

	protected IndividuoPadrao() {}
	
	public IndividuoPadrao(Gene[] genes, FuncaoDesempenho funcaoDesempenho) {
		this.cromossomo="";
		this.genes= genes;
		this.funcaoDesempenho=funcaoDesempenho;
		
		for (int i=0;i<genes.length;++i)
		{
			cromossomo+=genes[i].getStringBinaria();
		}
		funcaoDesempenho.calcular(this);
		
		
	}

	public IndividuoPadrao(Gene[] genesDoPai, String novoCromossomo,FuncaoDesempenho funcaoDesempenho) throws GeneException
	{
		this.cromossomo= novoCromossomo;
		this.genes= new Gene[genesDoPai.length];
		this.funcaoDesempenho=funcaoDesempenho;
		int posicaoInicial=0;
		
		// "Fatia" o novoCromossomo em novos genes, de acordo com a estrutura
		// dos genes do pai
		for (int locus=0; locus<genesDoPai.length;++locus)
		{
			String geneBinario=novoCromossomo.substring(posicaoInicial,posicaoInicial+genesDoPai[locus].getAlelo().getTamanhoMaximoGene());
			posicaoInicial+=genesDoPai[locus].getAlelo().getTamanhoMaximoGene();
			genes[locus]=new Gene(geneBinario,genesDoPai[locus].getAlelo());
		}
		funcaoDesempenho.calcular(this);
	}
	
	
	
	public Individuo cruzar(Individuo par)
	{
		int tamanho=cromossomo.length();
		int pontoCorte = (int)(Math.random()*tamanho);
		char[] novoCromossomo=new char[tamanho];
		
		for (int i=0;i<tamanho;++i )
		{
			if (i>=pontoCorte) {
				novoCromossomo[i]= cromossomo.charAt(i);
			}
			else {
				novoCromossomo[i]= par.getCromossomo().charAt(i);
			}
			
		}

		// Cria uma nova funcao de desempenho, do mesmo tipo da classe do pai.
		FuncaoDesempenho novaFuncao=null;
		try {
			novaFuncao= funcaoDesempenho.getClass().newInstance();
		} catch (Exception e) {
			
			// TODO Melhorar este tratamento de erro
			e.printStackTrace();
		} 
		return new IndividuoPadrao(this.genes,new String(novoCromossomo),novaFuncao);
	}
	public Individuo mutar() 
	{
		int tamanho=cromossomo.length();
		int pontoCorte = (int)(Math.random()*tamanho);
		char[] novoCromossomo=new char[tamanho];
		
		for (int i=0;i<tamanho;++i )
		{
			if (i==pontoCorte) {
				novoCromossomo[i]= (cromossomo.charAt(i)=='0')?'1':'0';
			}
			else {
				novoCromossomo[i]= cromossomo.charAt(i);
			}
			
		}

		
		// Cria uma nova funcao de desempenho, do mesmo tipo da classe do pai.
		FuncaoDesempenho novaFuncao=null;
		try {
			novaFuncao= funcaoDesempenho.getClass().newInstance();
		} catch (Exception e) {
			// TODO Melhorar esse tratamento de erro
			e.printStackTrace();
		} 
		return new IndividuoPadrao(this.genes,new String(novoCromossomo),novaFuncao);
	}
	
	
	public String toString()
	{
		final int NUMERO_MAXIMO_CASAS_DECIMAIS=7;
		String strNumeroGeracao= String.valueOf(numeroGeracao);
		char[] zeros= new char[NUMERO_MAXIMO_CASAS_DECIMAIS-strNumeroGeracao.length()];
		Arrays.fill(zeros, ' ');
		
		String saida= new String(zeros) + numeroGeracao + "  " +  funcaoDesempenho.getDesempenho();
		return saida;
	}
	public boolean equals(Object o)
	{
		if (!(o instanceof Individuo) )
		{
			return false;
		}
		return cromossomo.equals(((Individuo)o).getCromossomo());
			
	}
	/** Este método é o ponto crucial da avaliação de fitness, 
	 neste ponto é definido se um individuo é melhor, pior ou igual ao outro.
	 
	 @param o Individuo que será comparado com a instancia atual
	 @return Se o individuo e menor,igual ou maior que o outro.
	 */
	public int compareTo(Object o)
	{
		if (!(o instanceof Individuo))
		{
			return -1;
		}
		
		FuncaoDesempenho fitness= ((Individuo)o).getFuncaoDesempenho();
		short sinal= fitness.getOrdem();
		double desempenho = fitness.getDesempenho();
		
		return  sinal * ((new Double( this.getFuncaoDesempenho().getDesempenho()).compareTo(desempenho)));
	}

	// Getters e setters
	public String getCromossomo() {
		return cromossomo;
	}
	
	
	public int getNumeroGeracao() {
		return numeroGeracao;
	}
	
	public double getDesempenhoAcumulado() {
		return desempenhoAcumulado;
	}
	public void setDesempenhoAcumulado(double desempenhoAcumulado) {
		this.desempenhoAcumulado = desempenhoAcumulado;
	}

	public Gene[] getGenes() {
		return genes;
	}
	
	public FuncaoDesempenho getFuncaoDesempenho() {
		return funcaoDesempenho;
	}

	public void setFuncaoDesempenho(FuncaoDesempenho funcaoDesempenho) {
		this.funcaoDesempenho = funcaoDesempenho;
	}

	public void setNumeroGeracao(int numeroGeracao) {
		this.numeroGeracao=numeroGeracao;

	}

	

	
}
