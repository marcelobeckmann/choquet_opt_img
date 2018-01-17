package choquet;

import br.ufrj.coppe.pec.ga.Gene;

public class Parameter extends Gene {

	private double penalty;

	public Parameter(double valorReal) {
		super(valorReal);

		// TODO Auto-generated constructor stub
	}

	public double getPenalty() {
		return penalty;
	}

	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}


}
