package choquet;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import br.ufrj.coppe.pec.ga.Ecosistema;
import br.ufrj.coppe.pec.ga.FuncaoDesempenho;
import br.ufrj.coppe.pec.ga.GeneException;
import br.ufrj.coppe.pec.ga.Individuo;

public class Engine extends Ecosistema {

	//Parameters
	private Individual lastBestIndividual;
	private int precision=6;
	private double precisionOperator ;
	private boolean debug;
	private static int region;
	private static Individual expertParameters;
	private double expertNeighborhood = 0.1;
	private double neighborhoodRelaxationLimit=0.03;
	private boolean discardPenalty;
	private long experimentId;
	private boolean useEuclideanDistance=true;
	private DateFormat df = DateFormat.getDateTimeInstance();

	
	public boolean isUseEuclideanDistance() {
		return useEuclideanDistance;
	}

	public void setUseEuclideanDistance(boolean useEuclideanDistance) {
		this.useEuclideanDistance = useEuclideanDistance;
	}

	/**
	 * Returns an integer number with the experiment id.
	 * @return
	 */
	public long getExperimentId() {
		return experimentId;
	}


	//
	private NumberFormat nf;
	public  static int NUMBER_OF_PARAMETERS = 30 ;///6;
	
	
	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;

		nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(precision);
		precisionOperator= Math.pow(10, precision);
	}

	
	public double getExpertNeighborhood() {
		return expertNeighborhood;
	}

	public void setExpertNeighborhood(double expertNeighborhood) {
		this.expertNeighborhood = expertNeighborhood;
	}


	
	public double getNeighborhoodRelaxationLimit() {
		return neighborhoodRelaxationLimit;
	}

	public void setNeighborhoodRelaxationLimit(double neighborhoodFlexibility) {
		this.neighborhoodRelaxationLimit = neighborhoodFlexibility;
	}

	public Individual getExpertParameters() {
		return expertParameters;
	}

	public void setExpertParameters(Parameter[] genes) {

		Individual individuo = new Individual(genes, new Fitness(), this);
		individuo.setExpert(true);
		individuo.setNearFromExpert(true);
		this.expertParameters = individuo;
	}

	public static int getRegion() {
		return region;
	}

	public static void setRegion(int region2) {
		region = region2;
	}

	// private List<Individuo> kinderGarten = new ArrayList();


	enum TipoCruzamento {
		UNIFORM, MULTIPLE
	};

	public Engine() {
		nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(precision);
		
		precisionOperator= Math.pow(10, precision);
		

	}

	public void iniciarPopulacao() {
		geracaoAtual = 1;

		if (expertParameters != null) {
			
			expertParameters.funcaoDesempenho.calcular(expertParameters);
			populacao.add(expertParameters);

		}
		// System.exit(0);
		for (int i = 1; i < populacaoInicial; ++i) {
			Individuo individuo=generateRandomIndividual();
			
			if (individuo!=null && ((Individual)individuo).getPenalty()!=0)
			{
				System.out.println("Illegal state exception, individual with penalty" );
			}
			if (individuo!=null && !populacao.contains(individuo)) {
				populacao.add(individuo);
			}

		}

	}

	
	public Individual generateRandomIndividual() {
		Individual individuo =null;
		final int RETRY_LIMIT=600; //to avoid forever loop
		int i=0;
		while (true) {
			Parameter[] genes = new Parameter[NUMBER_OF_PARAMETERS];
			for (int j = 0; j < NUMBER_OF_PARAMETERS; j++) {

				genes[j] = generateGene(j);
			}

			individuo = new Individual(genes, new Fitness(), this);
			individuo.setNearFromExpert(true);
			//individuo.calculatePenalty();
			i++;
			if (i>RETRY_LIMIT)
			{
				System.out.println("No individual generated due to contraint violation");
				individuo=null;
				break;
			}
			if (discardPenalty && individuo.getPenalty() != 0) {
				// Generate again
				continue;
			}
			break;
		}
		return individuo;

	}

	private void printParameters(){
		
		

		System.out.println(getParametersString());
		
	}
	
	public String getExperimentIdentifier()
	{
		String ids=experimentId + "_R" +  region + "_N" + expertNeighborhood + "_NR" + neighborhoodRelaxationLimit + "_G" + getMaximoGeracoes()+ "_PM"+ getPopulacaoMaxima()+"_PI"+getPopulacaoInicial();
		
		return ids;
		
	}
	
	public void run() {
		geracaoAtual = 1;
		tempoInicio = System.currentTimeMillis();
		experimentId=Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(2));
		reset();
		printParameters();
		notificarInicioEcosistemaListeners();
		
		// Notifica os listeners de inicio de ecosistema
		long start = System.currentTimeMillis();
		iniciarPopulacao();
		
		
		
		calculateFitness(); // paralelizeCalculations();
	
		//
		calcularEstatisticas();

		// � necess�rio ordenar por desempenho para poder rankear
		Collections.sort(populacao);
		notificarFimInicializacaoListeners();
		//printPopulation(populacao);
		printResults();

		while (!parar()) {

			runKernel();

		
			printResults();

			notificarNovaGeracaoListeners();
			geracaoAtual++;
		}
		notificarFimEcosistemaListeners();
		printResults();

	}


	public void calcularEstatisticas() {

		// O primeiro da populacao sempre ser o com melhor fitness
		Individuo primeiroIndividuo = populacao.get(0);
		Individuo ultimoIndividuo = populacao.get(populacao.size() - 1);


		if (expertParameters!=null && ((Individual)ultimoIndividuo).isExpert())
		{
			ultimoIndividuo = populacao.get(populacao.size()-2);
			
		}

		
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
				System.out.println("Aten��o fitness nulo");
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
		// TODO Verificar o que � o correto aqui
		else if (ordem == FuncaoDesempenho.ORDEM_DECRESCENTE)
		{
			
			FuncaoDesempenho fitness = melhorIndividuo.getFuncaoDesempenho();
			//if (fitness.getDesempenho() < desempenhoMaximoAtual) {
			melhorIndividuo = primeiroIndividuo;
			piorIndividuo = ultimoIndividuo;
			//}
		}
		if (melhorIndividuoAtual != melhorIndividuo) {
			tempoDesempenhoMaximoAtual = System.currentTimeMillis() - tempoInicio;
		}

	}

	private void printPopulation(List<Individuo> pop) {

		
		System.out.println(getPopulationString(pop));

	}
	
	public String getPopulationString(List<Individuo> pop)
	{
		StringBuilder sb =new StringBuilder();
		
		for (Individuo ind : pop) {
			
			sb.append(ind.toString());
			
			sb.append('\n');
			
		}
		
		sb.append('\n');
		
		return sb.toString();
		
	}
	private void runKernel() {
		// List<Individuo> previous = populacao;

		// populacao = individuos;
		// ------------------------------
		List<Individuo> newBorn = cruzar(selecionarParaCruzamento());

		List<Individuo> mutations = mutar(selecionarParaMutacao());

		List<Individuo> elite = null;
		if (elitismPercent > 0) {
			elite = selectElite();

			populacao.clear();
		}

		if (elitismPercent == -1) {
			elite = new ArrayList();
			elite.addAll(populacao);
			populacao.clear();
		}
		addAll(newBorn);
		addAll(mutations);

		calculateFitness();

		// Necessario ordenar por desempenho para poder rankear
		Collections.sort(populacao);

		List<Individuo> doomed = selecionarParaRemocao();
		// Rescue the expert GENE
		if (expertParameters != null) {
			doomed.remove(expertParameters);
		}
		remover(doomed);

		if (elitismPercent > 0 || elitismPercent == -1) {
			addAll(elite);
		}

		// printPop(populacao);
		calcularEstatisticas();
		// populacao = previous;

	}

	/** Elitism implementation */

	public List<Individuo> selectElite() {
		List<Individuo> elite = new ArrayList<Individuo>();

		double e = elitismPercent;
		int countOfElite = (int) (populacao.size() * e / 100);

		for (int i = 0; i < countOfElite; i++) {
			Individual el = (Individual) populacao.get(i);
			elite.add(populacao.get(i));

		}

		return elite;
	}

	public synchronized void addAll(List<Individuo> toAdd) {

		Iterator<Individuo> it = toAdd.iterator();

		while (it.hasNext()) {
			Individuo individuo = it.next();
			if (!populacao.contains(individuo)) {
				
				//check if we shall discard the penalties
				if (this.discardPenalty)
				{
					if (((Individual)individuo).getPenalty()!=0)
					{
						continue;
					}
				}
				//check if is possible to trepass the expert threshold.
				/*if (neighborhoodRelaxationLimit==0)
				{
					this.populacao.add(individuo);
				}
				else {
					if (((Individual)individuo).getExpertPenalty()==0)
					{
						this.populacao.add(individuo);
					}
				}
				*/
				this.populacao.add(individuo);
				
			}
			// alocateIndividual(individuo, populacao, it);

		}

	}

	public List<Individuo> cruzar(List<Individuo> mattingPool) {

		TipoCruzamento tipo = TipoCruzamento.MULTIPLE;
		List<Individuo> newBorn = new ArrayList<Individuo>();
		List pais = new ArrayList();
		// List<Individuo> recemNascidos = new ArrayList<Individuo>();
		while (mattingPool.size() > 1) {
			int index = (int) (Math.random() * mattingPool.size());
			Individuo pai = mattingPool.get(index);
			mattingPool.remove(index);

			index = (int) (Math.random() * mattingPool.size());
			Individual mae = (Individual) mattingPool.get(index);
			mattingPool.remove(index);

			try {

				if (tipo == TipoCruzamento.UNIFORM) {
					Individuo filho = mae.cruzarUniforme((Individual) pai);
					newBorn.add(filho);
				} else {
					Individuo[] filhos = mae.cruzarMultiplo((Individual) pai);
					for (int i = 0; i < filhos.length; ++i) {
						if (filhos[i] != null) {
							filhos[i].setNumeroGeracao(geracaoAtual);
							newBorn.add(filhos[i]);
						}
					}
				}
			} catch (GeneException e) {
				// Indiv�duo natimorto
				e.printStackTrace();
			}

		}

		return newBorn;

	}

	private int countNotFeasible() {
		int count = 0;

		for (Individuo ind : populacao) {
			if (((Individual) ind).getPenalty() != 0) {
				count++;
			}

		}

		return count;
	}

	
	public void printResults() {

		System.out.println(toString());
	}

	public String getParametersString()
	{

		StringBuilder sb= new StringBuilder();
		sb.append("#" + experimentId);
		sb.append('\n');
		sb.append(df.format(new java.util.Date()));
		sb.append('\n');
		sb.append("AG parameters\n");
		sb.append("Region:           \t"  + region);
		sb.append('\n');
		sb.append("Expert genes:\t[");
		for (Parameter p:expertParameters.genes)
		{
			sb.append(nf.format(p.getValor()));
			sb.append(' ');
		}
		sb.append("]\n");
		//System.out.println(Arrays.toString(expertParameters));
		sb.append("Initial population:\t" + populacaoInicial);
		sb.append('\n');
		sb.append("Max population:   \t" + populacaoMaxima);
		sb.append('\n');
		sb.append("Crossover percent:\t" + percentualCruzamento);
		sb.append('\n');
		sb.append("Mutation percent:\t" + percentualMutacao);
		sb.append('\n');
		sb.append("Number of generations:\t" +maximoGeracoes);
		sb.append('\n');
		sb.append("Discard penalty:\t" + discardPenalty);
		sb.append('\n');
		sb.append("Expert neighborhood:\t" +expertNeighborhood);
	

		sb.append('\n');
		sb.append("Neighborhood relaxation limit:\t" +neighborhoodRelaxationLimit);


		sb.append('\n');
		sb.append("Precision:             \t" +precision);

		
		
		sb.append('\n');
		sb.append('\n');
		return sb.toString();
	}
	public String toString()
	{
		
		Individuo best = getMelhorIndividuo();
		Individuo worst = getPiorIndividuo();
		best = getMelhorIndividuo();
		worst = getPiorIndividuo();



		if (lastBestIndividual != best) {
			lastBestIndividual = (Individual) best;

		}

		StringBuilder sb= new StringBuilder();
		
		int notFeas = countNotFeasible();
		String feas = "[feas:" + (populacao.size() - notFeas) + ",not:"
				+ notFeas + "] avg fitness:" + nf.format(averageFitness);

		
		sb.append("g:" + (geracaoAtual) + " best ("
				+ best.getNumeroGeracao() + "):"
				+ nf.format(best.getFuncaoDesempenho().getDesempenho()) + " "
				+ " " + feas);
		
		if (worst != null && worst.getFuncaoDesempenho() != null) {
			sb.append(" worst (" + worst.getNumeroGeracao() + "):"
					+ nf.format(worst.getFuncaoDesempenho().getDesempenho())
					+ " ");
		} 
		sb.append("\n");
		for (int i = 0; i < 10; ++i) {

			if (i >= populacao.size()) {
				break;
			}
			Individual current = (Individual) populacao.get(i);

			sb.append( i );
			sb.append(' ');
			sb.append( current.toString());
		
			sb.append("\n");
		}

		if (expertParameters!=null) {
			sb.append("expert:"	+ expertParameters.toString());
		}
		
		sb.append('\n');

		return sb.toString();
		
	}
	public void calculateFitness() {

		for (Individuo individual : populacao) {
			Individual current = (Individual) individual;
			current.getFuncaoDesempenho().calcular(individual);
			if (debug) {
				System.out.println(">> "
						+ nf.format(current.getFuncaoDesempenho()
								.getDesempenho()) + "[" + current.toString()
						+ "]");

			}
		}

	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public Parameter generateGene(int j) {

		Parameter p;
		// Surround the gene
		if (expertParameters != null) {

			double finalLimit = expertNeighborhood+neighborhoodRelaxationLimit;

			double value = expertParameters.getGenes()[j].getValor();
			double newValue = (Math.random() * (finalLimit * 2))
					+ value - finalLimit;
			
			newValue = (newValue*precisionOperator)/precisionOperator;
			
			
			if (newValue < 0) {
				newValue = 0;
			}
			if (newValue > 1) {
				newValue = 1;
			}

			if (newValue > (value+finalLimit) || newValue< (value-finalLimit)) {
				throw new IllegalStateException();
			}
			p = new Parameter(newValue);

		} else {

			// Get a simple random number
			double x = Math.random();
			x = ((int) (x * precisionOperator)) / precisionOperator;
			p = new Parameter(x);
		}
		return p;

	}

	public boolean isDiscardPenalty() {
		return discardPenalty;
	}

	public void setDiscardPenalty(boolean usePenalty) {
		this.discardPenalty = usePenalty;
	}


	public float getMaxValue(int alel) {
	
		if (expertParameters==null)
		{
			return 1;
		}
		
		double value= expertParameters.getGenes()[alel].getValor();
		float max = (float) (value +  expertNeighborhood + neighborhoodRelaxationLimit);
		if (max > 1) {
			max = 1;

		}
		return max;

	}


	public float getMinValue(int alel) {
	
		if (expertParameters==null)
		{
			return 1;
		}
		
		double value= expertParameters.getGenes()[alel].getValor();
		float min = (float) (value - (  expertNeighborhood + neighborhoodRelaxationLimit));
		if (min <0) {
			min = 1;

		}
		return min;

	}

	
}
