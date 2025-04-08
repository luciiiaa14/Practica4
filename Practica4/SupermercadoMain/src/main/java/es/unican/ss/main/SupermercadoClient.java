package es.unican.ss.main;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.unican.ss.common.Categoria;
import es.unican.ss.common.Empleado;
import es.unican.ss.common.Tienda;
import es.unican.ss.domain.TiendaVenta;
import es.unican.ss.domain.Venta;
import es.unican.ss.domain.Ventas;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class SupermercadoClient {

	public static void main(String[] args) {
		//Variables
		WebTarget resource;
		Invocation.Builder invocation;
		Response respuesta = null;
		
		//Creamos el cliente
		Client client = ClientBuilder.newClient();
		
		//Creamos el WebTarget.
		WebTarget base = client.target("http://localhost:8080/TiendasREST");
	
		
		
		//Deseralizamos el XML para obtener los datos.
		Ventas ventas = null;
		JAXBContext context = null;
		Unmarshaller unmarshaller;
		File file = new File("C:\\Users\\lucia\\OneDrive\\Escritorio\\SERVICIOS\\Practica4F\\Practica4\\SupermercadoMain\\src\\main\\resources\\es\\unican\\ss\\datos\\ventasMes.xml");
		String nombre = null;
		String dni = null;
		double ventasMensual = 0.0;
		try {
			context = JAXBContext.newInstance(Ventas.class);
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Deseralizamos desde XML.
		try {
			unmarshaller = context.createUnmarshaller();
			
			ventas = (Ventas) unmarshaller.unmarshal(file);
			
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Cambio el importe de los empleados.
		for (TiendaVenta t : ventas.getTiendas()) {
			for (Venta v : t.getVentas()) {
				nombre = t.getIdTienda();
				dni = v.getDni();
				ventasMensual = v.getVentasMensual();
				resource = base.path("/tiendas/" + nombre + "/empleados/" + dni + "/ventasMensual");
				invocation = resource.request(MediaType.APPLICATION_JSON);
				respuesta = invocation.put(Entity.json(ventasMensual));

				procesaRespuesta(respuesta);
				
			}
		}
		
		
		//Cerramos el cliente.
		client.close();
		
	}

	public static void procesaRespuesta (Response response) {
		if (response.getStatus() == 200) {
			Empleado emp = response.readEntity(Empleado.class);
			System.out.println(emp.toString());
		} else if (response.getStatus() == 404) {
			System.out.println("El empleado no existe");
		} else {
			System.out.println(response.getStatus());
		}
	}
}
