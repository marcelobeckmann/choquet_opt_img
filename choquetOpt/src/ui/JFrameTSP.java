package ui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import br.ufrj.coppe.pec.ga.Ecosistema;
import br.ufrj.coppe.pec.ga.FimEcosistemaListener;
import br.ufrj.coppe.pec.ga.FimInicializacaoListener;
import br.ufrj.coppe.pec.ga.InicioEcosistemaListener;
import br.ufrj.coppe.pec.ga.NovaGeracaoListener;
import choquet.Engine;

public class JFrameTSP extends JFrame implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4832169807358789973L;

	private Ecosistema ecosistema;

	private JSplitPaneTSP jContentPane = null;

	private List charts;
	

	public List getCharts() {
		if (charts==null) {
			charts= new ArrayList();
		}
		return charts;
	}

	public void setCharts(List charts) {
		this.charts = charts;
	}

	/**
	 * This is the default constructor
	 */
	public JFrameTSP(Engine engine) {
		
		this.ecosistema = engine;
		initialize();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		AGListenerTSP listener = new AGListenerTSP(this);
		ecosistema.addListener((NovaGeracaoListener) listener);
		ecosistema.addListener((FimEcosistemaListener) listener);
		ecosistema.addListener((InicioEcosistemaListener) listener);
		ecosistema.addListener((FimInicializacaoListener) listener);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	public void initialize() {
		this.setSize(800, 600);

		this.setContentPane(getJContentPane());
		this.setTitle("GA for Choquet");
		this.setVisible(true);
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	public JSplitPaneTSP getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JSplitPaneTSP( this, ecosistema);
		}
		return jContentPane;
	}

	
	

	public Ecosistema getEcosistema() {
		return ecosistema;
	}

	public void setEcosistema(Ecosistema ecosistema) {
		this.ecosistema = ecosistema;
		this.getJContentPane().setEcosistema(ecosistema);
	}

	public void serialize(String fileName) throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		oos.writeObject(this);

		oos.close();

	}

	public JFrameTSP deSerialize(String fileName) throws IOException,
			ClassNotFoundException {
		try {
			FileInputStream fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			JFrameTSP frame = (JFrameTSP) ois.readObject();
			ois.close();
			return frame;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
