package es.unican.ss.dao;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase que gestiona el acceso a la base de datos H2 en memoria. Permite abrir
 * conexiones y crear y rellenar la propia base de datos al inicio de la
 * aplicacion
 */
public class H2ServerConnectionManager {

	// Conexion con la base de datos
	protected static Connection connection;

	// Atributos de acceso a la Base de Datos
	protected static String dbName = "test";
	protected static String user = "sa";
	protected static String pass = "";

	/**
	 * Obtiene una conexion con la base de datos
	 * 
	 * @return La conexion
	 * @throws DataAccessException si no se puede establecer al conexion
	 */
	public static Connection getConnection() throws SQLException {

		if (connection == null) {
			try {
				Class.forName("org.h2.Driver"); // comprueba que el driver esta instalado
				// Asi se podria poner persistente jdbc:h2:file:C:/data/demodb
				connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
				//connection = DriverManager.getConnection("jdbc:h2:file:C:/Temp/h2/franquicias", "sa", "");
				cargaDatos();
			} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
				throw new SQLException();
			}
		}
		return connection;
	}

	/**
	 * Crea la base de datos e introduce los datos iniciales
	 * 
	 * @throws DataAccessException Si hay un fallo en la conexion
	 */
	public static void cargaDatos() throws SQLException {
		Connection con = getConnection();

		// Creacion programatica de la BBDD
		Statement stm = con.createStatement();

		// Creacion de la tabla Tienda
		String sql = "CREATE TABLE Tienda (id BIGINT NOT NULL AUTO_INCREMENT, nombre VARCHAR(100) NOT NULL, "
				+ "calle VARCHAR(200) NOT NULL, localidad VARCHAR(200) NOT NULL, codigoPostal VARCHAR(10) NOT NULL, PRIMARY KEY(id))";
		stm.execute(sql);

		// Creacion de la tabla Empleado
		sql = "CREATE TABLE Empleado (dni CHAR(9) NOT NULL, nombre VARCHAR(100) NOT NULL, "
				+ "totalVentas FLOAT NOT NULL, categoria VARCHAR(10) NOT NULL, idTienda BIGINT, "
				+ "PRIMARY KEY(dni), FOREIGN KEY(idTienda) REFERENCES Tienda(id))";
		stm.execute(sql);

		// Inicializa tiendas
		sql = "INSERT INTO Tienda (nombre, calle, localidad, codigoPostal) VALUES ('Tienda A', 'Calle A', 'Santander', '39001')";
		stm.executeUpdate(sql);
		sql = "INSERT INTO Tienda (nombre, calle, localidad, codigoPostal) VALUES ('Tienda B', 'Calle B', 'Santander', '39002')";
		stm.executeUpdate(sql);
		sql = "INSERT INTO Tienda (nombre, calle, localidad, codigoPostal) VALUES ('Tienda C', 'Calle C', 'Madrid', '39009')";
		stm.executeUpdate(sql);
		sql = "INSERT INTO Tienda (nombre, calle, localidad, codigoPostal) VALUES ('Tienda D', 'Calle D', 'Santander', '39002')";
		stm.executeUpdate(sql);

		// Inicializa empleados
		sql = "INSERT INTO Empleado (dni, nombre, totalVentas, categoria, idTienda) "
				+ "VALUES ('11111111A', 'Juan Perez', 1000, 'ENCARGADO', 1)";
		stm.executeUpdate(sql);
		sql = "INSERT INTO Empleado (dni, nombre, totalVentas, categoria, idTienda) "
				+ "VALUES ('11111111B', 'Maria Rodriguez', 2000, 'VENDEDOR', 1)";
		stm.executeUpdate(sql);
		sql = "INSERT INTO Empleado (dni, nombre, totalVentas, categoria, idTienda) "
				+ "VALUES ('11111111C', 'Luis Martinez', 3000, 'AUXILIAR', 2)";
		stm.executeUpdate(sql);
		sql = "INSERT INTO Empleado (dni, nombre, totalVentas, categoria, idTienda) "
				+ "VALUES ('11111111D', 'Lucia Ibanez', 2000, 'ENCARGADO', 3)";
		stm.executeUpdate(sql);

		// Cierra el statement
		stm.close();
		
	}

	/**
	 * Metodo estatico para ejecutar operaciones SQL y manejar los errores.
	 * 
	 * IMPORTANTE: este metodo solo puede ser llamado para operaciones de INSERT,
	 * UPDATE y DELETE. No debe usarse para realizar SELECTs ni llamadas a
	 * PROCEDIMIENTO, las ejecuciones de ese tipo de operaciones tienen que
	 * implementarse en sus respectivos metodos.
	 * 
	 * @param stringStatement Instruccion a ejecutar
	 * @throws SQLException si hay un error en la conexion
	 */
	public static void executeSqlStatement(String stringStatement) throws SQLException {
		Connection con = getConnection();
		Statement stm = con.createStatement();
		stm.execute(stringStatement);
		stm.close();
	}
	
	public static void cierraConexion() {
		
	}
}
