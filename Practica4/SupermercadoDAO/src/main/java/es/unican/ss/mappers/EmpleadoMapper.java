package es.unican.ss.mappers;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import es.unican.ss.common.Categoria;
import es.unican.ss.common.DataAccessException;
import es.unican.ss.common.Empleado;



/**
 * Clase de utilidad que mapea filas de la base de datos a objetos 
 * de tipo Empleado
 */
public class EmpleadoMapper {

	/**
	 * Metodo privado de apoyo. Recibe un ResultSet de un empleado
	 * y devuelve un objeto Empleado con los datos del ResultSet
	 * @param results Fila resultado de una consulta en base de datos
	 * @return Empleado
	 */
	public static Empleado toEmpleado(ResultSet results) throws DataAccessException {

		Empleado emp =null;
		try {
			String dni = results.getString("dni");
			String nombre = results.getString("nombre");
			double ventas = results.getDouble("totalVentas");
			Categoria cat = Categoria.valueOf(results.getString("categoria"));
			emp = new Empleado(dni, nombre, cat, ventas);
		}
		catch (SQLException e) {
			throw new DataAccessException();
		}
		return emp;
	}
}
