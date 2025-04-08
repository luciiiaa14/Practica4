package es.unican.ss.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="tienda")
@XmlAccessorType(XmlAccessType.FIELD)
public class TiendaVenta implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@XmlAttribute(required=true)
	@JsonProperty("tienda")
	String idTienda;
	
	@XmlElement(name = "venta")
	@JsonProperty("venta")
	List<Venta> ventas = new LinkedList<Venta>();

	public TiendaVenta(String idTienda, List<Venta> ventas) {
		super();
		this.idTienda = idTienda;
		this.ventas = ventas;
	}

	public TiendaVenta() {}

	public String getIdTienda() {
		return idTienda;
	}


	public List<Venta> getVentas() {
		return ventas;
	}
	
	
	
	
}
