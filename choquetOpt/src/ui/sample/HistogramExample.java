package ui.sample;

import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

public class HistogramExample {
	public static void main(String[] args) {
		double[] value = new double[100];
		Random generator = new Random(System.currentTimeMillis());
		for (int i = 1; i < 100; i++) {
			value[i] = generator.nextDouble();

		}
		value = new  double[]{1,2,2,3,3,3};
		int number = 10;
		HistogramDataset dataset = new HistogramDataset();
		dataset.setType(HistogramType.FREQUENCY);
		dataset.addSeries("Histogram", value, number);
		String plotTitle = "Histogram";
		String xaxis = "number";
		String yaxis = "value";
		PlotOrientation orientation = PlotOrientation.VERTICAL;
		boolean show = false;
		boolean toolTips = false;
		boolean urls = false;
		JFreeChart chart = ChartFactory.createHistogram(plotTitle, xaxis,
				yaxis, dataset, orientation, show, toolTips, urls);
		int width = 500;
		int height = 300;

		
		ImageIcon image = new ImageIcon(chart.createBufferedImage(width, height));
		JOptionPane.showMessageDialog(null,new JLabel(image));
		
/*		try {

			ChartUtilities.saveChartAsPNG(new File("c:/temp/histogram.PNG"),
					chart, width, height);
		} catch (IOException e) {
		}
*/
	}
}