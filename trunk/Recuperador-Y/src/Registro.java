import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Clase que modela un registro.
 * Un registro esta conformado por una clave(termino) y un offset.
 * Para un nodo hoja el offset representa al numero de bloque de un
 * archivo donde se encuentra la lista de documentos para este termino.
 * En el caso de un nodo interno representa al hijo derecho que delimita esa clave.
 */
public class Registro implements Comparable<Registro>{
	
	private String clave;
	
	private int offset;
	
	private List<Integer> documentos;

	
	/**
	 * Constructor con parametros.
	 * @param clave: clave(termino) del registro.
	 * @param off: offset que representa al hijo derecho en un nodo interno, o al bloque
	 * donde se encuentra la lista de documentos para esa clave en un nodo hoja.
	 */
	public Registro(String clave,int off){
		this.clave=clave;
		this.offset=off;
		this.documentos= new ArrayList<Integer>();
	}
	
	/**
	 * Devuelve la cantidad de bytes que ocupa el registro.
	 * Se considera que la cantidad de bytes que ocupa el registro esta conformada
	 * por el tamanio de la clave(cantidad de bytes), un campo de control que
	 * contiene la cantidad de caracteres de la clave(4 bytes por ser del tipo int)
	 * y la longitud en bytes del atributo  offset (4 bytes). 
	 * @return tamanio del registo.
	 */
	public int getTamanio() { 
		
		return (clave.length()+8);
	}

	/**
	 * Devuelve la cantidad de bytes que ocupa el registro con los documentos.
	 * Se considera que la cantidad de bytes que ocupa el registro con los documentos
	 * esta conformada por el tamanio de la clave(cantidad de bytes), un campo de control que
	 * contiene la cantidad de caracteres de la clave(4 bytes por ser del tipo int), un campo
	 * de control que posee la cantidad de documentos (4 bytes por ser int), otro campo de control que posee la 
	 * cantidad de bytes (4 bytes por ser int )para la codificacion y el array de bytes para la lista codificada.
	 * a cada documento (4bytes * cantidadDeDocumentos).
	 * @return tamanio del registro con documentos.
	 */
	public int getTamanioConDocumentos(){
		if (this.documentos == null) return 0;
		int cantidadDeDocumentos = this.documentos.size();
		int tamanioClave = this.clave.length();
		CodigoGamma codigo = new CodigoGamma(cantidadDeDocumentos);
		codigo.codeCodigoGamma(this.documentos);
		byte dato[] = codigo.copyCodigoGammaToArrayByte();
		// tamClave + clave + tamCantDocumentos + 
		return (4 + tamanioClave + 4 +  4 +(dato.length));
	}
	
//	public int getTamanioConDocumentos1(){
//		if (this.documentos == null) return 0;
//		int cantidadDeDocumentos = this.documentos.size();
//		int tamanioClave = this.clave.length();
//		
//		return (8 + tamanioClave + (cantidadDeDocumentos*4));
//	}
//	
	
	
	
	
	/**
	 * Devuelve la clave del registtro.
	 * @return clave del registro.
	 */
	public String getClave() {
		return clave;
	}

	/**
	 * Setea la clave del registro.
	 * @param clave: clave del registro.
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}
	
	/**
	 * Devuelve el offset del registro.
	 * El offset representa el hijo derecho que delimita a esa clave en el caso de que
	 * el registro corresponda a un nodo interno. Si pertenece a un nodo hoja representa
	 * al bloque del archivo donde se encuentra la lista de documentos para esa clave.
	 * @return offset del registro.
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Cargo el offset para el registro.
	 * @param offset: representa el hijo derecho que delimita a esa clave en el caso de que
	 * el registro corresponda a un nodo interno. Si pertenece a un nodo hoja representa
	 * al bloque del archivo donde se encuentra la lista de documentos para esa clave.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	/**
	 * Reimplementacion del metodo clone().
	 */
	public Object clone(){
		return new Registro(this.clave,this.offset); 
	}
	
    /**
     * Reimplementacion del metodo compareTo().
     * Necesario por implementar la interfaz Comparable.
     */
	public int compareTo(Registro registro) {
		return this.clave.compareTo(registro.getClave());
	}

	/**
	 * Retorna los documentos donde se encuentra la clave.
	 * @return Documentos donde se encuentra la clave.
	 */
	public List<Integer> getDocumentos() {
		return documentos;
	}

	/**
	 * Setea los documentos donde se encuentra la clave.
	 * @param documentos: documentos en donde se encuentra la clave.
	 */
	public void setDocumentos(List<Integer> documentos) {
		this.documentos = documentos;
	}
	
	
	/**
	 * 
	 * @param numero
	 * @return
	 */
	
	private boolean estaEnLaLista(Integer numero){
		Iterator it = this.documentos.iterator();
		boolean encontrado= false;
		while (it.hasNext()&&(!encontrado)){
			Integer numAux = (Integer) it.next();
			if (numAux.compareTo(numero)==0){
				encontrado = true;
			}
				
		}
		
		return encontrado;
	
		
	}
	
	/**
	 * Agrega los documentos que pasados como parametro.
	 * En caso de que el documento ya se encuentre no se lo repite.
	 * @param docAAgregar: documentos a agregar. 
	 */
	public void agregarDocumentos(List docAAgregar) {
		Iterator it2 = docAAgregar.iterator();
		
		while (it2.hasNext()){
			Integer num =(Integer) it2.next();
			if (!this.estaEnLaLista(num)){
				this.documentos.add(num);
			}
		}
		
		
		
	}
    public String toString() {
        int i=0;
        String listaDocu ="";
        int numero = this.documentos.size();
        while (i < numero) {
             listaDocu = listaDocu +" "+ this.documentos.get(i)+" ";
             i++;
        }
        return (this.getClave() + " (" + listaDocu + ")");
     }
	
	
	

}
