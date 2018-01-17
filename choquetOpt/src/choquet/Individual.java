package choquet;

import java.text.NumberFormat;
import java.util.Arrays;

import br.ufrj.coppe.pec.ga.FuncaoDesempenho;
import br.ufrj.coppe.pec.ga.Gene;
import br.ufrj.coppe.pec.ga.GeneException;
import br.ufrj.coppe.pec.ga.Individuo;

public class Individual implements Individuo, Runnable {

	private static final long serialVersionUID = 347367168114133434L;

	protected Parameter[] genes;
	protected int numeroGeracao;
	private double desempenhoAcumulado;
	protected FuncaoDesempenho funcaoDesempenho;
	private boolean expert;
	private boolean nearFromExpert;
	private static NumberFormat nf;

	static {

		nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(5);

	}

	public boolean isExpert() {
		return expert;
	}

	public void setExpert(boolean elite) {
		this.expert = elite;
	}

	protected Engine ecosystem;
	// According Beatriz lecture notes pp 15
	// pm = Math.max(1/((double)populationSize),
	// 1/((double)chromossomeSize));

	private static final double PM = 0.35;

	private double penalty;
	private double expertPenalty;

	public double getExpertPenalty() {
		return expertPenalty;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Individual other = (Individual) obj;

		for (int i = 0; i < genes.length; ++i) {
			Parameter p = genes[i];
			Parameter o = other.genes[i];

			if (p.getValor() != o.getValor())

			{

				return false;
			}
		}

		return true;
	}

	public Individual(Parameter[] genes, FuncaoDesempenho funcaoDesempenho, Engine ecosystem) {

		this.genes = genes;

		this.ecosystem = ecosystem;
		this.funcaoDesempenho = funcaoDesempenho;
		this.calculatePenalty();
		// this.isNearFromExpert();
		;
	}

	// Paralelize the calculation
	public void run() {
		// Internally this is done once
		funcaoDesempenho.calcular(this);
	}

	public Individuo cruzar(Individuo par) {
		throw new IllegalArgumentException("Somente aceito um RangeIndividual");
	}

	public Individuo cruzarUniforme(Individual par) {
		final int tamanho = this.genes.length;

		FuncaoDesempenho novaFuncao = null;
		Parameter newGenes[] = cruzarUniforme(this.genes, par.genes);

		try {
			novaFuncao = funcaoDesempenho.getClass().newInstance();
		} catch (Exception e) {

			// TODO Melhorar este tratamento de erro
			e.printStackTrace();
		}

		return new Individual(newGenes, novaFuncao, ecosystem);

	}

	private Parameter[] cruzarUniforme(Parameter[] genes1, Parameter[] genes2) {
		final int tamanho = genes1.length;
		Parameter newGenes[] = new Parameter[tamanho];
		for (int i = 0; i < tamanho; i++) {
			if (i % 2 == 0) {
				newGenes[i] = genes1[i];
			} else {
				newGenes[i] = genes2[i];
			}
		}
		return newGenes;
	}

	public Individuo[] cruzarMultiplo(Individual par) {

		Parameter newGenes[][] = cruzarMultiplo(this.genes, par.genes);
		int size = newGenes.length;
		Individuo[] individuos = new Individuo[size];

		for (int i = 0; i < size; ++i) {

			FuncaoDesempenho novaFuncao = null;

			if (newGenes[i] == null) {
				continue;
			}
			try {
				novaFuncao = funcaoDesempenho.getClass().newInstance();

			} catch (Exception e) {

				throw new RuntimeException(e);
			}

			individuos[i] = new Individual(newGenes[i], novaFuncao, ecosystem);
			((Individual) individuos[i]).isNearFromExpert();
		}
		return individuos;

	}

	private Parameter[][] cruzarMultiplo(Parameter[] genes1, Parameter[] genes2) {

		Parameter newGenes[][] = new Parameter[2][genes2.length];

		int ranges = 1;
		int attrCount = Engine.NUMBER_OF_PARAMETERS;
		for (int range = 0; range < ranges; range++) {
			// avoid the class index
			int cutPoint = 0;
			int cutPoint2 = 0;
			while (cutPoint == cutPoint2) {
				// ASSUMES THE CLASS INDEX IS THE LAST ATTRIBUTE
				cutPoint = (int) (Math.random() * attrCount - 1);
				cutPoint2 = (int) (Math.random() * attrCount - 1);

			}
			if (cutPoint2 < cutPoint) {
				int temp = cutPoint;
				cutPoint = cutPoint2;
				cutPoint2 = temp;
			}
			for (int i = 0; i < attrCount; i++) {

				int pointer = range * attrCount + i;

				if (i <= cutPoint || i >= cutPoint2) {
					newGenes[0][pointer] = genes1[pointer];
					newGenes[1][pointer] = genes2[pointer];

				} else {
					newGenes[0][pointer] = genes2[pointer];
					newGenes[1][pointer] = genes1[pointer];
				}
			}

			if (this.ecosystem.isDebug()) {
				System.out.println("crossover: " + cutPoint + "/" + cutPoint2 + "/" + range);
				System.out.print("genes1:");
				print(genes1);
				System.out.print("genes2:");
				print(genes2);

				for (int i = 0; i < newGenes.length; i++) {
					System.out.print("newGenes" + i + ":");
					print(newGenes[i]);
				}
				System.out.println("------------");
			}
		}

		return newGenes;
	}

	public void print(Parameter[] r) {
		if (r == null)
			return;

		for (int i = 0; i < r.length; i++) {
			System.out.print(r[i] + " ");

		}
		System.out.println();
	}

	public Individuo mutar() {

		Parameter newGenes[] = mutar(this.genes);

		// Cria uma nova funcao de desempenho, do mesmo tipo da classe do pai.
		FuncaoDesempenho novaFuncao = null;
		try {
			// TODO Put this approach to create new instances in all places
			novaFuncao = funcaoDesempenho.getClass().newInstance();
		} catch (Exception e) {
			// TODO Melhorar esse tratamento de erro
			e.printStackTrace();
		}
		this.setNumeroGeracao(ecosystem.getGeracaoAtual());
		Individual ind = new Individual(newGenes, novaFuncao, ecosystem);
		ind.isNearFromExpert();
		return ind;
	}

	public Parameter[] mutar(Parameter[] genes) {

		int chromossomeSize = this.genes.length;
		int populationSize = this.ecosystem.getPopulacao().size();

		Parameter newGenes[] = new Parameter[chromossomeSize];

		int totalCutPoint = (int) (PM * chromossomeSize);

		// Obtem todos os pontos de corte
		int[] pontosCortes = new int[totalCutPoint];
		for (int i = 0; i < pontosCortes.length; ++i) {
			pontosCortes[i] = (int) (Math.random() * chromossomeSize);
			// System.out.println("ponto de corte:" + pontosCortes[i]);

		}
		// Ordena
		Arrays.sort(pontosCortes);

		int j = 0;
		int proximoPontoCorte = pontosCortes[j];

		int stripe = 0;
		int numAttributes = Engine.NUMBER_OF_PARAMETERS;
		// try {
		for (int i = 0; i < chromossomeSize; ++i) {
			if (i != 0 && i % numAttributes == 0) {
				stripe++;
			}

			if (i == proximoPontoCorte) {
				try {
					newGenes[i] = ecosystem.generateGene(i - (numAttributes * stripe));
				} catch (GeneException e) {
					System.out.println(e.getMessage());
					i--;
					continue;
				}
				if (++j < pontosCortes.length) {
					proximoPontoCorte = pontosCortes[j];
				}
			} else {
				newGenes[i] = this.genes[i];
			}

		}
		return newGenes;
	}

	// Este m�todo � o ponto crucial da avalia��o de fitness, neste
	// ponto �
	// definido se um individuo � melhor, pior ou igual ao outro.
	// 
	// @param o
	// Individuo que ser� comparado com a instancia atual
	// @return Se o individuo e menor,igual ou maior que o outro.
	//
	public int compareTo(Object o) {
		if (!(o instanceof Individuo)) {
			return -1;
		}

		FuncaoDesempenho fitness = ((Individuo) o).getFuncaoDesempenho();
		short sinal = fitness.getOrdem();
		double desempenho = fitness.getDesempenho();

		return sinal * ((new Double(this.getFuncaoDesempenho().getDesempenho()).compareTo(desempenho)));
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
		this.numeroGeracao = numeroGeracao;

	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(nf.format(getFuncaoDesempenho().getDesempenho()));

		sb.append(" [");

		for (int i = 0; i < genes.length; i++) {
			Parameter g = genes[i];
			sb.append(nf.format(g.getValor()));

			if (i != genes.length - 1) {
				sb.append(" ");
			}

		}

		sb.append("] ");

		sb.append(isNearFromExpert() ? "*" : " ");
		sb.append(' ');
		sb.append(nf.format(getPenalty()));
		sb.append(' ');
		sb.append(nf.format(getExpertPenalty()));

		return sb.toString();
	}

	public String getCromossomo() {

		return null;
	}

	public double getPenalty() {
		return penalty;
	}

	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}

	private void calculatePenalty() {

		double a1_a3 = genes[4].getValor();
		double a1_a2 = genes[2].getValor();
		double a2_a3 = genes[5].getValor();
		double u = 1;

		penalty = 0;
		for (int i = 0; i < genes.length; ++i) {
			int locus = i + 1;
			double value = genes[i].getValor();
			// u(a1) <= u(a1,a2) <= u(a1,a3) <= u(n)
			if (locus == 1) {
				if (!(value <= a1_a2)) {
					penalty += (value - a1_a2); // no experimento publicado,
												// estava assim: a1_a2-value
				}

				else if (!(value <= a1_a3)) {
					penalty += (value - a1_a3); // no ultimo exp. publicado,
												// estava assim: a1_a3-value
				}
			}
			// u(a1) <= u(a1,a2) <= u(a2,a3) <= u(n)
			else if (locus == 2) {

				if (!(value <= a1_a2)) {
					penalty += (value - a1_a2); // idem
				}

				else if (!(value <= a2_a3)) {
					penalty += (value - a2_a3); // idem
				}

			} else if (locus == 4) {

				if (!(value <= a1_a3)) {
					penalty += (value - a1_a3); // idem
				}

				else if (!(value <= a2_a3)) {
					penalty += (value - a2_a3); // idem
				}

			}

		}

		calculateExpertPenalty();
	}

	private void calculateExpertPenalty() {
		expertPenalty = 0;
		if (ecosystem.getNeighborhoodRelaxationLimit() == 0  || ecosystem.getExpertParameters()==null) {
			return;
		}

		
//		if (!ecosystem.isUseEuclideanDistance()) {
			for (int i=0;i<genes.length;++i) {
				if (i == 0 || i == genes.length) {
					continue;
				}
				Parameter p =  genes[i];
				double value = ecosystem.getExpertParameters().getGenes()[i].getValor();
				double newValue = getGenes()[i].getValor();

				if (newValue < value - ecosystem.getExpertNeighborhood()) {
					
					double penalty1 =(value - ecosystem.getExpertNeighborhood()) - newValue;
					expertPenalty += (penalty1*penalty1);

				} else if (newValue > value + ecosystem.getExpertNeighborhood()) {
					
					double penalty1 =(newValue - (value + ecosystem.getExpertNeighborhood()));
					
					expertPenalty += (penalty1*penalty1);
				}
				++i;
			}


		if (expertPenalty != 0) {
			setNearFromExpert(false);
		} else {
			setNearFromExpert(true);
		}

	}

	public double calculateDistance(double x1, double y1, double x , double y)
	{
		
		return 0;
	}
	public boolean isNearFromExpert() {
		/*
		 * if (!nearFromExpert) { Engine.checkIsNearFromExpert(this); }
		 */
		return nearFromExpert;
	}

	public void setNearFromExpert(boolean nearFromExpert) {
		this.nearFromExpert = nearFromExpert;
	}

}
