package plugs;

import logica.Plugin;

public class Division implements Plugin {

	//contempla dos atributos y error.
	private double param1, param2;
	private boolean hasError;
	
	@Override
	public void setParameters(double param1, double param2) {
		this.param1 = param1;
		this.param2 = param2;
		this.hasError = false;
	}

	@Override
	public double execute() {
		
		if (param2 == 0) {
			this.hasError = true;
		}
		
		return ((double)this.param1 / this.param2);
	}

	@Override
	public String getPluginName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public boolean hasError() {
		return hasError;
	}

	@Override
	public String getDescription() {
		String msg = "Computa la división entre dos números reales. El divisor no puede valer 0 (cero).";
		return msg;
	}

}
