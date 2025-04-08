package es.unican.ss.common;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Categorias de empleados en la franquicia
 */
@XmlRootElement(name="categoria")
@XmlAccessorType(XmlAccessType.FIELD)
public enum Categoria {
	
	ENCARGADO(2000), VENDEDOR(1500), AUXILIAR(1000);
	
	public final double sueldoBase;

    private Categoria(double sueldoBase) {
        this.sueldoBase= sueldoBase;
    }
    
    public double getSueldoBase() {
    	return sueldoBase;
    }

}
