package plugs;

import logica.Plugin;

public class Potencia implements Plugin{

	//contempla dos atributos. No contempla la existencia de errores.
	private double param1, param2; 
	
	@Override
	public void setParameters(double param1, double param2) {
		this.param1 = param1;
		this.param2 = param2;
	}

	@Override
	public double execute() {
		return Math.pow(param1, param2);
	}

	@Override
	public String getPluginName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public boolean hasError() {
		//no existen errores en esta operacion, por lo cual retornamos siempre false
		return false;
	}

	@Override
	public String getDescription() {
		String msg = "Computa la potenciación entre dos números reales.";
		return msg;
	}

}
