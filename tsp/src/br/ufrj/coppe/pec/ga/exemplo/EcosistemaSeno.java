package br.ufrj.coppe.pec.ga.exemplo;

import br.ufrj.coppe.pec.ga.*;


public class EcosistemaSeno extends Ecosistema {

	public void iniciarPopulacao()
	{
		geracaoAtual=1;
		Alelo alelo=new Alelo();
		for (int i=1;i<=populacaoInicial;++i)
		{
			//Sorteia um numero
			int x= (int)((Math.random() * Short.MAX_VALUE)+1);
			Gene gene=new Gene(x,alelo);
			
			x= (int)((Math.random() * Short.MAX_VALUE)+1);
			Gene gene2=new Gene(x, alelo);
			
			Individuo individuo = new IndividuoPadrao(new Gene[] {gene, gene2}, new FuncaoDesempenhoSeno());
			
			//Impede que existam individuos gemeos, para evitar a convergencia.
			if (populacao.contains(individuo))
			{
				i--;
				continue;
			}
			populacao.add(individuo);
			
		}
		
	}
	
}
