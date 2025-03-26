package es.unican.ss.dao;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import es.unican.ss.common.DataAccessException;
import es.unican.ss.common.Empleado;
import es.unican.ss.common.Tienda;
import es.unican.ss.mappers.EmpleadoMapper;
import es.unican.ss.mappers.TiendaMapper;



/**
 * Implementacion de la capa DAO de acceso a Tiendas. 
 * Utiliza almacenamiento en base de datos H2 en memoria.
 */
public class TiendasDAO implements ITiendasDAO {

	public Tienda crearTienda(Tienda t) throws DataAccessException {
		// Comprobar que no existe ya un empleado con ese dni
		if (tienda(t.getId())!=null)
			return null;
				// Si no existe lo insertamos
		String insertStatement = String.format("insert into Tienda(nombre, calle, localidad, codigoPostal) "
				+ "values ('%s', '%s','%s','%s')",
				t.getNombre(), t.getDireccion().getCalle(), t.getDireccion().getLocalidad(), t.getDireccion().getCodigoPostal());
		try {
			H2ServerConnectionManager.executeSqlStatement(insertStatement);
		} catch (SQLException e1) {
			e1.printStackTrace();
			throw new DataAccessException();
		}
		return tiendaPorNombre(t.getNombre());
	}

	public Tienda tienda(long id) throws DataAccessException {
		Tienda result = null;
		try {
		Connection con = H2ServerConnectionManager.getConnection();
		
			Statement statement = con.createStatement();
			String statementText = "select * from Tienda where id = '" + id + "'";
			ResultSet results = statement.executeQuery(statementText);
			if (results.next())
				result = procesaTienda(con, results);
			statement.close();
		} catch (SQLException e) {
			System.out.println(e);
			throw new DataAccessException();
		}
		return result;
	}

	public Tienda tiendaPorNombre(String nombre) throws DataAccessException {
		Tienda result = null;
		try {
		Connection con = H2ServerConnectionManager.getConnection();
		
			Statement statement = con.createStatement();
			String statementText = "select * from Tienda where nombre = '" + nombre + "'";
		//	System.out.println(statementText);
			ResultSet results = statement.executeQuery(statementText);
			if (results.next())
				result = procesaTienda(con, results);
			statement.close(); 

		} catch (SQLException e) {
			// System.out.println(e);
			throw new DataAccessException();
		}
		return result;
	}

	public List<Tienda> tiendas() throws DataAccessException {
		List<Tienda> tiendas = new ArrayList<>(); 
		try {
		Connection con = H2ServerConnectionManager.getConnection(); 

		
			Statement statement = con.createStatement(); 
			String statementText = "select * from Tienda"; 
			ResultSet results = statement.executeQuery(statementText); 
			// Procesamos cada fila como tienda independiente
			while (results.next()) {
				tiendas.add(procesaTienda(con, results)); 
			}
			statement.close(); 
		} catch (SQLException e) {
			// System.out.println(e);
			throw new DataAccessException();
		}

		return tiendas;
	}

	public Tienda modificarTienda(Tienda nuevo) throws DataAccessException {
		Tienda tienda = null;
		String statementText;
		try {
			if (tienda(nuevo.getId())== null)
				return null;
			Connection con = H2ServerConnectionManager.getConnection();
			statementText = String.format(
					"update Tienda set nombre = '%s', calle = '%s', localidad = '%s', codigoPostal = '%s' "
					+ "where id = %d", nuevo.getNombre(), nuevo.getDireccion().getCalle(), 
					nuevo.getDireccion().getLocalidad(), nuevo.getDireccion().getCodigoPostal(),
					nuevo.getId());
			H2ServerConnectionManager.executeSqlStatement(statementText);
			for(Empleado e: nuevo.getEmpleados()) {
				statementText = String.format(
						"update Empleado set idTienda = '%d' where dni = '%s'", nuevo.getId(),
						e.getDNI());
				H2ServerConnectionManager.executeSqlStatement(statementText);
			}
			
			tienda = tienda(nuevo.getId());
		} catch (SQLException e) {
			throw new DataAccessException();
		}
		return tienda;
	}

	public Tienda eliminarTienda(long id) throws DataAccessException {
		Tienda tienda = null;
		String statementText=null;

		try {
			tienda = tienda(id);
			if (tienda ==null)
				return null;
			Connection con = H2ServerConnectionManager.getConnection();
			for (Empleado e:tienda.getEmpleados()) {
				statementText = "delete from Empleado where dni = '" + e.getDNI() + "'";
				H2ServerConnectionManager.executeSqlStatement(statementText);
			}
			statementText = "delete from Tienda where id = " + id;
			H2ServerConnectionManager.executeSqlStatement(statementText);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException();
		}
		return tienda;
	}

	private Tienda procesaTienda(Connection con, ResultSet results) throws SQLException, DataAccessException {
		Tienda result = null;
		result = TiendaMapper.toTienda(results);
		// Cargamos los empleados de la tienda
		Statement statement = con.createStatement();
		String statementText = String.format("select * from Empleado where idTienda = %d", result.getId());
		results = statement.executeQuery(statementText);
		while (results.next()) {
			result.getEmpleados().add(EmpleadoMapper.toEmpleado(results));
		}
		statement.close();
		return result;
	}

}
