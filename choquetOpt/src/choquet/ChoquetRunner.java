package choquet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ChoquetRunner {

	public static  String path = "/home/home/BECKMANN/ChoquetFusionOptimization/32parameters5att/";
	private static final String command = path + "run.sh";
	private static StreamGobbler outputGlobber;
	private static final boolean runFake=false;
	
	public static double runChoquet(Individual individual, double... parm) {
		
		File parmFile = new File(path + "SimR" + Engine.getRegion()+"_h256_1.txt");
		double valor = 0;
		try {
			if (runFake) {
				valor = Math.random()*0.5;

			}
			else
			{
				generateFileParameters(parmFile, parm);
				exec(command+" "+ Engine.getRegion());
				valor = ChoquetRunner.extractResult(individual);
				
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return valor;
	}

	private static Process exec(final String command) {

		Process p = null;
		try {

			// System.out.println(command);
			p = Runtime.getRuntime().exec(command);
			outputGlobber = new StreamGobbler(p.getInputStream(), false, true);
			StreamGobbler s2 = new StreamGobbler(p.getErrorStream(), true,
					false);
			outputGlobber.start();
			s2.start();
			p.waitFor();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return p;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// System.out.println(runChoquet(new double[]{0.720311147158,
		// 0.836011709051, 0.943110667565, 0.015834853585, 0.769082003525,
		// 0.891604947586}));
	}

	private static double extractResult(Individual individual) {
				
		int r=Engine.getRegion();
		//for (int i = 1; i <= 3; i++) {
			final String TO_SEARCH = "Separabilite de la region R" +r;
			double res=0;
			String result = outputGlobber.getStoredOutput().toString();
			//System.out.println(result);
			int pos = result.indexOf(TO_SEARCH);
			if (pos == -1) {
				res= 0;
			}
			result = result.substring(pos);
			pos = result.indexOf('=');
			if (pos == -1) {
				res=  0;
			}
			result = result.substring(pos + 1,pos+10).trim();
			result = result.replace('\t' , ' ');
			result = result.replace('\n' , ' ');
			result = result.replace('\r' , ' ');
			result= result.trim();
			try {
				res = Double.parseDouble(result);
			} catch (Exception e) {
				e.printStackTrace();
				res= 0;
			}
			
			

			return res;
	}

	private static void generateFileParameters(File file, double... parms)
			throws IOException {
		PrintWriter pw = null;

		try {
			pw = new PrintWriter(file);

			pw.println("0.0");
			for (double p : parms) {
				pw.println(p);
			}
			pw.println("1.0");
			pw.flush();

		} finally {
			if (pw == null) {
				pw.close();

			}

		}

	}
}
