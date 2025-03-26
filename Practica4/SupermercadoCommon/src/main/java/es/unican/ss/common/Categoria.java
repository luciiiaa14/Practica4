package es.unican.ss.common;


/**
 * Categorias de empleados en la franquicia
 */
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
