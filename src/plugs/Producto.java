package plugs;

import logica.Plugin;

public class Producto implements Plugin {
	
	//contempla dos atributos. No contempla errores.
	private double param1, param2;

	@Override
	public void setParameters(double param1, double param2) {
		this.param1 = param1;
		this.param2 = param2;
	}

	@Override
	public double execute() {
		return this.param1 * this.param2;
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
		String msg = "Computa la multiplicación entre dos números reales.";
		return msg;
	}

}
