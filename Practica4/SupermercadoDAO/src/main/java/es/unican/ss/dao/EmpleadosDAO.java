package es.unican.ss.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import es.unican.ss.common.DataAccessException;
import es.unican.ss.common.Empleado;
import es.unican.ss.mappers.EmpleadoMapper;


/**
 * Clase que implementa la capa DAO de acceso a Empleados. Utiliza
 * almacenamiento en base de datos H2 en memoria.
 */
public class EmpleadosDAO implements IEmpleadosDAO {

	public Empleado crearEmpleado(Empleado e) throws DataAccessException {
		String ventas = Double.toString(e.getTotalVentas()).replace(",", ".");
		// Comprobar que no existe ya un empleado con ese dni
		if (empleado(e.getDNI())!=null)
				return null;
		// Si no existe lo insertamos
		String insertStatement = String.format(
				"insert into Empleado(dni, nombre, totalVentas, categoria) values "+
				"('%s', '%s', %s, '%s')",
				e.getDNI(), e.getNombre(), ventas, e.getCategoria().toString(), 0);
		System.out.println(insertStatement);
		try {
			H2ServerConnectionManager.executeSqlStatement(insertStatement);
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new DataAccessException();
		}
		return e;
	}

	public Empleado empleado(String dni) throws DataAccessException {
		Empleado result = null;
		try {
			Connection con = H2ServerConnectionManager.getConnection();

			Statement statement = con.createStatement();
			String statementText = "select * from Empleado where dni = '" + dni + "'";
			ResultSet results = statement.executeQuery(statementText);
			if (results.next()) {
				result = EmpleadoMapper.toEmpleado(results);
			}
			statement.close();
			
		} catch (SQLException e) {
			throw new DataAccessException();
		}
		
		return result;

	}

	public List<Empleado> empleados() throws DataAccessException {
		List<Empleado> empleados = new ArrayList<>(); // Lista para retornar a los alumnos
		try {
			Connection con = H2ServerConnectionManager.getConnection(); // creamos una nueva conexion con la BD
			Statement statement = con.createStatement(); // Creamos un nuevo statement
			String statementText = "select * from Empleado"; // Seleccionamos a todos los alumnos
			ResultSet results = statement.executeQuery(statementText); // Le proporcionamos como parametro al statement
			// el SELECT y lo ejecutamos
			while (results.next()) {
				empleados.add(EmpleadoMapper.toEmpleado(results)); // procesamos cada fila como un autor independiente
			}
			statement.close(); // Cerramos el statement
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException();
		}
		return empleados;
	}

	public Empleado eliminarEmpleado(String dni) throws DataAccessException {
		Empleado empleado = null;

		try {
			Connection con = H2ServerConnectionManager.getConnection();
			empleado = empleado(dni);
			String statementText = "delete from Empleado where dni = '" + dni + "'";
			H2ServerConnectionManager.executeSqlStatement(statementText);
		} catch (SQLException e) {
			throw new DataAccessException();
		}
		return empleado;
	}

	public Empleado modificarEmpleado(Empleado nuevo) throws DataAccessException {
		Empleado empleado= null;
		try {
			if (empleado(nuevo.getDNI())==null)
				return null;
			String ventas = Double.toString(nuevo.getTotalVentas()).replace(",", ".");
			Connection con = H2ServerConnectionManager.getConnection();
			String statementText = String.format(
					"update Empleado set nombre = '%s', categoria = '%s', "
					+ "totalVentas = %s where dni = '%s'", 
					nuevo.getNombre(),
					nuevo.getCategoria().toString(),ventas, nuevo.getDNI());

			H2ServerConnectionManager.executeSqlStatement(statementText);
			empleado = empleado(nuevo.getDNI());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException();
		}
		return empleado;

	}

}
