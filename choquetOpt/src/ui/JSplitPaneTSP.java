package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SingleSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileFilter;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import br.ufrj.coppe.pec.ga.Ecosistema;
import br.ufrj.coppe.pec.util.Constantes;
import br.ufrj.coppe.pec.util.SerializableActionListener;
import br.ufrj.coppe.pec.util.SerializableChangeListener;
import br.ufrj.coppe.pec.util.SerializableJTextArea;
import choquet.Engine;

public class JSplitPaneTSP extends JSplitPane implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2606165996781214608L;

	private JFrameTSP frame;

	private GraficoLinha graficoLinha;

	transient private JFileChooser jFileChooser;

	private String nomeArquivo = "sem_nome.obj"; // @jve:decl-index=0:

	private Ecosistema ecosistema; // @jve:decl-index=0:

	transient private Thread currentThreadGA;

	private JSplitPane jSplitPane = null;

	private JSplitPane jSplitPane1 = null;

	private JScrollPane jScrollPane = null;

	protected SerializableJTextArea jTextAreaConsoleGA = null;

	private JTabbedPane jTabbedPane = null;

	private JPanel jPanelGA = null;

	private JPanel jPanel = null;

	private JLabel jLabel2 = null;

	private JLabel jLabel3 = null;

	private JPanel jPanel1 = null;

	private JLabel jLabel4 = null;

	private JPanel jPanel11 = null;

	private JLabel jLabel = null;

	private JLabel jLabel1 = null;

	private JLabel jLabel41 = null;

	private JLabel jLabel42 = null;

	private JLabel jLabel5 = null;

	private JLabel jLabel6 = null;

	private JTextField jTextFieldPopulacaoInicial = null;

	private JTextField jTextFieldPopulacaoMaxima = null;

	private JTextField jTextFieldPercentualCruzamento = null;

	private JTextField jTextFieldPercentualMutacao = null;

	private JTextField jTextFieldNumeroGeracoes = null;

	private JTextField jTextFieldDesempenhoEsperado = null;

	private JButton jButtonStart = null;

	private JButton jButtonParar = null;

	private JLabel jLabel7 = null;

	private JTextField jTextFieldDeteccaoConvergencia = null;

	private JLabel jLabel8 = null;

	private JButton jButtonSalvar;

	private JCheckBox jCheckBoxPermitirGemeos = null;

	// private MapaJPanel mapaJPanel = null;

	private JPanel plotPanel = null;

	private JTextField jTextFieldNumeroVertices = null;

	private JTextField jTextFieldNumeroFormigas = null;

	private JButton jButtonReset = null;

	private JSlider jSliderPercentualCruzamento = null;

	private JSlider jSliderPercentualMutacao = null;

	private JButton jButton1Salvar = null;

	private JButton jButton1Abrir = null;

	private JPanel jPanel2 = null;

	private JLabel jLabel12 = null;

	private JTextField jTextFieldNumeroIteracoes = null;

	private JLabel jLabel61 = null;
	
	private static int saveCount=0;

	public JSplitPaneTSP(JFrameTSP frame, Ecosistema ecosistema) {

		this.frame = frame;
		this.ecosistema = ecosistema;
		initialize();
	}

	public void resetGA() {
		getJTextAreaConsoleGA().setText("");
		graficoLinha.clear();

	}

	/**
	 * This method initializes jSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPane.setOneTouchExpandable(true);
			jSplitPane.setDividerLocation(420);
			jSplitPane.setDividerSize(8);
			jSplitPane.setBottomComponent(getJScrollPane());
			jSplitPane.setTopComponent(getJTabbedPane());
			jSplitPane.setContinuousLayout(true);
		}
		return jSplitPane;
	}

	/**
	 * This method initializes jSplitPane1
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getJSplitPane1() {
		if (jSplitPane1 == null) {
			// Inserir aqui o painel do mapa
			jSplitPane1 = new JSplitPane();
			jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
			jSplitPane1.setOneTouchExpandable(true);
			jSplitPane1.setDividerSize(8);
			jSplitPane1.setDividerLocation(420);
			jSplitPane1.setTopComponent(getPlotPanel());
			jSplitPane1.setBottomComponent(getGraficoLinha());

		}
		return jSplitPane1;
	}

	public JPanel getPlotPanel() {
		if (plotPanel == null) {

			plotPanel = new JPanel();
			// plotPanel = new PlotPanel();

		}
		return plotPanel;
	}

	public GraficoLinha getGraficoLinha() {
		if (graficoLinha == null) {

			graficoLinha = new GraficoLinha();
		}
		return graficoLinha;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	public JTextArea getJTextAreaConsoleGA() {
		if (jTextAreaConsoleGA == null) {
			jTextAreaConsoleGA = new SerializableJTextArea();

			jTextAreaConsoleGA.setFont(new Font("Courier", Font.PLAIN, 11));

		}
		return jTextAreaConsoleGA;
	}

	/**
	 * This method initializes jTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab("GA", null, getJPanelGA(), null);
			jTabbedPane.getModel().addChangeListener(new SerializableChangeListener() {

				public void stateChanged(ChangeEvent e) {
					SingleSelectionModel model = (SingleSelectionModel) e.getSource();
					getJScrollPane().setViewportView(null);
					// switch (1) { //model.getSelectedIndex()) {
					// case 1: {
					getJScrollPane().setViewportView(getJTextAreaConsoleGA());

					// jTextFieldDesempenhoEsperado.setText(jTextFieldDesempenhoEsperadoACO.getText());
					// break;
					// }

					plotPanel.repaint();
					graficoLinha.repaint();
				}
			});

		}
		return jTabbedPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelGA() {
		if (jPanelGA == null) {
			jLabel42 = new JLabel();
			jLabel42.setBounds(new Rectangle(5, 215, 128, 16));
			jLabel42.setText("Stop criteria:");
			jLabel42.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel41 = new JLabel();
			jLabel41.setBounds(new Rectangle(5, 5, 190, 16));
			jLabel41.setText("Population:");
			jLabel41.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel4 = new JLabel();
			jLabel4.setBounds(new Rectangle(5, 110, 169, 15));
			jLabel4.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel4.setText("Genetic operators:");
			jPanelGA = new JPanel();
			jPanelGA.setLayout(null);
			jPanelGA.setFont(new Font("Dialog", Font.PLAIN, 12));
			jPanelGA.add(getJPanel(), null);
			jPanelGA.add(getJPanel1(), null);
			jPanelGA.add(jLabel4, null);
			jPanelGA.add(getJPanel11(), null);
			jPanelGA.add(jLabel41, null);
			jPanelGA.add(jLabel42, null);
			jPanelGA.add(getJButtonStart(), null);
			jPanelGA.add(getJButtonSalvar(), null);
			jPanelGA.add(getJButtonReset(), null);

		}
		return jPanelGA;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel3 = new JLabel();
			jLabel3.setBounds(new Rectangle(5, 5, 91, 22));
			jLabel3.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel3.setText("Crossover:");
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(6, 46, 71, 22));
			jLabel2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel2.setText("Mutation:");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(new Rectangle(5, 123, 295, 89));
			jPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
			jPanel.add(jLabel2, null);
			jPanel.add(jLabel3, null);
			jPanel.add(getJTextFieldPercentualCruzamento(), null);
			jPanel.add(getJTextFieldPercentualMutacao(), null);
			jPanel.add(getJSliderPercentualCruzamento(), null);
			jPanel.add(getJSliderPercentualMutacao(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jLabel8 = new JLabel();
			jLabel8.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel8.setBounds(new Rectangle(210, 90, 64, 22));
			jLabel8.setText("");
			jLabel7 = new JLabel();
			jLabel7.setBounds(new Rectangle(5, 61, 187, 22));
			jLabel7.setToolTipText("");
			jLabel7.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel7.setText("Convergence limit (%):");
			jLabel6 = new JLabel();
			jLabel6.setBounds(new Rectangle(5, 33, 141, 22));
			jLabel6.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel6.setText("Expected fitness:");
			jLabel5 = new JLabel();
			jLabel5.setBounds(new Rectangle(5, 5, 127, 22));
			jLabel5.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel5.setText("Number of generations:");
			jPanel1 = new JPanel();
			jPanel1.setLayout(null);
			jPanel1.setBounds(new Rectangle(5, 229, 295, 120));
			jPanel1.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
			jPanel1.add(jLabel5, null);
			jPanel1.add(jLabel6, null);
			jPanel1.add(getJTextFieldNumeroGeracoes(), null);
			jPanel1.add(getJTextFieldDesempenhoEsperado(), null);
			jPanel1.add(jLabel7, null);
			jPanel1.add(getJTextFieldDeteccaoConvergencia(), null);
			jPanel1.add(jLabel8, null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel11
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel11() {
		if (jPanel11 == null) {
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(5, 33, 111, 22));
			jLabel1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel1.setText("Max population:");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(5, 5, 99, 20));
			jLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel.setText("Initial population:");
			jPanel11 = new JPanel();
			jPanel11.setLayout(null);
			jPanel11.setBounds(new Rectangle(5, 19, 295, 85));
			jPanel11.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
			jPanel11.add(jLabel, null);
			jPanel11.add(jLabel1, null);
			jPanel11.add(getJTextFieldPopulacaoInicial(), null);
			jPanel11.add(getJTextFieldPopulacaoMaxima(), null);
			jPanel11.add(getJCheckBoxPermitirGemeos(), null);

		}
		return jPanel11;
	}

	/**
	 * This method initializes jTextFieldPopulacaoInicial
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldPopulacaoInicial() {
		if (jTextFieldPopulacaoInicial == null) {
			jTextFieldPopulacaoInicial = new JTextField();
			jTextFieldPopulacaoInicial.setBounds(new Rectangle(124, 5, 71, 22));
			jTextFieldPopulacaoInicial.setText("350");
		}
		return jTextFieldPopulacaoInicial;
	}

	/**
	 * This method initializes jTextFieldPopulacaoMaxima
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldPopulacaoMaxima() {
		if (jTextFieldPopulacaoMaxima == null) {
			jTextFieldPopulacaoMaxima = new JTextField();
			jTextFieldPopulacaoMaxima.setBounds(new Rectangle(124, 33, 71, 22));
			jTextFieldPopulacaoMaxima.setText("100");
		}
		return jTextFieldPopulacaoMaxima;
	}

	/**
	 * This method initializes jTextFieldPercentualCruzamento
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldPercentualCruzamento() {
		if (jTextFieldPercentualCruzamento == null) {
			jTextFieldPercentualCruzamento = new JTextField();
			jTextFieldPercentualCruzamento.setBounds(new Rectangle(124, 5, 71, 22));
			jTextFieldPercentualCruzamento.setText("0.6");
		}
		return jTextFieldPercentualCruzamento;
	}

	/**
	 * This method initializes jTextFieldPercentualMutacao
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldPercentualMutacao() {
		if (jTextFieldPercentualMutacao == null) {
			jTextFieldPercentualMutacao = new JTextField();
			jTextFieldPercentualMutacao.setBounds(new Rectangle(124, 46, 71, 22));
			jTextFieldPercentualMutacao.setText("0.05");
		}
		return jTextFieldPercentualMutacao;
	}

	/**
	 * This method initializes jTextFieldNumeroGeracoes
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldNumeroGeracoes() {
		if (jTextFieldNumeroGeracoes == null) {
			jTextFieldNumeroGeracoes = new JTextField();
			jTextFieldNumeroGeracoes.setBounds(new Rectangle(202, 8, 71, 22));
			jTextFieldNumeroGeracoes.setText("20");
		}
		return jTextFieldNumeroGeracoes;
	}

	/**
	 * This method initializes jTextFieldDesempenhoEsperado
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldDesempenhoEsperado() {
		if (jTextFieldDesempenhoEsperado == null) {
			jTextFieldDesempenhoEsperado = new JTextField();
			jTextFieldDesempenhoEsperado.setBounds(new Rectangle(202, 36, 71, 22));
		}
		return jTextFieldDesempenhoEsperado;
	}

	/**
	 * This method initializes jButtonStart
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonStart() {
		if (jButtonStart == null) {
			jButtonStart = new JButton();
			jButtonStart.setBounds(new Rectangle(14, 357, 75, 24));
			jButtonStart.setFont(new Font("Dialog", Font.PLAIN, 12));
			jButtonStart.setText("Start");
			jButtonStart.addActionListener(new SerializableActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					// Reseta o painel
					resetGA();
					// Alimenta o ecosistema com os novos parametros
					ecosistema.setPopulacaoInicial(Integer.parseInt(jTextFieldPopulacaoInicial.getText()));
					ecosistema.setPopulacaoMaxima(Integer.parseInt(jTextFieldPopulacaoMaxima.getText()));
					ecosistema.setPercentualCruzamento(Double.parseDouble(jTextFieldPercentualCruzamento.getText()) * 100);
					ecosistema.setPercentualMutacao(Double.parseDouble(jTextFieldPercentualMutacao.getText()) * 100);

					if (!jTextFieldNumeroGeracoes.getText().equals("")) {
						ecosistema.setMaximoGeracoes(Integer.parseInt(jTextFieldNumeroGeracoes.getText()));
					}
					if (!jTextFieldDesempenhoEsperado.getText().equals("")) {
						ecosistema.setDesempenhoEsperado(Integer.parseInt(jTextFieldDesempenhoEsperado.getText()));
					} else {
						ecosistema.setDesempenhoEsperado(Constantes.NUMERO_NAO_INFORMADO);

					}

					if (!jTextFieldDeteccaoConvergencia.getText().equals("")) {
						// ecosistema.setMaximoGeracoes(Integer.parseInt(jTextFieldDeteccaoConvergencia.getText()));
					}

					if (currentThreadGA != null) {
						currentThreadGA.stop();
					}
					ecosistema.setPermitirGemeos(jCheckBoxPermitirGemeos.isSelected());

					currentThreadGA = new Thread(ecosistema);
					currentThreadGA.start();

				}
			});
		}
		return jButtonStart;
	}

	/**
	 * This method initializes jButtonParar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonSalvar() {
		if (jButtonParar == null) {
			jButtonParar = new JButton();
			jButtonParar.setBounds(new Rectangle(105, 357, 90, 24));
			jButtonParar.setFont(new Font("Dialog", Font.PLAIN, 12));
			jButtonParar.setText("Save charts");
			jButtonParar.addActionListener(new SerializableActionListener() {
				 
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						// currentThreadGA.stop();
						
						
						String id = ((Engine) JSplitPaneTSP.this.ecosistema).getExperimentIdentifier();
						int i = 0;
						frame.setExtendedState(Frame.ICONIFIED);
						
						
						String dir = "/home/home/BECKMANN/" + id + "/";
						new File(dir).mkdir();

						for (Object o : frame.getCharts()) {

							String f = dir + id + "_" + i;
							if (o instanceof SurfacePanel) {

								((SurfacePanel) o).saveAsPng(new File(f + "_" + saveCount+ ".PNG"));
							}

							else if (o instanceof GraficoHistograma) {
								((GraficoHistograma) o).saveAsPng(new File(f + ".PNG"));
							}
							i++;

						}

						String f = dir + id + "_" + i;
						JFreeChart chart = graficoLinha.getChart();
						try {
							ChartUtilities.saveChartAsPNG(new File(f + ".PNG"), chart, 640, 480);
						} catch (Exception e1) {
							e1.printStackTrace();
						}

						PrintWriter writer = new PrintWriter(new File(dir + id + "_result.txt"));

						writer.write(frame.getJContentPane().getJTextAreaConsoleGA().getText());
						writer.flush();
						writer.close();
						frame.setExtendedState(Frame.NORMAL);
						
						JOptionPane.showMessageDialog(null, "Charts saved sucessfully in " + dir);

					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
					saveCount++;

				}
			});
		}
		return jButtonParar;
	}

	/**
	 * This method initializes jTextFieldDeteccaoConvergencia
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldDeteccaoConvergencia() {
		if (jTextFieldDeteccaoConvergencia == null) {
			jTextFieldDeteccaoConvergencia = new JTextField();
			jTextFieldDeteccaoConvergencia.setBounds(new Rectangle(202, 64, 71, 22));
		}
		return jTextFieldDeteccaoConvergencia;
	}

	/**
	 * This method initializes jCheckBoxPermitirGemeos
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getJCheckBoxPermitirGemeos() {
		if (jCheckBoxPermitirGemeos == null) {
			jCheckBoxPermitirGemeos = new JCheckBox();
			jCheckBoxPermitirGemeos.setBounds(new Rectangle(5, 61, 127, 21));
			jCheckBoxPermitirGemeos.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCheckBoxPermitirGemeos.setSelected(false);
			jCheckBoxPermitirGemeos.setText("Permitir g�meos");
		}
		return jCheckBoxPermitirGemeos;
	}

	/**
	 * This method initializes jTextFieldNumeroVertices
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldNumeroVertices() {
		if (jTextFieldNumeroVertices == null) {
			jTextFieldNumeroVertices = new JTextField();
			jTextFieldNumeroVertices.setBounds(new Rectangle(135, 15, 53, 21));

		}
		return jTextFieldNumeroVertices;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */

	/**
	 * This method initializes jTextFieldNumeroFormigas
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldNumeroFormigas() {
		if (jTextFieldNumeroFormigas == null) {
			jTextFieldNumeroFormigas = new JTextField("50");
			jTextFieldNumeroFormigas.setBounds(new Rectangle(168, 7, 71, 22));
		}
		return jTextFieldNumeroFormigas;
	}

	/**
	 * This method initializes jButtonReset
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButtonReset() {
		if (jButtonReset == null) {
			jButtonReset = new JButton();
			jButtonReset.setBounds(new Rectangle(215, 357, 75, 24));
			jButtonReset.setText("Stop");
			jButtonReset.setFont(new Font("Dialog", Font.PLAIN, 12));
			jButtonReset.addActionListener(new SerializableActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					currentThreadGA.stop();
					/*
					 * if (currentThreadGA != null) { currentThreadGA.stop(); }
					 * ecosistema.reset(); graficoLinha.clear();
					 * ecosistema.setMelhorIndividuo(null); plotPanel.repaint();
					 * graficoLinha.repaint();
					 */
				}
			});
		}
		return jButtonReset;
	}

	/**
	 * This method initializes jButton1Salvar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1Salvar() {
		if (jButton1Salvar == null) {
			jButton1Salvar = new JButton();
			jButton1Salvar.setBounds(new Rectangle(215, 357, 75, 24));
			jButton1Salvar.setFont(new Font("Dialog", Font.PLAIN, 12));
			jButton1Salvar.setText("Salvar...");
			jButton1Salvar.addActionListener(new SerializableActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					try {
						JFileChooser fc = getFileChooser();

						// show the filechooser
						int result = fc.showOpenDialog(null);

						// if we selected an image, load the image
						if (result == JFileChooser.APPROVE_OPTION) {
							nomeArquivo = fc.getSelectedFile().getPath();

							frame.setTitle("TSP - " + nomeArquivo);
							frame.serialize(nomeArquivo);
							fc.setCurrentDirectory(new File(nomeArquivo));

						}

					} catch (IOException e1) {

						e1.printStackTrace();
						JOptionPane.showMessageDialog(frame.getContentPane(), e1.getMessage());
					}

				}

			});
		}
		return jButton1Salvar;
	}

	public JFileChooser getFileChooser() {
		if (jFileChooser == null) {

			jFileChooser = new JFileChooser();

		}
		return jFileChooser;
	}

	/**
	 * This method initializes jButton1Abrir
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton1Abrir() {
		if (jButton1Abrir == null) {
			jButton1Abrir = new JButton();
			jButton1Abrir.setBounds(new Rectangle(105, 357, 90, 24));
			jButton1Abrir.setText("Abrir...");

			jButton1Abrir.setFont(new Font("Dialog", Font.PLAIN, 12));
			jButton1Abrir.addActionListener(new SerializableActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					try {
						FileFilter fileFilter = new FileFilterTSP();
						JFileChooser fc = getFileChooser();
						fc.setFileFilter(fileFilter);
						// show the filechooser
						int result = fc.showOpenDialog(null);

						// if we selected an image, load the image
						if (result == JFileChooser.APPROVE_OPTION) {

							nomeArquivo = fc.getSelectedFile().getPath();

							if (nomeArquivo == null) {
								return;
							}
							if (nomeArquivo == null || nomeArquivo.indexOf(".tsp") != -1) {

								carregarArquivoTSP(nomeArquivo);

								plotPanel.repaint();
								// getJPanelGrafo().repaint();
								// Tratar chamada
							} else // Sen�o � um objeto
							{
								JFrameTSP frame2 = frame.deSerialize(nomeArquivo);
								frame2.setVisible(true);
								frame2.setTitle("TSP - " + nomeArquivo);
								frame.setVisible(false);
								Thread.currentThread().interrupt();
								Thread.currentThread().stop();

							}
							fc.setCurrentDirectory(new File(nomeArquivo));

						}

					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(frame.getContentPane(), e1.getMessage());
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(frame.getContentPane(), e1.getMessage());
					}

				}
			});
		}
		return jButton1Abrir;
	}

	private void carregarArquivoTSP(String nomeArquivo) throws IOException {
		BufferedReader in = null;
		boolean carregar = false;
		int longitudeMaxima = 0;
		int latitudeMaxima = 0;

		int i = 0;
		try {
			in = new BufferedReader(new FileReader(nomeArquivo));
			String s = in.readLine();
			while (s != null) {

				if (carregar && !s.equalsIgnoreCase("EOF") && !s.equals("")) {

					StringTokenizer token = new StringTokenizer(s, ", ");

					// A primeira coluna n�o conta
					token.nextToken();

					String str = token.nextToken();
					int latitude = (int) Double.parseDouble(str.replace('.', ' ').replaceAll(" ", ""));

					str = token.nextToken();
					int longitude = (int) Double.parseDouble(str.replace('.', ' ').replaceAll(" ", ""));

					latitudeMaxima = Math.max(latitudeMaxima, latitude);
					longitudeMaxima = Math.max(longitudeMaxima, longitude);

					s = in.readLine();

					i++;
				} else {
					if (s.equalsIgnoreCase("NODE_COORD_SECTION")) {
						carregar = true;

					}
					s = in.readLine();
				}
				// System.out.println(s);
			}

		} finally {
			in.close();
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(800, 600);

		this.setDividerSize(8);
		this.setDividerLocation(320);
		this.setRightComponent(getJSplitPane1());
		this.setOneTouchExpandable(true);
		this.setLeftComponent(getJSplitPane());
		getJScrollPane().setViewportView(getJTextAreaConsoleGA());
	}

	public Ecosistema getEcosistema() {
		return ecosistema;
	}

	public void setEcosistema(Ecosistema ecosistema) {
		this.ecosistema = ecosistema;

	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jLabel61 = new JLabel();
			jLabel61.setBounds(new Rectangle(8, 36, 135, 20));
			jLabel61.setText("Desempenho esperado:");
			jLabel61.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel12 = new JLabel();
			jLabel12.setBounds(new Rectangle(7, 7, 119, 20));
			jLabel12.setText("N�mero de Itera��es:");
			jLabel12.setFont(new Font("Dialog", Font.PLAIN, 12));
			jPanel2 = new JPanel();
			jPanel2.setLayout(null);
			jPanel2.setBounds(new Rectangle(7, 238, 293, 79));
			jPanel2.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
			jPanel2.add(jLabel12, null);
			jPanel2.add(getJTextFieldNumeroIteracoes(), null);
			jPanel2.add(jLabel61, null);

		}
		return jPanel2;
	}

	/**
	 * This method initializes jTextFieldNumeroIteracoes
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextFieldNumeroIteracoes() {
		if (jTextFieldNumeroIteracoes == null) {
			jTextFieldNumeroIteracoes = new JTextField("100");
			jTextFieldNumeroIteracoes.setBounds(new Rectangle(161, 7, 36, 22));
		}
		return jTextFieldNumeroIteracoes;
	}

	/**
	 * This method initializes jSliderPercentualCruzamento
	 * 
	 * @return javax.swing.JSlider
	 */
	private JSlider getJSliderPercentualCruzamento() {
		if (jSliderPercentualCruzamento == null) {
			jSliderPercentualCruzamento = new JSlider();
			jSliderPercentualCruzamento.setBounds(new Rectangle(7, 29, 200, 15));
			jSliderPercentualCruzamento.setValue(50);
			jSliderPercentualCruzamento.addChangeListener(new SerializableChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					double valor = jSliderPercentualCruzamento.getValue();

					jTextFieldPercentualCruzamento.setText(String.valueOf(valor / 100));
				}
			});
		}
		return jSliderPercentualCruzamento;
	}

	/**
	 * This method initializes jSliderPercentualMutacao
	 * 
	 * @return javax.swing.JSlider
	 */
	private JSlider getJSliderPercentualMutacao() {
		if (jSliderPercentualMutacao == null) {
			jSliderPercentualMutacao = new JSlider();
			jSliderPercentualMutacao.setBounds(new Rectangle(7, 70, 200, 15));
			jSliderPercentualMutacao.setValue(10);
			jSliderPercentualMutacao.addChangeListener(new SerializableChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					double valor = jSliderPercentualMutacao.getValue();

					jTextFieldPercentualMutacao.setText(String.valueOf(valor / 100));

				}
			});
		}
		return jSliderPercentualMutacao;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
