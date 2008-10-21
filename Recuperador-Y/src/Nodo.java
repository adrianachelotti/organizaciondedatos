import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Clase abstracta que modela un Nodo.
 * 
 */
public abstract class Nodo  {

	private static final int COLCHON_ESPACIO_LIBRE = 30;
	private static final int TAMANIO_INFO_ADM = 16 + COLCHON_ESPACIO_LIBRE;	
	
	private ArrayList<Registro> registros;
	
	private Boolean overflow;
    
	private int tamanio;
	
	private int nivel;
	
	private int espacioLibre;
	
	private int numeroNodo; 
	
	private int nodoComodin;
	
	
	/**
	 * Constructor con parametros.
	 * @param numero : numero de bloque del nodo en el archivo.
	 * @param tamanio: tamanio del bloque del archivo que contiene al nodo.
	 */
	public Nodo(int numero,int tamanio){
		this.numeroNodo = numero;
		this.tamanio = tamanio;
		this.registros = new ArrayList<Registro>();
	}
	/**
	 * Constructor con parametros.
	 * @param nivel: nivel del nodo en el arbol.
	 * @param tamanio: tamanio del nodo, representa al tamanio de bloque del archivo
	 * 				   donde se persista.
	 * @param numero: numero de bloque en el archivo donde se persista.
	 * @param nodoComodin: numero de bloque en el archivo del nodo comodin.
	 */
	public Nodo(int nivel, int tamanio,int numero,int nodoComodin){
		
		this.nivel=nivel;
		this.numeroNodo=numero;
		this.tamanio=tamanio;
		this.espacioLibre= tamanio - TAMANIO_INFO_ADM;
		this.nodoComodin = nodoComodin;
		this.registros = new ArrayList<Registro>();

		
	}
	
	/**
	 * Devuelve el nivel del nodo en el arbol.
	 * @return nivel del nodo en el arbol.
	 */
	public int getNivel() {
		return nivel;
	}

	/**
	 * Carga el nivel del nodo en el arbol.
	 * @param nivel: nivel del nodo en el arbol.
	 */
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}
	
	/**
	 * Devuelve el numero del nodo.
	 * Este representa la ubicacion relativa del nodo en el archivo donde se
	 * persiste el arbol. Se puede determinar esta posicion realizando el producto
	 * entre este numero de nodo y el tamanio de los nodos.
	 * 
	 * @return numero de nodo.
	 */

	public int getNumeroNodo() {
		return numeroNodo;
	}

	/**
	 * Carga el numero del nodo.
	 * Este representa la ubicacion relativa del nodo en el archivo donde se
	 * persiste el arbol.
	 * @param numeroNodo: numero de nodo a cargar.
	 */
	public void setNumeroNodo(int numeroNodo) {
		this.numeroNodo = numeroNodo;
	}

	/**
	 * Devuelve el espacio libre en el nodo en bytes.
	 * Este espacio libre se calcula tomando el tamanio total del nodo y quitandole
	 * a este el tamanio de la informacion administrativa del nodo (nivel,espacioLibre,
	 * offset y cantidad de registros) y lo ocupado por los registros que posea. 
	 * 
	 * @return Espacio libre en el nodo en bytes.
	 */
	public int getEspacioLibre() {
		  
		return this.espacioLibre;
	}
	
	/**
	 * Se carga el espacio libre del nodo en bytes.
	 * @param libre: espacio libre del nodo en bytes.
	 */
	public void setEspacioLibre(int libre){
		this.espacioLibre = libre;
	}
	
	/**
	 * Muestra por consola las claves del nodo.
	 */
	public void mostrarNodo(){
	   Iterator it =this.registros.iterator();
	   Registro regAux = null;
	   System.out.print("[nivel "+ this.nivel +"|n° "+this.numeroNodo+"|("+this.registros.size()+")|"+this.nodoComodin);
	   while (it.hasNext()){
			regAux=(Registro)it.next();
		    System.out.print(";"+regAux.getClave()+ " ; "+regAux.getOffset());
			
		}
		System.out.println("]");
   }

	/**
	 * Determina si un nodo se encuentra en overflow.
	 * @return true: si existe un desborde en el nodo
	 *		   false: en caso contrario. 
	 */
	public Boolean getOverflow() {
		return overflow;
	}
	
	/**
	 * Se carga el valor de la existencia de un desborde en el nodo.
	 * @param overflow: true si el nodo esta desbordado, false en caso contrario.
	 */

	public void setOverflow(Boolean overflow) {
		this.overflow = overflow;
	}

	/**
	 * Devuelve el tamanio del nodo.
	 * @return tamanio del nodo.
	 */
	public int getTamanio() {
		return this.tamanio;
	}

	/**
	 * Devuelve los registros del nodo.
	 * @return registros que se encuentran en el nodo.
	 */
	public List<Registro> getRegistros() {
		return registros;
	}
	
	/**
	 * Se agrega un registro al nodo.
	 * @param registro: registro a agregar al nodo.
	 */
	public void addRegistro(Registro registro) {
		this.registros.add(registro);
		Collections.sort(this.registros);
		this.espacioLibre -= registro.getTamanio();
	}

	/**
	 * Devuelve el numero de bloque del nodo comodin.
	 * En el caso de ser un nodo interno este numero corresponde al numero de bloque
	 * del menor hijo izquierdo. Para un nodo hoja representa al numero de bloque del nodo
	 * siguiente en la secuencia de nodos hojas.
	 * 
	 * @return numero de bloque del nodo comodin.
	 */
	public int getNodoComodin() {
		return nodoComodin;
	}
	
	/**
	 * Se carga el numero de bloque del nodo comodin.
	 * En el caso de ser un nodo interno este numero corresponde al numero de bloque
	 * del menor hijo izquierdo. Para un nodo hoja representa al numero de bloque del nodo
	 * siguiente en la secuencia de nodos hojas.
	 * 
	 * @param nodoComodin: numero de bloque del nodo comodin.
	 */
	public void setNodoComodin( int nodoComodin) {
		this.nodoComodin = nodoComodin;
	}

	/**
	 * Devuelve el registro que se encuentre en la posicion pasada como parametro.
	 * @param i: numer de registro que se desea obtener.
	 * @return : registro de la i-esima posicion.
	 */
	Registro getRegistro(int i){
		return ((Registro)this.registros.get(i));
	
	}
	
	/**
	 * Se hidrata el array de bytes pasado como parametro . 
	 * Los datos se guardan con la siguiente estructura:
	 * 
	 * nivel|espacioLibre|offset1|cantRegistros|tamanioClave|clave|offset2
	 * 
	 * @param bloque: array de bytes que representa un bloque del archivo,
	 * 				  el cual equivale a un nodo del arbol.
	 */

	public abstract void hidratar(byte[] bloque);
	
	
	
	/**
	 * Se serializa el nodo en un array de bytes.
	 * Este array representa a un bloque del archivo.
	 * Los datos se guardan con la siguiente estructura:
	 * 
	 * nivel|espacioLibre|offset1|cantRegistros|tamanioClave|clave|offset2
	 * 
	 * @return array de bytes que representa un bloque del archivo,
	 *         el cual equivale a un nodo  del arbol.
	 */
	
	public abstract byte[] serializar();
	 
}
