package br.ufrj.coppe.pec.ga;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import br.ufrj.coppe.pec.util.Constantes;

public abstract class Ecosistema implements Runnable, Serializable {

	protected List<Individuo> populacao = new ArrayList<Individuo>();

	protected Collection<NovaGeracaoListener> geracaoListeners = new LinkedList<NovaGeracaoListener>();

	protected Collection<InicioEcosistemaListener> inicioListeners = new LinkedList<InicioEcosistemaListener>();

	protected Collection<FimEcosistemaListener> fimListeners = new LinkedList<FimEcosistemaListener>();


	protected Collection<FimInicializacaoListener> fimInicializacaoListeners = new LinkedList<FimInicializacaoListener>();

	

	/* Valores padrao */
	protected int populacaoInicial = 100;

	protected int populacaoMaxima = 100;

	protected double percentualMutacao = 5;

	protected double percentualCruzamento = 30;

	protected int geracaoAtual = 0;

	protected int maximoGeracoes = 100;

	protected double desempenhoEsperado = Constantes.NUMERO_NAO_INFORMADO;

	protected double desempenhoMinimoAtual = 0;

	protected double desempenhoMaximoAtual = Constantes.NUMERO_NAO_INFORMADO;

	protected long tempoDesempenhoMaximoAtual;

	protected boolean permitirGemeos = false;

	protected Individuo melhorIndividuo;

	protected Individuo piorIndividuo;

	protected long tempoInicio;

	protected double selectionPressure;
	
	protected double averageFitness;
	
	protected double elitismPercent;
	

	protected NumberFormat numberFormater = NumberFormat.getInstance();

	public Ecosistema() {
		
		numberFormater.setMaximumFractionDigits(4);
		numberFormater.setMinimumFractionDigits(4);
	
		
	}

	
	/**
	 * Fluxo principal do algoritmo.
	 */
	public void run() {

		tempoInicio = System.currentTimeMillis();
		reset();
		// Notifica os listeners de inicio de ecosistema
		notificarInicioEcosistemaListeners();
		iniciarPopulacao();
		notificarFimInicializacaoListeners();
		Collections.sort(populacao);

		while (!parar()) {
			List <Individuo> newBorn= cruzar(selecionarParaCruzamento());
			List <Individuo> mutations= mutar(selecionarParaMutacao());
			if (elitismPercent>0)
			{
				List <Individuo> elite= selectElite();
				populacao.clear();
				addAll(elite);
			}
			addAll(newBorn);
			addAll(mutations);
			
			// É necessário ordenar por desempenho para poder rankear
			Collections.sort(populacao);
			remover(selecionarParaRemocao());

			geracaoAtual++;
			calcularEstatisticas();
			notificarNovaGeracaoListeners();
			System.out.println(this);
		}

		// Notifica os listeners de fim de ecosistema
		notificarFimEcosistemaListeners();

	}
	
	/** Elitism implementation */

	public List <Individuo> selectElite()
	{
		List <Individuo> elite = new ArrayList<Individuo>();

		double e=elitismPercent;
		int countOfElite= (int) (populacao.size() * e/100); 
		
		
		for (int i=0;i<countOfElite;i++)
		{
			elite.add(populacao.get(i));
			//System.out.println("##### elite (" + i+ ")" + populacao.get(i).getFuncaoDesempenho().getDesempenho());
		}

		//System.out.println("populacao:" + populacao.size() + "/elite:" + elite.size() + "/recem nascidos:"+ recemNascidos.size());
	
		return elite;
	}

	public void reset() {
		// Inicializa as propriedades
		desempenhoMinimoAtual = 0;
		desempenhoMaximoAtual = 0;
		populacao = new ArrayList<Individuo>();
		geracaoAtual = 0;
		// melhorIndividuo=null;
	}

	/**
	 * Inicializa a população randomicamente
	 * 
	 */
	public abstract void iniciarPopulacao();

	/**
	 * Seleciona os individuos da população para cruzamento. Utiliza roda da
	 * roleta para selecionar os indivíduos.
	 * 
	 * @return Lista de indivíduos que irão cruzar
	 */
	public List<Individuo> selecionarParaCruzamento() {

		// Calcula o desempenho acumulado
		double somatorioDesempenho = 0;

		// Vai para o ultimo, isto é, o com menor desempenho
		for (int i = populacao.size() - 1; i >= 0; i--) {
			Individuo individuo = populacao.get(i);
			somatorioDesempenho += individuo.getFuncaoDesempenho().getDesempenho();
			individuo.setDesempenhoAcumulado(somatorioDesempenho);
			//System.out.println("###### desempenho acumulado:" + individuo.getDesempenhoAcumulado() + "/" + somatorioDesempenho);

		}

		List<Individuo> populacaoIntermediaria = new LinkedList<Individuo>();
		// Descobre o percentual de cruzantes da populacao
		int totalNamorados = (int) (populacao.size() * (percentualCruzamento / 100));
		double aptidoesSorteadas[] = new double[totalNamorados];

		while (totalNamorados > 0) { // Seleciona os "namorados" por sorteio
			aptidoesSorteadas[totalNamorados - 1] = (Math.random() * somatorioDesempenho);
			totalNamorados--;
		}

		Arrays.sort(aptidoesSorteadas);
		int j = populacao.size() - 1;
		// Busca pelos individuos que tenham a aptidao acumulada maior ou igual
		for (int i = 0; i < aptidoesSorteadas.length; i++) {
			// Vai para o ultimo, isto é, o com menor desempenho
			for (; j >= 0; j--) {
				Individuo individuo = populacao.get(j);
				if (individuo.getDesempenhoAcumulado() >= aptidoesSorteadas[i]) {
					populacaoIntermediaria.add(individuo);
					break;
				}
			}
		}
		// System.out.println("##population size:" +
		// populacao.size()+"dating.size:" + namorados.size());
		return populacaoIntermediaria;
	}

	/**
	 * Seleciona individuos que irão sofrer mutação
	 * 
	 * @return Lista de indivíduos que irão sofrer mutação
	 */
	public List<Individuo> selecionarParaMutacao() {
		List<Individuo> mutantes = new LinkedList<Individuo>();
		// Descrobre o percentual de mutantes da populacao
		int totalMutantes = (int) (populacao.size() * (percentualMutacao / 100));
		while (totalMutantes > 0) { // Seleciona os mutantes por sorteio
			int index = (int) (Math.random() * populacao.size());
			mutantes.add(populacao.get(index));
			totalMutantes--;
		}
		// System.out.println("## population size:" +
		// populacao.size()+"dating.size:" + mutantes.size());
		return mutantes;
	}

	/**
	 * Seleciona os indivíduos que irão morrer, levando em consideração o menor
	 * desempenho.
	 * 
	 * @return Lista de indivíduos selecionados para morte.
	 */
	public List<Individuo> selecionarParaRemocao() {
		List<Individuo> doomed;
		int tamanho = populacao.size();
		// Ordena a populacao pelo desempenho
		List populacaoOrdenadaPorDesempenho = new ArrayList<Individuo>(populacao);
		if (populacaoMaxima < tamanho) {
			doomed = populacaoOrdenadaPorDesempenho.subList(populacaoMaxima, tamanho);
		} else {
			doomed = Collections.EMPTY_LIST;
		}
		// System.out.println("###max.pop:" + populacaoMaxima+ "curr.size:" +
		// tamanho + "doomed.size:" + condenados.size());
		return doomed;
	}

	/**
	 * Efetua operações de cruzamento, buscando aleatoreamente pares para
	 * cruzamento em uma lista de individuos selecionados.
	 * 
	 * @param selecionados
	 *            Lista de indivíduos selecionados para cruzamento.
	 */
	public List<Individuo>  cruzar(List<Individuo> selecionados) {

		List pais = new ArrayList();
		List<Individuo> recemNascidos = new ArrayList<Individuo>();
		while (selecionados.size() > 1) {
			int index = (int) (Math.random() * selecionados.size());
			Individuo pai = selecionados.get(index);
			selecionados.remove(index);
			pais.add(pai);

			index = (int) (Math.random() * selecionados.size());
			Individuo mae = selecionados.get(index);
			selecionados.remove(index);
			pais.add(mae);

			// TODO Melhorar isso aqui!!

			try {
				Individuo filho = mae.cruzar(pai);
				filho.setNumeroGeracao(geracaoAtual);
				recemNascidos.add(filho);
			} catch (GeneException e) {
				// Indivíduo natimorto
				e.printStackTrace();
			}

		}
		// populacao.addAll(recemNascidos);
		return recemNascidos;
	}

	public List <Individuo> mutar(List<Individuo> mutantes) {

		List<Individuo> mutations = new ArrayList<Individuo>();
		Iterator<Individuo> it = mutantes.iterator();
		
		while (it.hasNext()) {
			Individuo individuo = it.next();
			// TODO Melhorar isso!!
			// Ignora o mutante que tenha gerado um individuo invalido
			try {
				Individuo mutacao = individuo.mutar();
				mutacao.setNumeroGeracao(geracaoAtual);
				mutations.add(mutacao);
			} catch (GeneException e) {
				// Mutação ocasionou um natimorto
				e.printStackTrace();
			}

		}
		
		return mutations;
		// addAll(mutacoes);

	}

	/**
	 * Evita que existam individuos gemeos na populacao
	 * 
	 * @param individuos
	 */
	public void addAll(List<Individuo> individuos) {
		// if (this.permitirGemeos) {
		for (int i = 0; i < individuos.size(); ++i) {
			// if (!populacao.contains(individuos.get(i))) {
			populacao.add(individuos.get(i));
			// }
		}
		// }
	}

	public void remover(List<Individuo> condenados) {
		Iterator it = condenados.iterator();
		for (Individuo individuo : condenados) {

			individuo.getFuncaoDesempenho().clean();
		//	individuo.setFuncaoDesempenho(null);
			
		}

		populacao.removeAll(condenados);
		System.gc();
	}

	public boolean parar() {

		boolean parar = (geracaoAtual >= maximoGeracoes);
		if (desempenhoEsperado != Constantes.NUMERO_NAO_INFORMADO && !parar) {
			// TODO Melhorar o tratamento da ordem (Melhor/Pior)
			if (melhorIndividuo != null && (desempenhoMaximoAtual != 0 && desempenhoMaximoAtual != Constantes.NUMERO_NAO_INFORMADO)) {
				parar = desempenhoMaximoAtual <= desempenhoEsperado;
			}

		}
		return parar;

	}

	public String toString() {

		
		StringBuffer saida = new StringBuffer("Generation #");
		saida.append(geracaoAtual + 1);
		saida.append("\n\n");
		saida.append("Selection pressure:");
		saida.append(numberFormater.format(this.getSelectionPressure()));
		saida.append("\n");
		saida.append("Best individual:\n");
		saida.append(melhorIndividuo.getNumeroGeracao());
		saida.append("  ");
		saida.append(numberFormater.format(desempenhoMaximoAtual));
		saida.append(" in ");
		saida.append(tempoDesempenhoMaximoAtual);
		saida.append(" ms\n\n");

		Individuo pior = populacao.get(populacao.size() - 1);
		saida.append("Worst individual:\n");
		saida.append(pior.getNumeroGeracao());
		saida.append("  ");
		saida.append(numberFormater.format(desempenhoMinimoAtual));
		saida.append("\n");

		final int TOTAL_RANKING = 10;
		if (Constantes.DEBUG) {
			saida.append("#   generation  fitness\n");
			saida.append("--  ----------  ----------\n");
			// Imprime os 10 primeiros em ordem decrescente
			for (int i = 0; i < TOTAL_RANKING && i<populacao.size(); i++) {
				Individuo individuo = populacao.get(i);
				if (i + 1 < 10) {
					saida.append(" ");
				}
				saida.append(i + 1);
				saida.append("  ");
				saida.append(individuo);
				saida.append("\n");
			}
			
			
	
			
		} else {
		}
		return saida.toString();

	}

	// /Getters e Setters
	public List<Individuo> getPopulacao() {
		return populacao;
	}

	public void setPopulacao(List<Individuo> population) {
		this.populacao = population;
	}

	public int getPopulacaoInicial() {
		return populacaoInicial;
	}

	public void setPopulacaoInicial(int startPopulation) {
		this.populacaoInicial = startPopulation;
	}

	public double getPercentualMutacao() {
		return percentualMutacao;
	}

	public void setPercentualMutacao(double mutationPercent) {
		this.percentualMutacao = mutationPercent;
	}

	public double getPercentualCruzamento() {
		return percentualCruzamento;
	}

	public void setPercentualCruzamento(double crossOverPercent) {
		this.percentualCruzamento = crossOverPercent;
	}

	public int getGeracaoAtual() {
		return geracaoAtual;
	}

	public void setGeracaoAtual(int currentGeneration) {
		this.geracaoAtual = currentGeneration;
	}

	public int getMaximoGeracoes() {
		return maximoGeracoes;
	}

	public void setMaximoGeracoes(int maxGeneration) {
		this.maximoGeracoes = maxGeneration;
	}

	public double getDesempenhoEsperado() {
		return desempenhoEsperado;
	}

	public void setDesempenhoEsperado(double maximoDesempenho) {
		this.desempenhoEsperado = maximoDesempenho;
	}

	public int getPopulacaoMaxima() {
		return populacaoMaxima;
	}

	public void setPopulacaoMaxima(int populacaoMaxima) {
		this.populacaoMaxima = populacaoMaxima;
	}

	public Individuo getMelhorIndividuo() {
		return melhorIndividuo;
	}

	// Processa os listeners de nova geracao
	public void addListener(NovaGeracaoListener listener) {
		geracaoListeners.add(listener);
	}

	public void removeListener(NovaGeracaoListener listener) {
		geracaoListeners.remove(listener);
	}

	public void removeListener(FimInicializacaoListener listener) {
		fimInicializacaoListeners.remove(listener);
	}
	public void addListener(InicioEcosistemaListener listener) {
		inicioListeners.add(listener);
	}

	public void removeListener(InicioEcosistemaListener listener) {
		inicioListeners.remove(listener);
	}

	public void addListener(FimInicializacaoListener listener) {
		fimInicializacaoListeners.add(listener);
	}

	public void addListener(FimEcosistemaListener listener) {
		fimListeners.add(listener);
	}

	public void removeListener(FimEcosistemaListener listener) {
		fimListeners.remove(listener);
	}

	protected void notificarNovaGeracaoListeners() {
		Iterator<NovaGeracaoListener> it = geracaoListeners.iterator();
		while (it.hasNext()) {

			it.next().novaGeracaoCriada(geracaoAtual);
		}

	}

	protected void notificarInicioEcosistemaListeners() {
		Iterator<InicioEcosistemaListener> it = inicioListeners.iterator();
		while (it.hasNext()) {

			it.next().ecosistemaIniciado();
		}

	}

	protected void notificarFimInicializacaoListeners() {
		Iterator<FimInicializacaoListener> it = fimInicializacaoListeners.iterator();
		while (it.hasNext()) {

			it.next().ecosystemaInicializado();
		}

	}

	protected void notificarFimEcosistemaListeners() {
		Iterator<FimEcosistemaListener> it = fimListeners.iterator();
		while (it.hasNext()) {

			it.next().ecosistemaFinalizado();
		}

	}

	/**
	 * Seleciona o melhor desempenho de todas as geracoes. Conta que a populacao
	 * esteja ordenada por desempenho, sendo o primeiro o melhor. Seleciona o
	 * pior e melhor desempenho da geração atual.
	 */

	public void calcularEstatisticas() {

		// O primeiro da populacao sempre ser o com melhor fitness
		Individuo primeiroIndividuo = populacao.get(0);
		Individuo ultimoIndividuo = populacao.get(populacao.size() - 1);
		short ordem = primeiroIndividuo.getFuncaoDesempenho().getOrdem();
		desempenhoMaximoAtual = primeiroIndividuo.getFuncaoDesempenho().getDesempenho();
		desempenhoMinimoAtual = ultimoIndividuo.getFuncaoDesempenho().getDesempenho();

		//Selection pressure calculation
		averageFitness=0;
		double sumFitness=0;
		Iterator <Individuo>it= populacao.iterator();
		while (it.hasNext())
		{	Individuo individuo = it.next();
			FuncaoDesempenho fitness = individuo.getFuncaoDesempenho();
			if (fitness!=null)
			{
				sumFitness+= fitness.getDesempenho();
			}
			else
			{
				System.out.println("Atenção fitness nulo");
			}
			
			
		}
		averageFitness= sumFitness/populacao.size();
		selectionPressure= desempenhoMaximoAtual/averageFitness;
		//-----------------------------
		
		
		
		
		
		Individuo melhorIndividuoAtual = melhorIndividuo;

		if (melhorIndividuo!=null) 
		{

			melhorIndividuo.getFuncaoDesempenho();
			if (melhorIndividuo==null)
			{
				System.out.println("erro funcao do melhor individuo estah nula");
			}
			piorIndividuo.getFuncaoDesempenho();
			if (piorIndividuo==null)
			{
				System.out.println("erro funcao do pior individuo estah nula");
			}

		}
		// Seleciona o topo da lista
		if (melhorIndividuo == null) {
			melhorIndividuo = primeiroIndividuo;
			piorIndividuo = ultimoIndividuo;
			
		} 
		
		if (ordem == FuncaoDesempenho.ORDEM_CRESCENTE
				&& melhorIndividuo.getFuncaoDesempenho().getDesempenho() > desempenhoMaximoAtual) {
			melhorIndividuo = primeiroIndividuo;
			piorIndividuo = ultimoIndividuo;

		}
		// TODO Verificar o que é o correto aqui
		else if (ordem == FuncaoDesempenho.ORDEM_DECRESCENTE)
		{
			FuncaoDesempenho fitness = melhorIndividuo.getFuncaoDesempenho();
			if (fitness.getDesempenho() < desempenhoMaximoAtual) {
			melhorIndividuo = primeiroIndividuo;
			piorIndividuo = ultimoIndividuo;
			}
		}
		if (melhorIndividuoAtual != melhorIndividuo) {
			tempoDesempenhoMaximoAtual = System.currentTimeMillis() - tempoInicio;
		}

	}

	public Individuo getPiorIndividuo() {
		return piorIndividuo;
	}

	public double getDesempenhoMaximoAtual() {
		return desempenhoMaximoAtual;
	}

	public double getDesempenhoMinimoAtual() {
		return desempenhoMinimoAtual;
	}

	public void setMelhorIndividuo(Individuo melhorIndividuo) {
		this.melhorIndividuo = melhorIndividuo;
	}

	public boolean isPermitirGemeos() {
		return permitirGemeos;
	}

	public void setPermitirGemeos(boolean permitirGemeos) {
		this.permitirGemeos = permitirGemeos;
	}

	public long getTempoDesempenhoMaximoAtual() {
		return tempoDesempenhoMaximoAtual;
	}

	public double getSelectionPressure() {
		return selectionPressure;
	}

	public void setSelectionPressure(double selectionPressure) {
		this.selectionPressure = selectionPressure;
	}

	public double getAverageFitness() {
		return averageFitness;
	}

	public void setAverageFitness(double averageFitness) {
		this.averageFitness = averageFitness;
	}

	public double getElitismPercent() {
		return elitismPercent;
	}

	public void setElitismPercent(double percentOfElitism) {
		this.elitismPercent = percentOfElitism;
	}

}
