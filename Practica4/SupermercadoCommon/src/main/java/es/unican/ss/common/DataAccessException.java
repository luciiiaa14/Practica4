package es.unican.ss.common;

public class DataAccessException extends Exception {
	private static final long serialVersionUID = 1L;

    /**
     * Constructor que permite especificar un mensaje de error.
     *
     * @param message Mensaje descriptivo del error.
     */
    public DataAccessException() {
    	super();
    }
    
    /**
     * Constructor que permite especificar un mensaje de error.
     *
     * @param message Mensaje descriptivo del error.
     */
    public DataAccessException (String menssage) {
    	super(menssage);
    }
}
