package ui;
import java.awt.Graphics;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;



public class GraficoLinha extends JPanel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7090645249690730917L;
	private ImageIcon image;
	private DefaultCategoryDataset dsGA;
	JFreeChart graficoGA;
	
	public JFreeChart getChart()
	{
		return graficoGA;
	}
	

	public GraficoLinha() {
		
		dsGA= new DefaultCategoryDataset();

		graficoGA = ChartFactory.createLineChart("","","",dsGA,PlotOrientation.VERTICAL ,true,false,false);
	
	}


	protected void paintComponent(Graphics g) {
	
		image = new ImageIcon(graficoGA.createBufferedImage(this.getWidth(), this.getHeight()));
		g.drawImage(image.getImage(),0,0,this.getWidth(),this.getHeight(),null);

	}
	
	public void setValue(double valorY, String serie, String valorX )
	{
		
			dsGA.setValue(valorY, serie, valorX);
	
	}
	
	public void clear()
	{
			dsGA.clear();
	
	}
	
	}
