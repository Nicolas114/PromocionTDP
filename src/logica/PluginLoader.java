package logica;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Clase encargada de la parte de búsqueda, interpretación y carga de plugins en la aplicación.
 * @author Nicolás González
 *
 */
public class PluginLoader extends ClassLoader {
	//directorio desde donde serán cargadas las clases
	private File directorio;
	
	
	/**
	 * Inicializa el PluginLoader con la dirección parametrizada.
	 * @param dir Directorio con el cual trabajará el PluginLoader. 
	 */
	public PluginLoader (File dir) {
		directorio = dir;
	}

	@Override
	public Class<?> loadClass (String name) throws ClassNotFoundException { 
		return loadClass(name, true); 
	}

	@Override
	public Class<?> loadClass (String classname, boolean resolve) throws ClassNotFoundException {
		Class<?> c;
		String filename = "";
		File f;
		int length = 0;
		byte[] classbytes;
		DataInputStream in;
		try {
			//chequea si la clase buscada ya está cargada en el cache
			c = findLoadedClass(classname);

			if (c == null) {
				try {
					c = findSystemClass(classname); 
				}
				catch (Exception | Error ex) {
					//no hacer nada, seguir probando otras opciones
				}

			//si la clase no fue encontrada en las dos instancias anteriores, entonces intentamos
			//cargarla desde un archivo ubicado en el directorio 
			if (c == null) {
				filename = classname.replace('.', File.separatorChar) + ".class";

				f = new File(directorio, filename);

				length = (int) f.length();
				classbytes = new byte[length];
				in = new DataInputStream(new FileInputStream(f));
				in.readFully(classbytes);
				in.close();

				// Convierte los bytes almacenados en una clase
				c = defineClass(classname, classbytes, 0, length);
			}

			if (resolve)
				resolveClass(c);
			}

			return c;
		}
		
		catch (Exception ex) { 
			throw new ClassNotFoundException(ex.toString()); 
		}
	}
		
}
