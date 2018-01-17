package choquet;

import javax.swing.SwingUtilities;

import ui.JFrameTSP;

public class Start {

	public static void main(String[] args) {

		int region = 1;
		Engine.setRegion(region);
		final Engine ecosystem = new Engine();
		int i = 0;

		Engine.NUMBER_OF_PARAMETERS = 6;

		ChoquetRunner.path = "/home/home/BECKMANN/ChoquetFusionOptimization/";

		System.out.println("#### " + i + " ---------------- R"
				+ Engine.getRegion());

		Parameter[] genes = new Parameter[Engine.NUMBER_OF_PARAMETERS];
		if (region == 1) {

			genes[0] = new Parameter(0.7203);
			genes[1] = new Parameter(0.83601);
			genes[2] = new Parameter(0.94311066);
			genes[3] = new Parameter(0.015834);
			genes[4] = new Parameter(0.769082);
			genes[5] = new Parameter(0.8916049);

		} else if (region == 2) {
			genes[0] = new Parameter(0.64430771347687831874);
			genes[1] = new Parameter(0.21105844124637174186);
			genes[2] = new Parameter(0.94338456100739376264);
			genes[3] = new Parameter(0.03733155573839298802);
			genes[4] = new Parameter(0.71584565615499451408);
			genes[5] = new Parameter(0.27525383233061873067);

		} else if (region == 3) {
			genes[0] = new Parameter(0.91499164456272352730);
			genes[1] = new Parameter(0.28059083429149384926);
			genes[2] = new Parameter(0.96523023725494005998);
			genes[3] = new Parameter(0.03584001219432794705);
			genes[4] = new Parameter(0.95662401468655444692);
			genes[5] = new Parameter(0.34922282346121208407);

		}
		ecosystem.setExpertParameters(genes);
		ecosystem.setDiscardPenalty(true);
		ecosystem.setPopulacaoInicial(40);
		ecosystem.setPopulacaoMaxima(20);
		ecosystem.setPercentualMutacao(5);
		ecosystem.setPercentualCruzamento(60);
		ecosystem.setMaximoGeracoes(5);
		ecosystem.setDebug(true);
		ecosystem.setPrecision(6);
		ecosystem.setExpertNeighborhood(0.20);
		ecosystem.setNeighborhoodRelaxationLimit(0.0);
		// Thread t = new Thread(ecosystem);

		// t.run();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrameTSP frame = new JFrameTSP(ecosystem);

			}
		});

		// }

	}

}
