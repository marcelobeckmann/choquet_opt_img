package ui.sample;
import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.ericaro.surfaceplotter.JSurfacePanel;
import net.ericaro.surfaceplotter.Mapper;
import net.ericaro.surfaceplotter.ProgressiveSurfaceModel;
import choquet.Individual;



public class SimpleRun extends JSurfacePanel {
        
		private ProgressiveSurfaceModel model;	
		private List <Individual>data;
		
		private String title;
		public void setData(List <Individual> data)
		{
			this.data= data;
			
			
		}
		public void setTitle(String titl)
		{
			title= titl;
			
		}
		
        
        public void initialize() 
        {
                
                setTitleText(title);
                
                model = new ProgressiveSurfaceModel() ;
                
                
                
                
                model.setMapper(new Mapper() {
                        public  float f1( float x, float y)
                        {
                        		System.out.println(x+","+y);
                        		
                                float r = x*x+y*y;
                               
                                if (r == 0 ) return 1f;
                               return (float)( Math.sin(r)/(r));
                        }
                        
                        public  float f2( float x, float y)
                        {
                               
                        	System.out.println(x+","+y);
                    		
                        	return (float)(Math.sin(x*y));
                        }
                });
                
                this.setModel(model);
                
                
                //jsp.doRotate();
                
                //canvas.doPrint();
                //sm.doCompute();
        }

        public void startFrame() 
        {
                initialize();
                
                
                JFrame jf= new JFrame("test");
                jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jf.getContentPane().add(this, BorderLayout.CENTER);
                jf.pack();
                jf.setSize(640,480);
                jf.setVisible(true);
                
                
                
        }
        /*
        public static float f1( float x, float y)
        {  
                             System.out.print('.');
         //       return (float)( Math.sin(x*x+y*y)/(x*x+y*y));
                             return (float) (1.0 + 0.25 * Math.sin(3*y));
                return (float)(10*x*x+5*y*y+8*x*y -5*x+3*y);
        }
        
        public static float f2( float x, float y)
        {
                return (float)(Math.sin(x*x-y*y)/(x*x+y*y));
                //return (float)(10*x*x+5*y*y+15*x*y-2*x-y);
        }
        
        */
        
        
        public static void main(String[] args) 
        {
                SwingUtilities.invokeLater(new Runnable() {
                        
                        public void run() {
                                new SimpleRun().startFrame() ;
                        }
                });
                
        }
        
}
