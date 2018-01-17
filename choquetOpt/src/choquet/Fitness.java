package choquet;

import br.ufrj.coppe.pec.ga.FuncaoDesempenho;
import br.ufrj.coppe.pec.ga.Gene;
import br.ufrj.coppe.pec.ga.Individuo;

public class Fitness implements FuncaoDesempenho {

	private boolean calculado = false;
	private double desempenho = 0;
	private double expertPenalty =-1;
	
	public double calcular(Individuo individuo) {

		expertPenalty = ((Individual)individuo).getExpertPenalty();
		
		if (!calculado) {
			
			
			desempenho=0;
		
			//((Individual)individuo).calculatePenalty();
			
			double penalty= ((Individual)individuo).getPenalty();
			//System.out.println( ">"+Math.floor(penalty));
					
			if (((Individual)individuo).getPenalty()==0) {
				
				//Get the values to parametrize
				double d[]=new double[individuo.getGenes().length];
				for (int i=0;i<individuo.getGenes().length;++i)
				{
					d[i]= individuo.getGenes()[i].getValor();
					
				}
				//Call Lionel´s routine
				double fit= ChoquetRunner.runChoquet((Individual)individuo,d);
				desempenho += fit;
				//System.out.println("##lionel routine:" + fit);
			}
			else
			{
				desempenho =(-Math.pow(((Individual)individuo).getPenalty(),2))+0.4;
				//System.out.println("penalty:" + desempenho);
				
			}
			
			calculado=true;
		}
		return desempenho;
	}

	public double getDesempenho() {
		
		if (expertPenalty==-1)
		{throw new IllegalStateException("Illegal state expertPenalty=-1"); }
		
		return desempenho  - expertPenalty;
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
