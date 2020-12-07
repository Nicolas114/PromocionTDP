package logica;

public class Division implements Plugin {

	private double param1, param2;
	private boolean hasError;
	
	@Override
	public void setParameters(double param1, double param2) {
		this.param1 = param1;
		this.param2 = param2;
	}

	@Override
	public double execute() {
		this.hasError = false;
		
		if (param2 == 0) {
			this.hasError = true;
			return 0;
		}
		
		return ((double)this.param1 / this.param2);
	}

	@Override
	public String getPluginName() {
		// TODO Auto-generated method stub
		return this.getClass().getSimpleName();
	}

	@Override
	public boolean hasError() {
		// TODO Auto-generated method stub
		return hasError;
	}

}
