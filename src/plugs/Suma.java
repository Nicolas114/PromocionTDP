package plugs;

import logica.Plugin;

public class Suma implements Plugin {
	
	private double param1, param2;

	@Override
	public void setParameters(double param1, double param2) {
		this.param1 = param1;
		this.param2 = param2;
	}

	@Override
	public double execute() {
		return param1 + param2;
	}

	@Override
	public String getPluginName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public boolean hasError() {
		// TODO Auto-generated method stub
		return false;
	}

	
	



}
