package logica;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase encargada de la parte lógica de la aplicación. Tiene como responsabilidad manejar la
 * obtención e instanciación de plugins 
 * @author Nicolás González
 *
 */
public class PluginApp {

	//bandera booleana que registra si el último plugin que corrió tuvo errores.
	private boolean tieneError;
	//directorio de los plugins
	private String pluginsDir;
	//una lista donde guardamos los plugins inicializados
	private List<Plugin> plugins;
	//una lista donde guardar los nombres de los plugins cargados a fin de evitar repeticion
	private List<String> pluginNames;
	//logger utilizado para guardar puntos claves de información
	private static Logger logger;

	/**
	 * Inicializa la aplicación de plugins, indicando el directorio donde serán buscados e inicializando
	 * las colecciones donde serán almacenados.
	 */
	public PluginApp () {
		this.pluginsDir = "plugs"; //directorio utilizado a la hora de generar el .jar
		//		this.pluginsDir = "bin/plugs"; //directorio usado en el entorno de Eclipse
		this.plugins = new ArrayList<>();
		this.pluginNames = new ArrayList<>();
		this.tieneError = false;


		//inicializa y configura el logger
		if (logger == null) {
			logger = Logger.getLogger(PluginApp.class.getName());

			Handler hnd = new ConsoleHandler();
			hnd.setLevel(Level.WARNING);
			logger.addHandler(hnd);

			//filtramos los +WARNINGS (warning y severe)
			logger.setLevel(Level.WARNING);

			Logger rootLogger = logger.getParent();
			for (Handler h : rootLogger.getHandlers()) {
				h.setLevel(Level.OFF);
			}

			//realiza una búsqueda inicial en caso que ya haya plugins ubicados en la carpeta
			this.actualizarPlugins();
		}
	}

	/**
	 * Devuelve una lista con los nombres de los plugins <b>cargados</b>.
	 * @return Lista con los nombres simples de los plugins que están cargados.
	 */
	public List<String> getPluginsNames(){
		List<String> names = new ArrayList<>();
		for (Plugin p: plugins) {
			names.add(p.getPluginName());
		}

		return names;
	}

	/**
	 * Devuelve la lista con los plugins cargados.
	 * @return Lista con los plugins que están cargados.
	 */
	public List<Plugin> getPlugins() {
		return this.plugins;
	}

	/**
	 * Busca por archivos <b>.class</b> en el directorio indicado en la inicialización de la app de plugins.
	 * En caso de encontrar alguno, lo carga y lo agrega en la lista de plugins (y de nombres).
	 * 
	 */
	public void actualizarPlugins() {
		File dir = new File(System.getProperty("user.dir") + File.separator + pluginsDir);
		ClassLoader cl = new PluginLoader(dir);
		String[] archivos;
		Class<?> c;
		Class<?>[] intf;
		Plugin pf;

		if (dir.exists() && dir.isDirectory()) {
			archivos = dir.list();

			for (int i=0; i<archivos.length; i++) {
				try {
					//sólo son considerados aquellos archivos .class
					if (archivos[i].endsWith(".class")) {
						c = cl.loadClass("plugs." + archivos[i].substring(0, archivos[i].indexOf(".")));
						intf = c.getInterfaces();

						for (int j=0; j < intf.length; j++) {
							if (intf[j].getName().equals("logica.Plugin")) {
								//la línea siguiente se encarga de "adicionar" el plugin a la aplicación en general,
								//sólo si no está repetido en la lista de nombres.
								//Por supuesto, podría suceder que coexistan dos plugins sólo iguales en funcionalidad, por ejemplo, 
								//Suma1.class y Suma2.class, pero no está contemplada la tarea de verificar la repetición en este sentido.
								if (!this.pluginNames.contains(c.getSimpleName())) {
									// asume que el constructor de Plugin no requiere de parámetros.
									pf = (Plugin) c.getDeclaredConstructor().newInstance();
									this.pluginNames.add(c.getSimpleName());
									this.plugins.add(pf);
									logger.info("Agregado plugin " + c.getSimpleName());
								}

							}
						}
					}
				} catch (Exception  ex) {
					logger.severe(("El archivo " + archivos[i] + " no es un plugin válido."));
				}
			}
		}
	}

	/**
	 * Ejecuta el plugin indicado por el parámetro <b>pluginame</b>
	 * @param n1 Primer parámetro.
	 * @param n2 Segundo parámetro.
	 * @param pluginame Nombre del plugin que se quiere ejecutar.
	 * @return Cómputo del plugin ejecutado.
	 */
	public double runPlugin(double n1, double n2, String pluginame) {
		double resultado = 0;
		boolean encontre = false;
		Iterator<?> iter = plugins.iterator();
		Plugin p;

		//busca el plugin con nombre equivalente a pluginame parametrizado entre todos los plugins, a fin de ejecutarlo.
		while (iter.hasNext()) {
			p = (Plugin) iter.next();

			if (!encontre && p.getPluginName().equals(pluginame)) {
				encontre = true;
				p.setParameters(n1, n2);

				if (p.hasError()) {
					logger.warning("Hubo un error durante la inicialización del plugin.");
					this.tieneError = true;
				}
				else {
					logger.info("Plugin '" + p.getPluginName() + "' en ejecución.");
					resultado = p.execute();

					if (p.hasError()) {
						logger.warning("Hubo un error durante la ejecución del plugin.");
						this.tieneError = true;
					}
					else {
						System.out.print("Plugin " + p.getPluginName());
						System.out.print("( " + n1 + ", " + n2 + " ): ");
						System.out.println(resultado);
						this.tieneError = false;
					}
				}
			}
		}

		if (!encontre) {
			logger.severe("Plugin '" + pluginame + "' no encontrado.");
		}

		System.out.println("Ultimo plugin con errores: " + this.tieneError);
		return resultado;
	}

	/**
	 * Muestra la descripcion del plugin con nombre parametrizado
	 * @param pluginame Nombre del plugin.
	 * @return Descripcion del plugin.
	 */
	public String mostrarDescripcion(String pluginame) {
		boolean encontre = false;
		Iterator<?> iter = plugins.iterator();
		Plugin p;
		String desc = "Descripción no encontrada.";

		while (iter.hasNext()) {
			p = (Plugin) iter.next();

			if (!encontre && p.getPluginName().equals(pluginame)) {
				encontre = true;
				desc = p.getDescription();
			}
		}

		if (!encontre) {
			logger.severe("Plugin '" + pluginame + "' no encontrado.");
		}

		return desc;
	}

	public boolean tieneError() {
		return tieneError;
	}

	/**
	 * Devuelve el logger de la aplicación.
	 * @return logger de la app.
	 */
	public Logger getLogger() {
		return logger;
	}
}
