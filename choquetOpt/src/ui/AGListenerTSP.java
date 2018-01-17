package ui;


import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import br.ufrj.coppe.pec.ga.Ecosistema;
import br.ufrj.coppe.pec.ga.FimEcosistemaListener;
import br.ufrj.coppe.pec.ga.FimInicializacaoListener;
import br.ufrj.coppe.pec.ga.InicioEcosistemaListener;
import br.ufrj.coppe.pec.ga.NovaGeracaoListener;
import br.ufrj.coppe.pec.util.Constantes;
import choquet.Engine;
import choquet.Individual;

public class AGListenerTSP implements InicioEcosistemaListener, NovaGeracaoListener , FimEcosistemaListener, FimInicializacaoListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6346506317625185938L;
	private long inicio;
	private short contador=1;
	private JFrameTSP frame;
	private JTextArea jTextAreaConsoleGA;
	private GraficoLinha grafico;
	private JPanel plotPanel;
	
	
	public AGListenerTSP(JFrameTSP frame) {
		this.frame = frame;
		jTextAreaConsoleGA= frame.getJContentPane().getJTextAreaConsoleGA();
		grafico = frame.getJContentPane().getGraficoLinha();
		plotPanel = frame.getJContentPane().getPlotPanel();
	
	}

	
	public void novaGeracaoCriada(int numeroGeracao) {

		//Para efeito de performance, só atualiza de n em n geracoes
		if (contador!=Constantes.NIVEL_ATUALIZACAO) {
			contador++;
			return;
		}
		else
		{
			contador=1;
		}
		Engine ecosistema= (Engine)frame.getEcosistema();
		StringBuffer buffer=new StringBuffer(jTextAreaConsoleGA.getText()); 
		buffer.append(ecosistema.toString());
		//buffer.append("\n"+frame.getEcosistema().getMelhorIndividuo().toString());
		
		long duracao= System.currentTimeMillis()-inicio;

		buffer.append('\n');
		buffer.append("elapsed time: ");
		buffer.append(String.valueOf(duracao));
		buffer.append(" ms\n");

		jTextAreaConsoleGA.setText(buffer.toString());
		jTextAreaConsoleGA.repaint();
		String strNumeroGeracao = String.valueOf(numeroGeracao);
		grafico.setValue(ecosistema.getDesempenhoMinimoAtual(), "worst", strNumeroGeracao);
		grafico.setValue(ecosistema.getDesempenhoMaximoAtual(), "best", strNumeroGeracao);
		
		grafico.setValue(ecosistema.getAverageFitness(), "avg", strNumeroGeracao);
		
		if (ecosistema.getExpertParameters()!=null )
		{
			grafico.setValue(ecosistema.getExpertParameters().getFuncaoDesempenho().getDesempenho(), "expert", strNumeroGeracao);
		
		}
		grafico.repaint();
		
		
		
		if (plotPanel instanceof PlotPanel)
		((PlotPanel)plotPanel).plot((List<Individual>)(Object)ecosistema.getPopulacao());
		
		
	}

	public void ecosistemaIniciado() {

		inicio= System.currentTimeMillis();
		Engine ecosistema= (Engine)frame.getEcosistema();
		jTextAreaConsoleGA.setText(ecosistema.getParametersString());
		jTextAreaConsoleGA.repaint();
	}

	public void ecosistemaFinalizado() {
		
		contador=Constantes.NIVEL_ATUALIZACAO;
		novaGeracaoCriada(frame.getEcosistema().getGeracaoAtual());

		Ecosistema ecosistema= frame.getEcosistema();
		
		List <Individual> pop =(List<Individual>)(Object)ecosistema.getPopulacao();
		
		frame.getCharts().add(GraficoHistograma.show(pop, "Last generation"));
		showSurfacePanel(0,1,"Last generation");
		showSurfacePanel(2,3,"Last generation");
		showSurfacePanel(4,5,"Last generation");
		
		
	}

	private SurfacePanel showSurfacePanel(int x,int y ,String title)
	{

		Engine ecosistema= (Engine)frame.getEcosistema();
		
		List <Individual> pop =(List<Individual>)(Object)ecosistema.getPopulacao();
		
		Individual expert = ecosistema.getExpertParameters();
		String expertText="";
		if (expert!=null)
		{
			double valuex= expert.getGenes()[x].getValor();
			double valuey= expert.getGenes()[y].getValor();
			
			expertText= " (expert_x=" + valuex + ",expert_y=" + valuey + ")";
		}
		
		float max = Math.max(ecosistema.getMaxValue(x),ecosistema.getMaxValue(y))+0.05f;
		if (max>1)
			max=1;


		float min = Math.min(ecosistema.getMinValue(x),ecosistema.getMinValue(y))-0.05f;
	
		if (min<0)
			min=0;
		
		SurfacePanel sp1= new SurfacePanel(pop,x,y, 0,1);
		sp1.setTitle( title + " - Surface Plot (x=" + x + ",y="+ y+")"+ expertText );
		sp1.show();
		frame.getCharts().add(sp1);
		
		return sp1;

	}
	@Override
	public void ecosystemaInicializado() {
		
		Engine ecosistema= (Engine)frame.getEcosistema();
		
		
		
		List <Individual> pop =(List<Individual>)(Object)ecosistema.getPopulacao();
		StringBuilder sb= new StringBuilder(jTextAreaConsoleGA.getText());
		sb.append("### Initial population\n");
		sb.append(ecosistema.getPopulationString(ecosistema.getPopulacao()));
				
		jTextAreaConsoleGA.setText(sb.toString());
		sb.append("### End of initial population\n");
		
		if (plotPanel instanceof PlotPanel)
			((PlotPanel)plotPanel).plot(pop);
		
		
		frame.getCharts().add(GraficoHistograma.show(pop,"Initialization" ) );
		 showSurfacePanel(0,1,"Initialization");
		 showSurfacePanel(2,3,"Initialization");
		 showSurfacePanel(4,5,"Initialization");
	
		
			
			
	}

}
