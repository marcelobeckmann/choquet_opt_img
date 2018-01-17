package ui;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import choquet.Individual;

public class GraficoHistograma extends JPanel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7090645249690730917L;
	private ImageIcon image;
	private JFreeChart chart;

	public JFreeChart getChart() {
		return chart;
	}

	public GraficoHistograma() {

		this.setData(null);
		initialize();
	}

	public GraficoHistograma(List<Individual> data) {

		this.setData(data);
		initialize();
	}

	private void initialize() {

		setSize(640, 480);

	}

	protected void paintComponent(Graphics g) {

		if (chart != null) {
			image = new ImageIcon(chart.createBufferedImage(this.getWidth(), this.getHeight()));
			g.drawImage(image.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
		}

	}

	private class Category {
		public Category(String name) {
			this.name = name;
		}

		private String name;
		private int count;

		public String getName() {
			return name;
		}

		public int getCount() {
			return count;
		}

		public void add() {
			count++;
		}

	}

	public void setData(List<Individual> data) {
		
		double values[] = new double[data.size()];
		for (int i = 1; i < data.size(); i++) {
			Individual ind = data.get(i);
			// Binarize the stuffs
			values[i] = ((int) (ind.getFuncaoDesempenho().getDesempenho() * 100.00)) / 100.00;
		}

		Arrays.sort(values);

		/*for (double v:values)
		{
			System.out.println(v);
		}*/
		
		List<Category> cats = new ArrayList<Category>();
		double previous=values[0];
		//Sumarize the stuffs
		Category cat = new Category(values[0]+"");
		for (double value : values) {
		
			if (value!=previous)
			{
				cats.add(cat);
				previous=value;
				cat = new Category(value+"");
			
			}
			
			cat.add();
		}

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (Category cat1 : cats) {
			dataset.addValue(cat1.getCount(), "S", cat1.getName());
		}

		String plotTitle = "S frequency";
		String xaxis = "S";
		String yaxis = "frequency";
	
		
		chart = ChartFactory.createBarChart(
	           plotTitle,         // chart title
	            xaxis,               // domain axis label
	            yaxis,                  // range axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL, // orientation
	            true,                     // include legend
	            true,                     // tooltips?
	            false                     // URLs?
	        );
		
		
		
		StandardCategoryItemLabelGenerator sd= new StandardCategoryItemLabelGenerator();
	
				// Set the dial font to plain dialog size 14
				//sd.setTickLabelFont(new java.awt.Font("Dialog", java.awt.Font.PLAIN, 14));

		
		// chart = ChartFactory.createHistogram(plotTitle, xaxis, yaxis,
		// dataset, orientation, show, toolTips, urls);

		/*
		 * try {
		 * 
		 * ChartUtilities.saveChartAsPNG(new File("c:/temp/histogram1.PNG"),
		 * chart, 800, 600); } catch (IOException e) { }
		 */
		this.repaint();
	}

	public void saveAsPng(File file)
	{
		


		try {
			ChartUtilities.saveChartAsPNG(file, chart, 1280, 800);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static GraficoHistograma show(List<Individual> data, String title) {
		GraficoHistograma hist = new GraficoHistograma(data);

		String tit2 = "Histogram -" + title + " (pop size=" + data.size() + ")";
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		frame.setTitle(tit2);
		frame.setContentPane(hist);
		hist.getChart().setTitle(tit2);
		return hist;
	}

	public static void main(String args[]) {
		show(null, "Teste");
	}
}
