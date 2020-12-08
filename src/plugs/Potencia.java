package plugs;

import logica.Plugin;

public class Potencia implements Plugin{

	private double param1, param2; 
	
	@Override
	public void setParameters(double n1, double n2) {
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
		// TODO Auto-generated method stub
		return false;
	}

}
