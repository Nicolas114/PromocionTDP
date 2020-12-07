package logica;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginApp {

	// the directory where we keep the plugin classes
	private String pluginsDir;
	// a list where we keep an initialized object of each plugin class
	private List<Plugin> plugins;
	private List<String> pluginNames;
	
	private static Logger logger;
	
	public PluginApp () {
		pluginsDir = "/bin/plugs";
		plugins = new ArrayList<>();
		pluginNames = new ArrayList<>();
		
		this.actualizarPlugins();
		
		if (logger == null) {
			logger = Logger.getLogger(PluginApp.class.getName());

			Handler hnd = new ConsoleHandler();
			hnd.setLevel(Level.WARNING);
			logger.addHandler(hnd);

			logger.setLevel(Level.WARNING);
			
			Logger rootLogger = logger.getParent();
			for (Handler h : rootLogger.getHandlers()) {
				h.setLevel(Level.OFF);
			}
		}
	}
	
	public List<String> getPluginsNames(){
		return this.pluginNames;
	}
	
	public List<Plugin> getPlugins() {
		return this.plugins;
	}

	public void actualizarPlugins() {
		File dir = new File(System.getProperty("user.dir") + File.separator + pluginsDir);
		ClassLoader cl = new PluginLoader(dir);
		if (dir.exists() && dir.isDirectory()) {
			// we'll only load classes directly in this directory -
			// no subdirectories, and no classes in packages are recognized
			String[] files = dir.list();
			for (int i=0; i<files.length; i++) {
				try {
					// only consider files ending in ".class"
					if (! files[i].endsWith(".class"))
						continue;

					Class<?> c = cl.loadClass("logica." + files[i].substring(0, files[i].indexOf(".")));
					Class<?>[] intf = c.getInterfaces();
					for (int j=0; j<intf.length; j++) {
						if (intf[j].getName().equals("logica.Plugin")) {
							// the following line assumes that PluginFunction has a no-argument constructor
							Plugin pf = (Plugin) c.getDeclaredConstructor().newInstance();
							if (!this.pluginNames.contains(pf.getPluginName())) {
								plugins.add(pf);
								pluginNames.add(pf.getPluginName());
								continue;
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					System.err.println("File " + files[i] + " does not contain a valid PluginFunction class.");
				}
			}
		}
		
		for (Plugin p : plugins) {
			System.out.println(p.getPluginName());
		}
		System.out.println("---");
	}

	public double runPlugins(double n1, double n2) {
		double result = 0;
		Iterator<?> iter = plugins.iterator();
		while (iter.hasNext()) {
			Plugin p = (Plugin) iter.next();
			try {
				p.setParameters(n1, n2);
				
				System.out.print("Plugin " + p.getPluginName());
				System.out.print("( " + n1 + ", " + n2 + " ): ");
				
				if (p.hasError()) {
					System.out.println("there was an error during plugin initialization");
					continue;
				}
				
				result = p.execute();
				
				if (p.hasError())
					System.out.println("there was an error during plugin execution");
				else
					System.out.println(result);
				
			} catch (SecurityException secEx) {
				System.err.println("plugin '"+p.getClass().getName()+"' tried to do something illegal");
			}
		}
		
		return result;
	}
	
	public double runPlugin(double n1, double n2, String pluginame) {
		double result = 0;
		boolean encontre = false;
		Iterator<?> iter = plugins.iterator();
		while (iter.hasNext()) {
			Plugin p = (Plugin) iter.next();
			
			if (p.getPluginName().equals(pluginame)) {
				encontre = true;
				p.setParameters(n1, n2);
				result = p.execute();
				logger.info("Plugin '" + p.getPluginName() + "' en ejecución.");
				System.out.print("Plugin " + p.getPluginName());
				System.out.print("( " + n1 + ", " + n2 + " ): ");
				System.out.println(result);
				break;
			}
		}
		
		if (!encontre) {
			logger.severe("Plugin '" + pluginame + "' no encontrado.");
		}
		
		return result;
	}
}
