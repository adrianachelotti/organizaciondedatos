import java.util.List;

/**
 * Clase que modela un nodo interno del arbol.
 * 
 */
public class NodoInterno extends Nodo {

	/**
	 * Constructor con parametros.
	 * @param numero: numero de bloque en el archivo donde se ubica el nodo.
	 * @param tamanio: tamanio del bloque del archivo que contiene al nodo.
	 */
	public NodoInterno(int numero,int tamanio){
		super(numero,tamanio);
	}
	
	/**
	 * Constructor con parametros.
	 * @param nivel: nivel del nodo en el arbol.
	 * @param tamanio: tamanio del nodo, representa al tamanio de bloque del archivo
	 * 				   donde se persista.
	 * @param numero: numero de bloque en el archivo donde se persista.
	 * @param nodoComodin: numero de bloque en el archivo del menor hijo izquierdo.
	 * 
	 */
	
	public NodoInterno(int nivel,int tamanio,int numero,int nodoComodin) {
		
		super(nivel,tamanio, numero,nodoComodin);
		
	}
	
	/**
	 * Se hidrata el array de bytes pasado como parametro . 
	 * Los datos se guardan con la siguiente estructura:
	 * 
	 * nivel|espacioLibre|offsetHijoIzq|cantRegistros|tamanioClave|clave|offsetHijoDer
	 * 
	 * @param bloque: array de bytes que representa un bloque del archivo,
	 *                el cual equivale a un nodo interno del arbol.
	 */

	public void hidratar(byte[] bloque){
		
		 int posicionActual = 0;	
		 		 
		 //cargo el nivel del nodo
		 int nivel = Utilidades.byteArrayToInt(bloque, posicionActual);
	     posicionActual+=4;   
	     this.setNivel(nivel);
	     
		 //cargo el espacio libre
		 int espacioLibre = Utilidades.byteArrayToInt(bloque, posicionActual);
	     posicionActual+=4;
		 this.setEspacioLibre(espacioLibre);
		 
		 //cargo el hijo izquierdo del nodo
		 int offsetHijoIzq = Utilidades.byteArrayToInt(bloque, posicionActual);
		 posicionActual+=4;
		 this.setNodoComodin(offsetHijoIzq);
		 
		 //leo la cantidad de registros (clave,nodoHijoDerecho)
		 int cantidadRegistros = Utilidades.byteArrayToInt(bloque, posicionActual);
		 posicionActual+=4;
		
		 while(cantidadRegistros!=0){
			 
			 //leo el tamanio de la clave
			 int tamanioClave = Utilidades.byteArrayToInt(bloque, posicionActual);
			 posicionActual+=4;
			 
			 //cargo la clave para el registro
			 String claveActual = Utilidades.byteArrayToString(bloque, posicionActual, tamanioClave); 
			 posicionActual+=tamanioClave;
			 	 
			 
			 //cargo el hijo derecho del nodo
			 int offsetHijoDer = Utilidades.byteArrayToInt(bloque, posicionActual);
			 posicionActual+=4;
			 
			 //creo el registro con la clave y el offset obtenido
			 Registro registroActual = new Registro(claveActual,offsetHijoDer);
			 
			 //agrego el registro al nodo
			 this.getRegistros().add(registroActual);
			 cantidadRegistros--;
			 
		 }
		 
		 
	
	}

	
	/**
	 * Se serializa el nodo actual en un array de bytes.
	 * Los datos se guardan con la siguiente estructura:
	 * 
	 * nivel|espacioLibre|offsetHijoIzq|cantRegistros|tamanioClave|clave|offsetHijoDer
	 * 
	 * @return array de bytes que representa un bloque del archivo,
	 * el cual equivale a un nodo interno del arbol.
	 */
	
	public byte[] serializar(){
		 	
		byte[] bloque = new byte[this.getTamanio()];
		
		byte[] bloqueInt = new byte[4];
		
		int posicionActual = 0;	
		 
		//cargo el nivel del nodo
		int nivel = this.getNivel();
		bloqueInt = Utilidades.intToByteArray(nivel);
		Utilidades.copyByteArrayInByteArray(bloque,bloqueInt, posicionActual, 4);
		posicionActual+=4;   
	    	     
		 //cargo el espacio libre
		 int espacioLibre = this.getEspacioLibre();
	     bloqueInt = Utilidades.intToByteArray(espacioLibre);
		 Utilidades.copyByteArrayInByteArray(bloque, bloqueInt, posicionActual,4);
	     posicionActual+=4;
		 
		 
		 //cargo el hijo izquierdo del nodo
		 int offsetHijoIzq = this.getNodoComodin();
		 bloqueInt = Utilidades.intToByteArray(offsetHijoIzq);
		 Utilidades.copyByteArrayInByteArray(bloque, bloqueInt, posicionActual,4);
	     posicionActual+=4;
		 
		 
	     //Obtengo la lista de registros
	     List<Registro> registros = this.getRegistros();
	     
		 //cargo la cantidad de registros (clave,nodoHijoDerecho)
		 int cantidadRegistros = registros.size();
		 bloqueInt = Utilidades.intToByteArray(cantidadRegistros);
		 Utilidades.copyByteArrayInByteArray(bloque, bloqueInt, posicionActual,4);
	     posicionActual+=4;
		
		 
	     for (Registro reg: registros){
	    	 
	    	 // obtengo la clave actual
	    	 String claveActual = reg.getClave();
	    	 
	    	 //cargo el tamanio actual de la clave
	    	 int tamanioClave = claveActual.length();
	    	 bloqueInt = Utilidades.intToByteArray(tamanioClave);
			 Utilidades.copyByteArrayInByteArray(bloque, bloqueInt, posicionActual,4);
		     posicionActual+=4;
			
		     //cargo la clave actual
		     byte[] bloqueString = Utilidades.stringToByteArray(claveActual); 
	    	 Utilidades.copyByteArrayInByteArray(bloque, bloqueString, posicionActual, tamanioClave);
	    	 posicionActual+=tamanioClave;
	    	 
	    	 //cargo el hijo derecho del nodo
			 int offsetHijoDer = reg.getOffset();
			 bloqueInt = Utilidades.intToByteArray(offsetHijoDer);
			 Utilidades.copyByteArrayInByteArray(bloque, bloqueInt, posicionActual,4);
		     posicionActual+=4;
	     }
	     	
				
		return bloque;
			
		
	}
	
	/**
	 * Se muestran por consola los atributos del nodo hoja.
	 * 
	 */
	public void mostrarInfoNodo(){
		//nivel|espacioLibre|offsetNodoSig|cantRegistros|tamanioClave|clave|offsetBlqDocumentos
		System.out.println("Numero: " + this.getNumeroNodo());
		System.out.println("Nivel: " + this.getNivel());
		System.out.println("Tamanio: " + this.getTamanio());
		System.out.println("Espacio libre: " + this.getEspacioLibre());
		System.out.println("Nodo siguiente: " + this.getNodoComodin() );
		System.out.print("Registros: [");
		List<Registro> registros = 	this.getRegistros();
		
		for(Registro reg: registros){
			System.out.print("(clave: " + reg.getClave() + " ; offset: " + reg.getOffset() + ") ; ");
		}
		System.out.println("]");
	
	}
	
	
	
}