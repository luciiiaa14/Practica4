package es.unican.ss.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="venta")
@XmlAccessorType(XmlAccessType.FIELD)
public class Venta implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@XmlAttribute(name = "empleado", required = true)
	@JsonProperty("empleado")
	private String dni;
	
	@JsonProperty("importe")
	@XmlAttribute(name = "importe", required = true)
	private Double ventasMensual;

	public Venta(String dni, Double importe) {
		this.dni = dni;
		this.ventasMensual = importe;
	}
	
	public Venta() {}

	public String getDni() {
		return dni;
	}

	public Double getVentasMensual() {
		return ventasMensual;
	}

	
	
	
	
	

	
	

}
