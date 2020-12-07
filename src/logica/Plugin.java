package logica;

public interface Plugin {

	// let the application pass in a parameter
	public void setParameters (double n1, double n2);

	// retrieve a result from the plugin
	public double execute();

	// return the name of this plugin
	public String getPluginName();

	// can be called to determine whether the plugin
	// aborted execution due to an error condition
	public boolean hasError();
}
