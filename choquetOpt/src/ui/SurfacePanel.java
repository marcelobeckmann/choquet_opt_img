package ui;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import net.ericaro.surfaceplotter.JSurfacePanel;
import net.ericaro.surfaceplotter.Mapper;
import net.ericaro.surfaceplotter.ProgressiveSurfaceModel;
import choquet.Individual;
import choquet.Parameter;




/**
 * @author User #1
 */
public class SurfacePanel extends JSurfacePanel {
        private ProgressiveSurfaceModel model;
        private String title;
        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        private JSurfacePanel surfacePanel1;
        private JToolBar toolBar1;
        private JButton button1;
        private Map <String, Double > xyData;
        private static final int PRECISION=2;
        private JFrame form;
        private static NumberFormat nf;
        {
        	nf= NumberFormat.getInstance();
        	nf.setMaximumFractionDigits(PRECISION);
        	nf.setMinimumFractionDigits(PRECISION);
        	
        }
        public SurfacePanel()
        {
        	this(null,0,0,0,0);
        }
        
        public void setTitle(String tit)
        {
        title=tit;	
        }
        public void show()
        {
        	
        	
        	 
             form = new JFrame("sample");
             form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
             form.setSize(640,480);
         	form.setExtendedState(JFrame.MAXIMIZED_BOTH);
             form.getContentPane().add(this, BorderLayout.CENTER);
             surfacePanel1.setTitleText(title);
             
             form.setTitle("Surface -" + title);
         //    jf.pack();
             
             form.setVisible(true);
        	
        }
        
        public void setData(List <Individual> data,int x, int y) {
        
        	
        	xyData = new HashMap<String,Double>();
        	String key;
        	for (Individual ind:data)
        	{
        		Parameter [] genes = (Parameter[])ind.getGenes();
        		key = nf.format(genes[x].getValor())+"," + nf.format(genes[y].getValor());
        		xyData.put(key, ind.getFuncaoDesempenho().getDesempenho());
//        		System.out.println(key);
        		
        	}
        		
        }
        public void saveAsPng(File file)
        {
        	//throws both AWTException and IOException
        	try {
        		
        		//form.setExtendedState(JFrame.MAXIMIZED_BOTH);
        	form.setAlwaysOnTop(true);
				BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
				
				
        		form.setAlwaysOnTop(false);
        		ImageIO.write(image, "PNG", file);
        		
        		Thread.currentThread().join(100);
        		//form.setExtendedState(JFrame.NORMAL);
        		
        		/* Graphics g = surfacePanel1.getGraphics();
			    Graphics2D g2d = (Graphics2D)g;
			    int width = 800;
			    int height = 600    ;
			    
			    BufferedImage
			    
			    image = g2d.getDeviceConfiguration().createCompatibleImage(
			            width, height, Transparency.BITMASK);
			    
			    ImageIO.write(image, "PNG", file);*/

				
			} catch (HeadlessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
        	
        	
        }
        
        public JFrame getForm() {
			return form;
		}

		// JFormDesigner - End of variables declaration  //GEN-END:variables
        public SurfacePanel(List <Individual> data,int x, int y, float min, float max) {
                initComponents();
               
                //DefaultSurfaceModel model = new DefaultSurfaceModel();
                model = new ProgressiveSurfaceModel() ;
                model.setXMax(max);
                model.setYMax(max);
                model.setZMax(1);
                model.setDisplayGrids(true);
                model.setXMin(min);
                model.setYMin(min);
                model.setZMin(0);
                model.setBoxed(true);
                model.setDisplayXY(true);
                model.setDisplayZ(true);
                model.setDensityType(true);
                //model.setBothFunction(true);
                
                
//                JOptionPane.showMessageDialog(null,model.getCurrentDefinition());
                //model.setCurrentDefinition(5);
                //model.setFirstFunctionOnly(true);
                
                
        
                if (data!=null)
                {
                	setData(data,x,y);

                	
                    model.setMapper(new Mapper() {
                        public  float f1( float x, float y)
                        {
                    		String key = nf.format(x)+"," + nf.format(y);
                    		Double z= xyData.get(key);

    //                		System.err.print( key);
                    		if (z==null) {
//                    			System.err.println();
                    			return 0; } 
                    		else
                    		{
  //                  			System.err.println('*');
                    			return 1-z.floatValue();
                    		}
                        }
                        
                        public  float f2( float x, float y)
                        {
                        	
                        	
                        	String key = nf.format(x)+"," + nf.format(y);
                    		Double z= xyData.get(key);
                    		return (float)(Math.sin(x*y));
                        }
                });

                	
                }
                
                else {
                
                model.setMapper(new Mapper() {
                        public  float f1( float x, float y)
                        {
                        		//System.out.println(x+","+y);
                        		
                                float r = x*x+y*y;
                               
                                if (r == 0 ) return 1f;
                                
                               return (float)( Math.sin(r)/(r));
                        }
                        
                        public  float f2( float x, float y)
                        {
                               
                        	//System.out.println(x+","+y);
                    		
                        	return (float)(Math.sin(x*y));
                        }
                });
                }
                
                surfacePanel1.setModel(model);
                surfacePanel1.setTitleText("");
                
                model.plot().execute();
        }

        public static void main(String[] args) {
                SurfacePanel sample = new SurfacePanel();
                sample.show();	
        }


        private void initComponents() {
                // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
                surfacePanel1 = new JSurfacePanel();
                toolBar1 = new JToolBar();
                button1 = new JButton();
                
                //======== this ========
                setLayout(new BorderLayout());

                //---- surfacePanel1 ----
                surfacePanel1.setTitleText(title);
                surfacePanel1.setBackground(Color.white);
                surfacePanel1.setTitleVisible(true);
                

                surfacePanel1.setTitleFont(surfacePanel1.getTitleFont().deriveFont(surfacePanel1.getTitleFont().getStyle() | Font.BOLD, surfacePanel1.getTitleFont().getSize()-2));
                surfacePanel1.setConfigurationVisible(true);
                setLayout(new BorderLayout());
                add(surfacePanel1, BorderLayout.CENTER);

                //======== toolBar1 ========
                toolBar1.add(button1);
                add(toolBar1, BorderLayout.NORTH);
                // JFormDesigner - End of component initialization  //GEN-END:initComponents
        }

        public ProgressiveSurfaceModel getModel()
        {
        	return model;
        }
       
}
