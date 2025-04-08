package es.unican.ss.domain;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import es.unican.ss.common.Tienda;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="ventas")
public class Ventas implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@XmlElement(name="tienda", required = true)
	@JsonProperty("tienda")
	private List<TiendaVenta> tiendas = new LinkedList<TiendaVenta>();

	public Ventas() {}

	public List<TiendaVenta> getTiendas() {
		return tiendas;
	}

}
