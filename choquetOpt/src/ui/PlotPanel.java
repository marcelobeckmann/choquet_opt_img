package ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.LineArray;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import br.ufrj.coppe.pec.ga.Gene;
import choquet.Individual;
import choquet.Parameter;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class PlotPanel extends JPanel {

	static final float coordinate_parameter = 0.8F;
	private static final long serialVersionUID = 1L;
	
	private JPanel jPanelButtons = null;
	private JButton jButtonStop = null;
	private JTextArea jTextArea = null;
	private int numAttributes;
	private Canvas3D canvas3D;
	private BranchGroup sceneGraph;
	private TransformGroup trg;
	private SimpleUniverse su;
	static  float cube_length = 0.005F;
	private boolean stopRequested = false;
	private Color3f colors[] = { new Color3f(1.0F, 1.0F, 0.0F),
			new Color3f(0.0F, 1.0F, 1.0F), new Color3f(1.0F, 0.0F, 1.0F) };

	private List<Individual> data;
	BranchGroup createTransBranchGroup;
	BranchGroup line_branch_group;
	BranchGroup surface_branch_group;
	private int previousX;
	private int previousY;
	private int previousZ;
	private boolean noPlotFromUIEvent;

	/**
	 * This is the default constructor
	 */
	public PlotPanel() {
		super();
		initialize();
	}

	public void plot(List<Individual> data) {
		this.data = data;
		noPlotFromUIEvent = true;
		if (data != null) {
			

			getNumAttributes();
			jSlider.setMinimum(0);
			jSlider.setMaximum(numAttributes - 2);
			try {
				String variables = "(" + jSlider.getValue() + ","
						+ (jSlider.getValue() + 1) ;
						//+ "," + (jSlider.getValue() + 2) + ")";
				jSlider.setToolTipText(variables);
			} catch (Exception e) {
				// do anything
			}

			for (int i = 0; i < data.size(); ++i) {
				Individual ind = data.get(i);
				//if (i==0 || ind.isExpert()) {
					plotInstance(ind);
				//}
			}

			noPlotFromUIEvent = true;
			initializeComboBoxes(data);

			getJComboBoxX().setSelectedIndex(jSlider.getValue());
			if (jSlider.getValue() + 1 <= numAttributes) {
				getJComboBoxY().setSelectedIndex(jSlider.getValue() + 1);
			}
			if (jSlider.getValue() + 2 <= numAttributes) {
				getJComboBoxZ().setSelectedIndex(jSlider.getValue() + 2);
			}

		}

		noPlotFromUIEvent = false;
	}

	private void getNumAttributes() {
		
		if (data != null && data.size() != 0) {

			Gene[] genes = data.get(0).getGenes();
			if (genes != null) {

				numAttributes =  genes.length;
			} else {
				numAttributes= 0;
			}
		} else {
			numAttributes= 0;
		}
	}

	private void initializeComboBoxes(List<Individual> data) {

		if (getJComboBoxX().getItemCount() == 0) {
			
			getNumAttributes();
			for (int i = 0; i <numAttributes; ++i) {
				getJComboBoxX().addItem(i);
				getJComboBoxY().addItem(i);
				getJComboBoxZ().addItem(i);

			}

			getJComboBoxX().setSelectedIndex(0);

			// reset the combo boxes
			if (numAttributes > 1) {
				getJComboBoxY().setSelectedIndex(1);
			} else {

				getJComboBoxY().setSelectedIndex(-1);
			}

			if (numAttributes > 2) {
				getJComboBoxZ().setSelectedIndex(2);
			} else {
				getJComboBoxZ().setSelectedIndex(-1);

			}

			previousY = jComboBoxY.getSelectedIndex();
			previousZ = jComboBoxZ.getSelectedIndex();
			previousX = jComboBoxX.getSelectedIndex();

		}
	}



	public Canvas3D getCanvas3D() {
		if (canvas3D == null) {
			canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
			
			// layoutComponents();

			createSimpleUniverse();
		}
		return canvas3D;
	}

	public void createSimpleUniverse() {
		sceneGraph = new BranchGroup();
		trg = new TransformGroup();
		trg.setCapability(14);
		trg.setCapability(13);
		trg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		createTrans();
		sceneGraph.addChild(trg);
		su = new SimpleUniverse(canvas3D);
		ViewingPlatform viewingPlatform = su.getViewingPlatform();
		OrbitBehavior orbit = new OrbitBehavior(canvas3D, 112);
		BoundingSphere bounds = new BoundingSphere(
				new Point3d(0.0D, 0.0D, 0.0D), 10D);
		orbit.setSchedulingBounds(bounds);
		viewingPlatform.setViewPlatformBehavior(orbit);
		viewingPlatform.setNominalViewingTransform();
		Background bk = new Background();
		bk.setApplicationBounds(bounds);
		bk.setColor(new Color3f(1.0F, 1.0F, 1.0F));
		sceneGraph.addChild(bk);
		su.addBranchGraph(sceneGraph);

	}

	public SimpleUniverse getSimpleUniverse() {
		return su;
	}

	public void createTrans() {
		Point3f xyzpoint[] = { new Point3f(0.0F, 0.0F, 0.0F),
				new Point3f(10F, 0.0F, 0.0F), new Point3f(0.0F, 0.0F, 0.0F),
				new Point3f(0.0F, 10F, 0.0F), new Point3f(0.0F, 0.0F, 0.0F),
				new Point3f(0.0F, 0.0F, 10F) };

		LineArray axl = new LineArray(xyzpoint.length, 5);
		axl.setCoordinates(0, xyzpoint);
		for (int i = 0; i < 6; i++)
			axl.setColor(i, axes_colors[i / 2]);

		Shape3D line = new Shape3D(axl, new Appearance());
		createTransBranchGroup = new BranchGroup();
		line_branch_group = new BranchGroup();
		line_branch_group.addChild(line);
		createTransBranchGroup.addChild(line_branch_group);
		createTransBranchGroup.setCapability(17);
		trg.addChild(createTransBranchGroup);

		Matrix3d rotY = new Matrix3d(Math.cos(Math.PI / 6), 0, -Math
				.sin(Math.PI / 6), 0, 1, 0, Math.sin(Math.PI / 6), 0, Math
				.cos(Math.PI / 6));
		Matrix3d rotX = new Matrix3d(1, 0, 0, 0, Math.cos(Math.PI / 6), -Math
				.sin(Math.PI / 6), 0, Math.sin(Math.PI / 6), Math
				.cos(Math.PI / 6));
		rotX.mul(rotY);
		Transform3D viewPos = new Transform3D();
		viewPos.setRotation(rotX);
		trg.setTransform(viewPos);
	}

	/**
	 * This method initializes jTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	public JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
		}
		return jTextArea;
	}

	/**
	 * enter a point from text input area with coordinate adjusted by some
	 * parameter.
	 */
	public void plotInstance(Individual individual) {

		Parameter genes[]=(Parameter[]) individual.getGenes();
		 
		
		float xv = (float) genes[jSlider.getValue()].getValor()	* coordinate_parameter;
		float yv = 0;
		
		if (numAttributes > jSlider.getValue() + 1) {
			yv = (float) (genes[jSlider.getValue()].getValor()+1)	* coordinate_parameter;
		}
		float zv = 0;
		//if (numAttributes > jSlider.getValue() + 2) {
			zv = (float) individual.getFuncaoDesempenho().getDesempenho();
		//}

			
		plotInstance(individual,xv, yv, zv);
		
		
	}

	/**
	 * enter a point from text input area with coordinate adjusted by some
	 * parameter.
	 */
	private void plotInstance( Individual individual, float x, float y, float z) {
		byte nowcolor;
		//TODO DECIDE THE COLOR ACCORDING A GRADIENT
		if (individual.isExpert())
		{
			nowcolor=1;
			//canvas3D.setBounds((int)x,(int) y, 100, 100);
			canvas3D.setLocation((int)x,(int) y);
			
			cube_length = 0.010F;
		}
		else
		{
			nowcolor = 2;
		}
		
		
		drawBox(x, y, z, colors[nowcolor]);
		
		cube_length = 0.005F;
	}

	public void plotInstancesFromCombo() {

		if (noPlotFromUIEvent) {
			return;
		}
		if (data != null ) {

			getSimpleUniverse().cleanup();
			createSimpleUniverse();

			if (data != null) {
				for (int i = 0; i < data.size(); ++i) {
					plotInstanceFromCombo((Individual)data.get(i));
				}
			}

			

		}
	}

	public void plotInstanceFromCombo(Individual individual) {

		int x = jComboBoxX.getSelectedIndex();
		int y = jComboBoxY.getSelectedIndex();
		int z = jComboBoxZ.getSelectedIndex();

		Parameter genes[] = (Parameter[])individual.getGenes();
		
		float xv = (float) genes[x].getValor() * coordinate_parameter;
		
		float yv = 0;
		if (jComboBoxY.getSelectedIndex() != -1) {
			yv = (float) genes[y].getValor() * coordinate_parameter;
		}
		float zv = 0;
/*		if (jComboBoxZ.getSelectedIndex() != -1) {
			zv = (float) instance.value(z) * coordinate_parameter;
		}
*/
		zv = (float)individual.getFuncaoDesempenho().getDesempenho();
		
		plotInstance(individual,xv, yv, zv);
	}


	private void drawBox(float x, float y, float z, Color3f color) {
		TransparencyAttributes t_attr = new TransparencyAttributes(
				TransparencyAttributes.FASTEST, 0.3F);
		Appearance app = new Appearance();
		app.setTransparencyAttributes(t_attr);
		Material m = new Material();
		m.setEmissiveColor(color);
		app.setMaterial(m);
		Box box = new Box(cube_length, cube_length, cube_length, app);

		Transform3D t2 = new Transform3D();
		t2.set(new Vector3f(x - cube_length / 2, y - cube_length / 2, z
				- cube_length / 2));
		TransformGroup tran2 = new TransformGroup(t2);
		tran2.addChild(box);
		BranchGroup b = new BranchGroup();
		b.setCapability(17);
		b.addChild(tran2);

		trg.addChild(b);
	}

	/**
	 * This method initializes jSlider
	 * 
	 * @return javax.swing.JSlider
	 */
	private JSlider getJSlider() {
		if (jSlider == null) {
			jSlider = new JSlider();
			jSlider.setValue(0);
			jSlider.setMinorTickSpacing(1);

			jSlider.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					if (noPlotFromUIEvent) {
						return;
					}
					noPlotFromUIEvent = true;
					getSimpleUniverse().cleanup();
					createSimpleUniverse();

					plot(data);
					noPlotFromUIEvent = false;
				}
			});
		}
		return jSlider;
	}

	/**
	 * This method initializes jComboBoxX
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBoxX() {
		if (jComboBoxX == null) {
			jComboBoxX = new JComboBox();

			jComboBoxX.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int selectedIndex = jComboBoxX.getSelectedIndex();
					if (selectedIndex != -1 && selectedIndex != previousX) {
						previousX = selectedIndex;
						plotInstancesFromCombo();
					}
				}
			});

		}
		return jComboBoxX;
	}

	/**
	 * This method initializes jComboBoxY
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBoxY() {
		if (jComboBoxY == null) {
			jComboBoxY = new JComboBox();

			jComboBoxY.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent e) {

					int selectedIndex = jComboBoxY.getSelectedIndex();
					if (selectedIndex != -1 && selectedIndex != previousY) {
						previousY = selectedIndex;
						plotInstancesFromCombo();
					}
				}
			});

		}
		return jComboBoxY;
	}

	/**
	 * This method initializes jComboBoxZ
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBoxZ() {
		if (jComboBoxZ == null) {
			jComboBoxZ = new JComboBox();
			jComboBoxZ.setVisible(false);
			jComboBoxZ.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {

					int selectedIndex = jComboBoxZ.getSelectedIndex();
					if (selectedIndex != -1 && selectedIndex != previousZ) {
						previousZ = selectedIndex;

						plotInstancesFromCombo();
					}
				}
			});

		}
		return jComboBoxZ;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
		}
		return jPanel;
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		
		setLayout(new BorderLayout());

		add(getJPanelButtons(), BorderLayout.SOUTH);
		add(getCanvas3D(), BorderLayout.CENTER);

		
		
		
	}

	
	/**
	 * This method initializes jPanelButtons
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.gridy = 0;
			jLabel3 = new JLabel();
			jLabel3.setText("          ");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.gridy = 0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 6;
			gridBagConstraints6.gridy = 0;
			jLabel2 = new JLabel();
			jLabel2.setText("Z:");
			jLabel2.setVisible(false);
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 4;
			gridBagConstraints5.gridy = 0;
			jLabel1 = new JLabel();
			jLabel1.setText("Y:");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 2;
			gridBagConstraints4.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText("X:");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridx = 7;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.gridx = 5;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.gridx = 3;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.gridx = 8;
			jPanelButtons = new JPanel();
			jPanelButtons.setLayout(new GridBagLayout());
			jPanelButtons.add(getJSlider(), gridBagConstraints);
			jPanelButtons.add(getJComboBoxX(), gridBagConstraints1);
			jPanelButtons.add(getJComboBoxY(), gridBagConstraints2);
			jPanelButtons.add(getJComboBoxZ(), gridBagConstraints3);
			jPanelButtons.add(jLabel, gridBagConstraints4);
			jPanelButtons.add(jLabel1, gridBagConstraints5);
			jPanelButtons.add(jLabel2, gridBagConstraints6);
			jPanelButtons.add(jLabel3, gridBagConstraints9);
		}
		return jPanelButtons;
	}

	private Color3f axes_colors[] = { new Color3f(0.5F, 0.5F, 0F),
			new Color3f(0.0F, 0.5F, 0.5F), new Color3f(0.5F, 0.5F, 0.5F) };
	private JSlider jSlider = null;
	private JComboBox jComboBoxX = null;
	private JComboBox jComboBoxY = null;
	private JComboBox jComboBoxZ = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JPanel jPanel = null;
	private JLabel jLabel3 = null;

	class Point {

		double x;
		double y;
		double z;
		byte value;

		Point(double x, double y, double z, byte value) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.value = value;
		}
	}

	public boolean isStopRequested() {
		return stopRequested;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
