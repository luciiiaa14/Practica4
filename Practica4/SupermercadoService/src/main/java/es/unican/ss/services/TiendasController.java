package es.unican.ss.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import es.unican.ss.common.Categoria;
import es.unican.ss.common.DataAccessException;
import es.unican.ss.common.Direccion;
import es.unican.ss.common.Empleado;
import es.unican.ss.common.Tienda;
import es.unican.ss.dao.IEmpleadosDAO;
import es.unican.ss.dao.ITiendasDAO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/tiendas")
public class TiendasController {

	//Especificación de las DAO
	private IEmpleadosDAO daoEmpleados;
	private ITiendasDAO daoTiendas;

	//LISTADO DE TIENDAS
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response tiendas (@QueryParam ("localidad")String localidad) throws DataAccessException {
		Response.ResponseBuilder builder = null;

		//Creo una lista con todas las tiendas.
		List<Tienda> tiendas = daoTiendas.tiendas();


		if  (localidad != null) {
			List<Tienda> tiendasValidas = new ArrayList<>();
			for (Tienda t : tiendas) {
				if (t.getDireccion().getLocalidad().equals(localidad)) {
					tiendasValidas.add(t);

				}
			}
			tiendas = tiendasValidas;
		}

		builder = Response.ok(tiendas);
		return builder.build();
	}


	//TIENDA POR NOMBRE
	@Path("/{nombre}")
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response tiendaPorNombre(@PathParam("nombre") String nombre) throws DataAccessException {
		Response.ResponseBuilder builder = null;

		Tienda tienda = daoTiendas.tiendaPorNombre(nombre);
		if (tienda == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		builder = Response.ok(tienda);
		return builder.build();
	}

	//EMPLEADO DE UNA TIENDA. GET
	@Path("/{nombre}/empleados/{dni}")
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response empleadoPorDni (@PathParam("dni") String dni) throws DataAccessException {
		Response.ResponseBuilder builder = null;

		Empleado empleado = daoEmpleados.empleado(dni);
		if (empleado == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		builder = Response.ok(empleado);
		return builder.build();
	}

	//EMPLEADO DE UNA TIENDA. PUT
	@Path("/{nombre}/empleados/{dni}")
	@PUT
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response anhadirEmpleado (@PathParam("nombre") String nombreTienda,
			@PathParam("dni") String dni,
			@Context UriInfo uriInfo, Empleado e) throws DataAccessException {
		Response.ResponseBuilder builder;
		URI uri;
		//Comprobacion del DNI.
		if (daoEmpleados.empleado(dni) != null) {
			return Response.status(Response.Status.CONFLICT).build();
		}

		//Compruebo que el dni del usuario sea igual que el dni del path.
		if (!dni.equals(e.getDNI())) {
			return Response.status(Response.Status.CONFLICT).build();

		}
		Empleado empleado = daoEmpleados.crearEmpleado(e);

		if (empleado == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		Tienda t = daoTiendas.tiendaPorNombre(nombreTienda);
		daoTiendas.tiendaPorNombre(nombreTienda).anhadeEmpleado(empleado);
		daoTiendas.modificarTienda(t);

		uri = uriInfo.getAbsolutePathBuilder().build();
		builder = Response.created(uri);
		return builder.build();

	}

	//EMPLEADO DE UNA TIENDA. DELETE
	@Path("/{nombre}/empleados/{dni}")
	@DELETE
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response eliminarEmpleado (@PathParam("nombre") String nombreTienda,
			@PathParam("dni") String dni,
			@Context UriInfo uriInfo, Empleado e) throws DataAccessException {
		Response.ResponseBuilder builder;
		//Comprobacion del DNI.
		if (daoEmpleados.empleado(dni) == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		//Compruebo que el dni del usuario sea igual que el dni del path.
		if (!dni.equals(e.getDNI())) {
			return Response.status(Response.Status.CONFLICT).build();

		}
		Tienda t = daoTiendas.tiendaPorNombre(nombreTienda);
		daoTiendas.tiendaPorNombre(nombreTienda).eliminaEmpleado(e.getDNI());
		daoTiendas.modificarTienda(t);

		builder = Response.noContent();

		return builder.build();

	}

	//Categoría del empleado
	@Path("/{nombre}/empleados/{dni}/Categoria")
	@PUT
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response categoriaEmpleado(@PathParam("nombre") String nombre,
			@PathParam("dni") String dni, Categoria categoria,
			@Context UriInfo uriInfo) throws DataAccessException {
		Response.ResponseBuilder builder = null;
		URI uri;

		Tienda tienda = daoTiendas.tiendaPorNombre(nombre);
		if (tienda == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		Empleado empleado = daoEmpleados.empleado(dni);
		if (empleado == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		empleado.setCategoria(categoria);
		daoEmpleados.modificarEmpleado(empleado);

		uri = uriInfo.getAbsolutePathBuilder().build();
		builder = Response.created(uri);

		return builder.build();
	}



	//Actualiza ventas mensuales del empleado
	@Path("/{nombre}/empleados/{dni}/ventasMensual")
	@PUT
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response anhadirVentasMensualEmplado(@PathParam("nombre") String nombre,
			@PathParam("dni") String dni, Double ventasMensual,
			@Context UriInfo uriInfo) throws DataAccessException {
		Response.ResponseBuilder builder = null;
		URI uri;

		Tienda tienda = daoTiendas.tiendaPorNombre(nombre);
		if (tienda == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		Empleado empleado = daoEmpleados.empleado(dni);
		if (empleado == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		double totalVentas = empleado.getTotalVentas() + ventasMensual;
		daoEmpleados.empleado(dni).setTotalVentas(totalVentas);
		daoEmpleados.modificarEmpleado(empleado);

		uri = uriInfo.getAbsolutePathBuilder().build();
		builder = Response.created(uri);

		return builder.build();
	}

	//MEJOR EMPLEADO DE UNA TIENDA. GET
	@Path("/empleados/mejorEmpleado")
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response mejorEmpleadoTienda() throws DataAccessException {
		Response.ResponseBuilder builder = null;
		Empleado empleado = null;
		double ventasMejor = 0;

		List<Empleado> empleados = daoEmpleados.empleados();
		if (empleados == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		for (Empleado e : empleados) {
			if (e.getTotalVentas() > ventasMejor) {
				ventasMejor =e.getTotalVentas();
				empleado = e;
			}
			
		}

		builder = Response.ok(empleado);
		return builder.build();
	}
	
	//SUELDO DE UNA TIENDA. GET
	@Path("/{nombre}/empleados/{dni}/sueldo")
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response sueldoEmpleado(@PathParam("nombre") String nombre,
			@PathParam("dni") String dni) throws DataAccessException {
		Response.ResponseBuilder builder = null;
		Empleado empleado = null;

		/*List<Empleado> empleados = daoEmpleados.empleados();
		if (empleados == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		for (Empleado e : empleados) {
			if (e.getTotalVentas() > ventasMejor) {
				ventasMejor =e.getTotalVentas();
				empleado = e;
			}
			
		}*/

		builder = Response.ok(empleado);
		return builder.build();
	}










}
