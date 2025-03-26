package es.unican.ss.common;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que representa una tienda de la franquicia.
 * Almacena sus datos y su lista de empleados
 * El nombre de la tienda es unico en la franquicia
 */
@SuppressWarnings("serial")
public class Tienda implements Serializable {
	
	// Primary key en BBDD (autogenerada)
	private long id;
	private String nombre;
	private Direccion direccion;
	private List<Empleado> empleados = new LinkedList<Empleado>();
	
	public Tienda() {}
	/**
	 * Constructor de tienda con nombre y direccion
	 * @param nombre Nombre de la tienda
	 * @param direccion Direccion de la tienda
	 */
	public Tienda(String nombre, Direccion direccion) {
		this.nombre=nombre;
		this.direccion= direccion;
	}

	/**
	 * Retorna la direccion de la tienda
	 * @return Direccion de la tienda
	 */
	public Direccion getDireccion() {
		return direccion;
	}

	/**
	 * Retorna el nombre de la tienda
	 * @return Nombre de la tienda
	 */
	public String getNombre() {
		return nombre;
	}
	
	
	/**
	 * Retorna la lista de Empleadoes actuales de la tienda
	 * @return La lista de empleados
	 */
	public List<Empleado> getEmpleados() {
		return empleados;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	

	/**
	 * Anhade un nuevo Empleado a la tienda
	 * 
	 * @param nuevoEmpleado
	 * @return true si el Empleado se ha anhadido 
	 *         false si ya habia un Empleado con el mismo id
	 */
	public boolean anhadeEmpleado(Empleado nuevoEmpleado) {
		Empleado e = buscaEmpleado(nuevoEmpleado.getDNI());
		if (e != null) {
			return false;
		}
		empleados.add(nuevoEmpleado);
		return true;
	}

	/**
	 * Elimina el Empleado cuyo dni se pasa como parametro
	 * @param id
	 * @return El empleado eliminado 
	 *         null si no existe el empleado
	 */
	public Empleado eliminaEmpleado(String id) {
		Empleado e = buscaEmpleado(id);
		if (e == null) {
			return null;
		}
		empleados.remove(e);
		return e;
	}

	/**
	 * Retorna el Empleado con el id indicado
	 * 
	 * @param id
	 *            Id del Empleado
	 * @return Empleado con ese id o null si no existe ninguno
	 */
	public Empleado buscaEmpleado(String dni) {
		for (Empleado e : empleados) {
			if (e.getDNI().equals(dni)) {
				return e;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "Tienda [id=" + id + ", empleados=" + empleados + ", direccion=" + direccion + ", nombre=" + nombre
				+ "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tienda other = (Tienda) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	
}
