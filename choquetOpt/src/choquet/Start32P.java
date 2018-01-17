package choquet;

import javax.swing.SwingUtilities;

import ui.JFrameTSP;

public class Start32P {

	public static void main(String[] args) {

		int region = 3;
		Engine.setRegion(region);
		Engine.NUMBER_OF_PARAMETERS=30;

		ChoquetRunner.path = "/home/home/BECKMANN/ChoquetFusionOptimization/32parameters5att/";
	
		final Engine ecosystem = new Engine();
		int i = 0;
		// for (int i = 1; i <= 30; i++) {

		System.out.println("#### " + i + " ---------------- R"
				+ Engine.getRegion());

		Parameter[] genes = new Parameter[Engine.NUMBER_OF_PARAMETERS];
		if (region == 1) {

			genes[0] = new Parameter(0.6699292806200695285);
			genes[1] = new Parameter(0.76838034299098967317);
			genes[2] = new Parameter(0.91798928723601613822);
			genes[3] = new Parameter(0.85046717744952871065);
			genes[4] = new Parameter(0.93296865617793134362);
			genes[5] = new Parameter(0.91551660802692691465);
			genes[6] = new Parameter(0.97330476251097375329);
			genes[7] = new Parameter(0.79596575874772468318);
			genes[8] = new Parameter(0.89697574101765686905);
			genes[9] = new Parameter(0.88142304382084535330);
			genes[10] = new Parameter(0.95260863364718229018);
			genes[11] = new Parameter(0.89843457042751151409);
			genes[12] = new Parameter(0.95144858394452813677);
			genes[13] = new Parameter(0.93015113216633016346);
			genes[14] = new Parameter(0.97801262222681795144);
			genes[15] = new Parameter(0.76010664367979596800);
			genes[16] = new Parameter(0.79013708412704009021);
			genes[17] = new Parameter(0.94016247366285043974);
			genes[18] = new Parameter(0.95912343365626262504);
			genes[19] = new Parameter(0.94895572800856298201);
			genes[20] = new Parameter(0.96031312590016459918);
			genes[21] = new Parameter(0.98578528326310888019);
			genes[22] = new Parameter(0.99596045407367506108);
			genes[23] = new Parameter(0.91464057777500895607);
			genes[24] = new Parameter(0.93323407181995376991);
			genes[25] = new Parameter(0.96955060558079286803);
			genes[26] = new Parameter(0.98325832291927461881);
			genes[27] = new Parameter(0.96321173051355113426);
			genes[28] = new Parameter(0.97470565115425056302);
			genes[29] = new Parameter(0.99015971105263178575);

		} else if (region == 2) {
			genes[0] = new Parameter(0.57606006039091883775);
			genes[1] = new Parameter(0.20207440996271097688);
			genes[2] = new Parameter(0.84328277348045688200);
			genes[3] = new Parameter(0.13757280330454582651);
			genes[4] = new Parameter(0.84584339001346875442);
			genes[5] = new Parameter(0.22376257111970607183);
			genes[6] = new Parameter(0.89320165669089324201);
			genes[7] = new Parameter(0.80719657991367332883);
			genes[8] = new Parameter(0.92174857175447511004);
			genes[9] = new Parameter(0.83237126638148639657);
			genes[10] = new Parameter(0.94940171616156998624);
			genes[11] = new Parameter(0.82524206460087878856);
			genes[12] = new Parameter(0.94976656860480335176);
			genes[13] = new Parameter(0.84233421238569916589);
			genes[14] = new Parameter(0.96579335283633305220);
			genes[15] = new Parameter(0.74218322024842509066);
			genes[16] = new Parameter(0.76773407815423899692);
			genes[17] = new Parameter(0.89694092047382512067);
			genes[18] = new Parameter(0.92688653302487689079);
			genes[19] = new Parameter(0.89393868096073614282);
			genes[20] = new Parameter(0.92366869344944324549);
			genes[21] = new Parameter(0.92623996370157590796);
			genes[22] = new Parameter(0.95840109019760832965);
			genes[23] = new Parameter(0.94827297533106535976);
			genes[24] = new Parameter(0.96230209433876512382);
			genes[25] = new Parameter(0.97407646558747706944);
			genes[26] = new Parameter(0.98818135556391550889);
			genes[27] = new Parameter(0.97016179009563907609);
			genes[28] = new Parameter(0.98533857031993876685);
			genes[29] = new Parameter(0.98519728819850538315);

		} else if (region == 3) {
			genes[0] = new Parameter(0.86904815490949283152);
			genes[1] = new Parameter(0.25670233988621288246);
			genes[2] = new Parameter(0.90546462480270972772);
			genes[3] = new Parameter(0.22014281544104424837);
			genes[4] = new Parameter(0.89660294040170795959);
			genes[5] = new Parameter(0.30399901810498147769);
			genes[6] = new Parameter(0.91519957585429312363);
			genes[7] = new Parameter(0.77614892753450992480);
			genes[8] = new Parameter(0.95185200534704839104);
			genes[9] = new Parameter(0.81826557463520488422);
			genes[10] = new Parameter(0.96829837603835389181);
			genes[11] = new Parameter(0.82096980763438864859);
			genes[12] = new Parameter(0.96703433019228823841);
			genes[13] = new Parameter(0.83546331739658041826);
			genes[14] = new Parameter(0.97467665572940920260);
			genes[15] = new Parameter(0.90718142803735435376);
			genes[16] = new Parameter(0.94030528643915489706);
			genes[17] = new Parameter(0.92776988636632096163);
			genes[18] = new Parameter(0.96103904278419205465);
			genes[19] = new Parameter(0.92491651937620078527);
			genes[20] = new Parameter(0.95422948681227892642);
			genes[21] = new Parameter(0.93645224107987867779);
			genes[22] = new Parameter(0.96707921539093599783);
			genes[23] = new Parameter(0.96288365392661146025);
			genes[24] = new Parameter(0.98252988306797361950);
			genes[25] = new Parameter(0.97819277191781528469);
			genes[26] = new Parameter(0.99543631339162386151);
			genes[27] = new Parameter(0.97724068821577592114);
			genes[28] = new Parameter(0.99294779255228560189);
			genes[29] = new Parameter(0.98445659939658503035);

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
		ecosystem.setExpertNeighborhood(0.10);
		ecosystem.setNeighborhoodRelaxationLimit(0.0);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrameTSP frame = new JFrameTSP(ecosystem);

			}
		});

		// }

	}

}
