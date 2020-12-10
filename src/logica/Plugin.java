package logica;

/**
 * Clase que modela una unidad de funcionalidad adicional (plugin).
 * @author Nicolás González
 *
 */
public interface Plugin {
	
	/**
	 * Permite setear los atributos que contenga el plugin (solo contempla la existencia de hasta 2 atributos).
	 * No es posible simular operaciones +ternarias, pero sí unarias.
	 * @param n1 Primer atributo del plugin.
	 * @param n2 Segundo atributo del plugin.
	 */
	public void setParameters (double n1, double n2);

	/**
	 * Computa la operación indicada por el plugin.
	 * @return El resultado de computar la operación indicada por el plugin.
	 */
	public double execute();

	/**
	 * Devuelve el nombre del plugin.
	 * @return Nombre del plugin.
	 */
	public String getPluginName();

	/**
	 * Método que puede ser invocado en aquellos plugins que contemplen errores (por ejemplo, la división por cero)
	 * y así determinar si se abortó su ejecución debido a ese error.
	 * @return true si la ejecución del plugin se detuvo por un error, false en caso contrario.
	 */
	public boolean hasError();
	
	/**
	 * Devuelve una descripción del plugin.
	 * @return descripción del plugin.
	 */
	public String getDescription();
}
