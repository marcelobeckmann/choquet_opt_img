package choquet;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * Consumes the process outputs, in order it not hangs.
 * @author MBeckmann
 *
 */
public class StreamGobbler implements Runnable {
	
	private InputStream is;
	private Thread thread;
	private boolean showOutput;
	private boolean storeOutput;
	private StringBuilder storedOutput=new StringBuilder();
	public StreamGobbler( InputStream is,boolean showOutput,boolean storeOutput) {
		
		this.is = is;
		this.showOutput=showOutput;
		this.storeOutput=storeOutput;
	}

	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);

			while (true) {
				String s = br.readLine();
				if (s == null)
					break;
				if (showOutput) {
					System.out.println( s);
				}
				if (storeOutput )
				{
					storedOutput.append(s);
					
				}
			}

			is.close();

		} catch (Exception ex) {
			
			
		}
	}

	public StringBuilder getStoredOutput() {
		return storedOutput;
	}
}
