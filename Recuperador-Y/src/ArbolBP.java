
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Clase que modela un arbol B+.
 * 
 */

public class ArbolBP {

	private RandomAccessFile archivo;
	
	private int tamanioNodo;
	
	private int cantidadDeNodos;
	
	private int altura;	
	
	private Nodo raiz;
	
	
	private ArchivoSecuencialDocumentos archivoSecuencial;
		

	/**
	 * Constructor con parametros.
	 * @param raiz: nodo raiz del arbol.
	 */
	public ArbolBP(Nodo raiz,int tamanioNodo,String nombreArchivo){
		AcumuladorDeBytesTransferidos acumulador = AcumuladorDeBytesTransferidos.obtenerInstancia();
		Registro registro =null;
		this.tamanioNodo = tamanioNodo;
	
		this.archivo=null;
		try {
			this.archivo = new RandomAccessFile(nombreArchivo,"rw");
			byte bloqueHeader[] = new byte[tamanioNodo];
			
			if (this.archivo.length()==0)
			{
				if (raiz!=null){
					registro=raiz.getRegistro(0); //TODO ver el tamanio del bloque
					this.archivoSecuencial = new ArchivoSecuencialDocumentos(new String("Secuencial"+nombreArchivo),this.tamanioNodo);
					int offset =this.archivoSecuencial.darAltaRegistro(registro);
					registro.setOffset(offset);
				}
                //cargo en el bloque header el tamaño del nodo
				byte bloqueInt[]=Utilidades.intToByteArray(tamanioNodo);
				int posicionActual =0;
				Utilidades.copyByteArrayInByteArray(bloqueHeader, bloqueInt, posicionActual,4);
				posicionActual+= 4;
				// cargo en el bloque header la altura inicial del arbol
				this.altura=0;
				bloqueInt= Utilidades.intToByteArray(0);
				Utilidades.copyByteArrayInByteArray(bloqueHeader, bloqueInt, posicionActual,4);
				posicionActual+= 4;
				// cargo en el bloque header la cantidad de nodos, inicialmente 1 
				this.cantidadDeNodos=1;
				bloqueInt= Utilidades.intToByteArray(1);
				Utilidades.copyByteArrayInByteArray(bloqueHeader, bloqueInt, posicionActual,4);
				posicionActual+= 4;
				this.archivo.seek(0);
				this.archivo.write(bloqueHeader);
				
				acumulador.acumular(this.tamanioNodo);
				this.archivo.seek(tamanioNodo);
				
				this.raiz=raiz;
				
				this.archivo.write(this.raiz.serializar());
						
			}
			else
			{	
				this.archivoSecuencial = new ArchivoSecuencialDocumentos(new String("Secuencial"+nombreArchivo),this.tamanioNodo);
				this.archivo.seek(0);
				this.archivo.read(bloqueHeader);
				acumulador.acumular(this.tamanioNodo);
				int posicionActual =4;
				//obtengo la altura del arbol
				this.altura = Utilidades.byteArrayToInt(bloqueHeader,posicionActual);
				posicionActual+= 4;
				// obtengo la cantidad de nodos
				this.cantidadDeNodos = Utilidades.byteArrayToInt(bloqueHeader,posicionActual);
				
				// leo e hidrato la raiz
				this.archivo.seek(tamanioNodo);
				this.archivo.read(bloqueHeader);
				acumulador.acumular(this.tamanioNodo);
				if (this.altura==0){
					this.raiz = new NodoHoja(0, tamanioNodo);
				}else{
					this.raiz= new NodoInterno(0,tamanioNodo);
				}
				this.raiz.hidratar(bloqueHeader);
								
			}
			
	
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Devuelvel el nodo raiz del arbol.
	 * @return nodo raiz del arbol.
	 */
	public Nodo getRaiz() {
		return raiz;
	}

	/**
	 * Carga el nodo raiz del arbol.
	 * @param raiz: nodo raiz del arbol.
	 */
	public void setRaiz(Nodo raiz) {
		this.raiz = raiz;
	}
	
	/**
	 * Devuelve la cantidad de nodos del arbol.
	 * @return cantidad de nodos del arbol.
	 */
	
	public int getCantidadDeNodos(){
		// lo deberia tener siempre en el header
		return this.cantidadDeNodos;
	}

	/**
	 * Devuelve el i-esimo nodo del arbol.
	 * @param i: numero de nodo a devolver.
	 * @return
	 */
	public  Nodo getNodo(int i) {
		//this.actualizarHeader();
		AcumuladorDeBytesTransferidos acumulador = AcumuladorDeBytesTransferidos.obtenerInstancia();
		byte datoNodo[] = new byte[this.tamanioNodo];		
		int posicion = (i+1)*this.tamanioNodo;
	
		try {
			
			this.archivo.seek(posicion);
			this.archivo.read(datoNodo);
			acumulador.acumular(this.tamanioNodo);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int nivel = Utilidades.byteArrayToInt(datoNodo,0);
		
		if (nivel==0){
			NodoHoja nodo  = new NodoHoja(i, tamanioNodo);
			nodo.hidratar(datoNodo);
			return nodo;
					
		}else{
			NodoInterno nodo= new NodoInterno(i,tamanioNodo);
			nodo.hidratar(datoNodo);
			return nodo;
		}
		

	}

	
	/**
	 * Actualiza el header
	 *
	 */
	public void actualizarHeader(){
		
		AcumuladorDeBytesTransferidos acumulador  = AcumuladorDeBytesTransferidos.obtenerInstancia();
		byte bloqueHeader[] = new byte[tamanioNodo];
		// cargo en el bloque header el tamanio del nodo
		byte bloqueInt[]=Utilidades.intToByteArray(tamanioNodo);
		int posicionActual =0;
		Utilidades.copyByteArrayInByteArray( bloqueHeader, bloqueInt,posicionActual,4);
		posicionActual+= 4;
		// cargo en el bloque header la altura inicial del arbol
		bloqueInt= Utilidades.intToByteArray(this.altura);
		Utilidades.copyByteArrayInByteArray( bloqueHeader, bloqueInt, posicionActual,4);
		posicionActual+= 4;
		// cargo en el bloque header la cantidad de nodos, inicialmente 1 
		bloqueInt= Utilidades.intToByteArray(this.cantidadDeNodos);
		Utilidades.copyByteArrayInByteArray( bloqueHeader, bloqueInt,posicionActual,4);
		posicionActual+= 4;
		
		try {
			//antes de cerrar el archivo actualizo el header por las dudas
			this.archivo.seek(0);
			this.archivo.write(bloqueHeader);
			acumulador.acumular(this.tamanioNodo);
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Se agrega un nodo en el arbol.
	 * @param nodo: nodo a agregar en el arbol.
	 * @param o : numero de nodo.
	 */
	public void addNodo(Nodo nodo,int numNodo) {
		int posicion = (numNodo+1)*this.tamanioNodo;
		if (numNodo>=this.cantidadDeNodos)
		{
			this.cantidadDeNodos++;
			
		}
		if (numNodo==0) {
			this.altura= nodo.getNivel();
		}
		try {
			
			this.archivo.seek(posicion);
			this.archivo.write(nodo.serializar());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		this.actualizarHeader();	
		
		
		
	}


	/**
	 * Inserta el registro pasado como parametro en el arbol.
	 * @param registro: registro a ser insertado en el arbol.
	 */
	public void insertar(Registro registro){
		int estado = this.insertarRegistro(this.raiz, registro,null);
		if ((estado==1)&&(this.raiz.getClass()==NodoInterno.class))
		{
			resolverOverflow(this.raiz, null,null);
		}
	}
	
	/**
	 * Se fija si se encuentra la clave en el nodo hoja
	 * @param clave: clave a buscar
	 * @param nodo: nodo hoja donde se busca la clave
	 * @return true/false si la clave ya se encuentra o no en la hoja
	 * 		  
	 */
	
	public boolean estaClaveEnHoja(String clave, NodoHoja nodo){
		List regs = nodo.getRegistros();
		Iterator it = regs.iterator();
		boolean encontrado = false;
		while ((it.hasNext())&&(!encontrado)){
			Registro regAux = (Registro)it.next();
			if ((regAux.getClave()).compareTo(clave)==0){
				encontrado=true;
			}
						
		}
		return encontrado;
	}

	/**
	 * Devuelve el offset asociado a la clave 
	 * @param clave: clave a buscar
	 * @param nodo: nodo hoja donde se busca la clave
	 * @return offset en el archivo de documentos, 0 si no se encuentra la clave
	 * 		  
	 */
	
	public int offsetClaveEnHoja(String clave, NodoHoja nodo){
		List regs = nodo.getRegistros();
		Iterator it = regs.iterator();
		Registro regAux = null;
		boolean encontrado = false;
		while ((it.hasNext())&&(!encontrado)){
			regAux= (Registro)it.next();
			if ((regAux.getClave()).compareTo(clave)==0){
				encontrado=true;
			}
						
		}
		if (encontrado) return regAux.getOffset();
		return 0;
	}

	
	
	/**
	 * Inserta un registro en el arbol. 
	 * Para ello busca el nodo hoja donde insertar la clave llamandose recursivamente. 
	 * Esto se logra tomando distintas porsiones del arbol.
	 *  
	 * 
	 * 
	 * @param raizArbol: raiz del arbol donde quiero insertar el registro. 
	 * @param registro: registro a insertar en el arbol.
	 * @param padre: numero de nodo del padre del arbol donde se inserta el registro. En caso
	 * 				 de que la raiz del arbol sea la del arbol completo el valor es cero.	
	 * @return 1: en caso de que exista un desborde al llegar a la raiz del arbol completo.
	 * 		   0: en caso contrario.	
	 */
	private int insertarRegistro(Nodo raizArbol,Registro registro, Nodo padre){

//		variable que comunica un estado del nodo despues de la insercion como overflow
		int estado=0;
		if (raizArbol!=null){

			Nodo padre1=raizArbol;
//			veo el espacio libre para ver si hay overflow luego
			int espacioLibre = raizArbol.getEspacioLibre();
//			se deberia hidratar la lista de claves con los documentos y nodos 


			
			if (raizArbol.getClass()==NodoInterno.class){
//				recorro la lista de registros del nodo hasta encontrar una clave mayor
				List<Registro> regs = raizArbol.getRegistros();
				Registro regAux= null;
				Registro regAnterior=null;
				int i =0;
				int resultadoComparacion=-1;
				int nodoAnt=raiz.getNodoComodin(); // por default 
				Iterator it = regs.iterator();

				while ((it.hasNext())&& (resultadoComparacion<=0)){
					if (i!=0) regAnterior =(Registro) regAux.clone();
					regAux=((Registro)it.next());
					String claveArbol =regAux.getClave();
					resultadoComparacion= claveArbol.compareTo(registro.getClave());
					i++;


				}
//				si hay un solo elemento en la lista de registros, no puedo guardar el anterior
				if (resultadoComparacion>0){
					if (regAnterior==null)  {
						nodoAnt= raizArbol.getNodoComodin();
					}else{
						nodoAnt=regAnterior.getOffset();
					}
							
				}else{
					nodoAnt = regAux.getOffset();
					
				}
				
				
//				padre=raizArbol.getNumeroNodo();
			
				raizArbol= this.getNodo(nodoAnt);
				estado= insertarRegistro(raizArbol, registro,padre1);
				
			}else{
				if (estaClaveEnHoja(registro.getClave(),(NodoHoja)raizArbol)){
					// actualizar el archivo secuencial
					int offsetRegistro = this.offsetClaveEnHoja(registro.getClave(), (NodoHoja)raizArbol);
					int offset = this.archivoSecuencial.actualizarRegistro(offsetRegistro, registro);
					registro.setOffset(offset);
					return 0;
				}

				if (registro.getTamanio()> espacioLibre){
					estado=1;
					//Solo para el caso en que la raiz es hoja
					if (raizArbol.getNumeroNodo()==0){
						//agrego igual el registro a la lista de registro en ram para poder bien la particion
						estado =0;
						int offset = this.archivoSecuencial.darAltaRegistro(registro);
						registro.setOffset(offset);
						raizArbol.addRegistro(registro);
						List<Registro> reg = raizArbol.getRegistros();
						Collections.sort(reg);
						//copio la mitad 
						int cantRegistros = reg.size();
						int cantRegDiv2= cantRegistros/2;


						Iterator it = reg.iterator();
						int i =0;
						NodoHoja nodoHijoIzq = new NodoHoja(this.tamanioNodo,1,2);
						this.addNodo(nodoHijoIzq,nodoHijoIzq.getNumeroNodo());


						NodoHoja nodoHijoDer = new NodoHoja(this.tamanioNodo,2,0);
						this.addNodo(nodoHijoDer,nodoHijoDer.getNumeroNodo());
						while (it.hasNext()){
							Registro regAux = (Registro)((Registro)it.next()).clone();
							if ((0<=i)&&(i<cantRegDiv2)){

								nodoHijoIzq.addRegistro(regAux);
							}
							if ((cantRegDiv2<=i)&&(i<=cantRegistros)){
								
								nodoHijoDer.addRegistro(regAux);

							}
							i++;


						}
						this.addNodo(nodoHijoIzq,nodoHijoIzq.getNumeroNodo());
						this.addNodo(nodoHijoDer,nodoHijoDer.getNumeroNodo());
						Registro  regRaiz = (Registro)nodoHijoDer.getRegistro(0).clone();

						regRaiz.setOffset(nodoHijoDer.getNumeroNodo());
						NodoInterno nuevaRaiz = new NodoInterno(1,this.tamanioNodo,0,nodoHijoIzq.getNumeroNodo());
						nuevaRaiz.addRegistro(regRaiz);
						//mantener la raiz en el nodo 0 siempre!!!!!!!!!!!
						this.addNodo(nuevaRaiz,0);
						this.raiz=nuevaRaiz;
			



					}
					return estado;

				}else //si no  hay overflow en la hoja
				{
//					Si es nodo hoja: se deberia insertar el nuevo registro
					int offset = this.archivoSecuencial.darAltaRegistro(registro);
					registro.setOffset(offset);
					//TODO insertar clave en la hoja
					raizArbol.addRegistro(registro);
					Collections.sort(raizArbol.getRegistros());
					this.addNodo(raizArbol,raizArbol.getNumeroNodo());

					return 0; // si sale todo ok, sin overflow
				}
				
			}// fin de si es hoja
	
			
		if (estado==1)return resolverOverflow(raizArbol, registro, padre1);
		}// fin de raiz es distinta de null
	
		this.addNodo(raizArbol,raizArbol.getNumeroNodo());
		return 0;
	}// fin insertar registro

	/**
	 * Cierra el archivo y actualiza el header
	 *
	 */
	public void cerrarArchivo(){
		AcumuladorDeBytesTransferidos acumulador = AcumuladorDeBytesTransferidos.obtenerInstancia();
		byte bloqueHeader[] = new byte[tamanioNodo];
		// cargo en el bloque header el tamanio del nodo
		byte bloqueInt[]=Utilidades.intToByteArray(tamanioNodo);
		int posicionActual =0;
		Utilidades.copyByteArrayInByteArray(bloqueHeader, bloqueInt, posicionActual,4);
		posicionActual+= 4;
		// cargo en el bloque header la altura inicial del arbol
		bloqueInt= Utilidades.intToByteArray(this.altura);
		Utilidades.copyByteArrayInByteArray(bloqueHeader, bloqueInt, posicionActual,4);
		posicionActual+= 4;
		// cargo en el bloque header la cantidad de nodos, inicialmente 1 
		bloqueInt= Utilidades.intToByteArray(this.cantidadDeNodos);
		Utilidades.copyByteArrayInByteArray(bloqueHeader, bloqueInt, posicionActual,4);
		posicionActual+= 4;
		
		try {
			//antes de cerrar el archivo actualizo el header por las dudas
			this.archivo.seek(0);
			this.archivo.write(bloqueHeader);
			this.archivo.close();
			acumulador.acumular(this.tamanioNodo);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int resolverOverflow(Nodo raizArbol, Registro registro, Nodo padre) {
 
		if (raizArbol.getClass()== NodoHoja.class){
			int offset = this.archivoSecuencial.darAltaRegistro(registro);
			registro.setOffset(offset);
			// el nodo rebalzado va a ser despues un nodo izquierdo
			raizArbol.addRegistro(registro);

			List<Registro> reg =  raizArbol.getRegistros();
			Collections.sort(reg);
			//copio la mitad 
			int cantRegistros = reg.size();
			int cantRegDiv2= cantRegistros/2;


			Iterator it = reg.iterator();
			int i =0;

			//tomo al menos un registro para crear al nodo

			NodoHoja nodoHijoIzq = new NodoHoja(this.tamanioNodo,raizArbol.getNumeroNodo(),this.getCantidadDeNodos());
		
			this.addNodo(nodoHijoIzq,nodoHijoIzq.getNumeroNodo());
              
			//El nuevo nodo hja va a tener el numero de nodo = a la cantidad de nodos que tenga el archivo
			// El siguiente va a ser el siguiente nodo que tiene raizArbol 
//			Registro registroHijoDer = (Registro)reg.get(cantRegDiv2);
			NodoHoja nodoHijoDer = new NodoHoja(this.tamanioNodo,this.getCantidadDeNodos(),raizArbol.getNodoComodin());
			this.addNodo(nodoHijoDer,nodoHijoDer.getNumeroNodo());

			//Reparto las claves
			while (it.hasNext()){
				Registro regAux = (Registro)((Registro)it.next()).clone();
				if ((0<=i)&&(i<cantRegDiv2)){

					nodoHijoIzq.addRegistro(regAux);
				
				}
				if ((cantRegDiv2<=i)&&(i<=cantRegistros)){

					nodoHijoDer.addRegistro(regAux);
				}
				i++;
			}
			
			
			this.addNodo(nodoHijoIzq,nodoHijoIzq.getNumeroNodo());
			this.addNodo(nodoHijoDer,nodoHijoDer.getNumeroNodo());
			
			
			Registro  regPadre = (Registro)nodoHijoDer.getRegistro(0).clone();
			regPadre.setOffset(nodoHijoDer.getNumeroNodo());

			//podria ser con un buscar padre :S
			Nodo nodoPadre = padre;
			int espacioLibreAnt = nodoPadre.getEspacioLibre();
			nodoPadre.addRegistro(regPadre);
			List<Registro> regsPadre = nodoPadre.getRegistros();
						
			Collections.sort(regsPadre);
			this.addNodo(nodoPadre,padre.getNumeroNodo());
			//informo que hay overflow en el nodo padre
			if (regPadre.getTamanio()>espacioLibreAnt ) return 1;
			else return 0;

		}// fin si es nodo hoja y hay overflow 
		
		else {// si es nodo interno
		
			//copio la mitad 
		
			List<Registro> reg = raizArbol.getRegistros();
			
			int cantRegistros = reg.size();
			int cantRegDiv2= cantRegistros/2;


			Iterator it = reg.iterator();
			int i =0;
			NodoInterno nodoHijoIzq=null;
			NodoInterno nuevaRaiz=null;
			boolean esNuevaRaiz = false; 
			if (this.raiz.getNivel()==raizArbol.getNivel()){
				esNuevaRaiz = true;
				nodoHijoIzq = new NodoInterno(raizArbol.getNivel(),this.tamanioNodo,this.getCantidadDeNodos(),raizArbol.getNodoComodin());
				this.addNodo(nodoHijoIzq,nodoHijoIzq.getNumeroNodo());
				nuevaRaiz = new NodoInterno(this.raiz.getNivel()+1,this.tamanioNodo,0,nodoHijoIzq.getNumeroNodo());
				
				
			} else{
			// el nivel es el mismo que el nodo que rebalzo 
				nodoHijoIzq = new NodoInterno(raizArbol.getNivel(),this.tamanioNodo,raizArbol.getNumeroNodo(),raizArbol.getNodoComodin());
				this.addNodo(nodoHijoIzq,nodoHijoIzq.getNumeroNodo());
			}
			
			//El nuevo nodo interno va a tener el numero de nodo = a la cantidad de nodos que tenga el archivo
			// El nodo comodin va a ser el  nodo que tiene raizArbol 

			Registro registroComodin = (Registro)reg.get(cantRegDiv2);
			//Registro registroHijoDer = (Registro)reg.get(cantRegDiv2+1);

			NodoInterno nodoHijoDer = new NodoInterno(raizArbol.getNivel(),this.tamanioNodo,this.getCantidadDeNodos(),registroComodin.getOffset());
			this.addNodo(nodoHijoDer,nodoHijoDer.getNumeroNodo());

			Registro regMedio = null;
			//Reparto las claves
			while (it.hasNext()){
				Registro regAux = (Registro)((Registro)it.next()).clone();
				if ((0<=i)&&(i<cantRegDiv2)){

					nodoHijoIzq.addRegistro(regAux);
				}
				
				if (i== cantRegDiv2) regMedio = (Registro) regAux.clone();
				if ((cantRegDiv2<i)&&(i<=cantRegistros)){
					nodoHijoDer.addRegistro(regAux);
				}
				i++;
			}

			this.addNodo(nodoHijoDer,nodoHijoDer.getNumeroNodo());
			this.addNodo(nodoHijoIzq,nodoHijoIzq.getNumeroNodo());
			
			regMedio.setOffset(nodoHijoDer.getNumeroNodo());
			//podria ser con un buscar padre :S
			
		    
			if (esNuevaRaiz){
				nuevaRaiz.addRegistro(regMedio);
				this.addNodo(nuevaRaiz, 0);
				this.raiz=nuevaRaiz;
				return 0;
				
			}else{
				Nodo nodoPadre = padre;
				int espacioLibreAnt = nodoPadre.getEspacioLibre();
				nodoPadre.addRegistro(regMedio);
				List<Registro> regsPadre = nodoPadre.getRegistros();
				Collections.sort(regsPadre);
				this.addNodo(nodoPadre,padre.getNumeroNodo());
				
				//informo que hay overflow en el nodo padre
				if (regMedio.getTamanio()>espacioLibreAnt) return 1;
				else return 0;
			}
		
			
			
			
			
			
		}//  fin si es nodo interno
		
	}

/**
 *  busca el nodo hoja donde se encuentra la clave
 * @param claveABuscar
 */
	public List<Integer> buscar(String claveABuscar){
		Registro regABuscar = new Registro(claveABuscar,0);
		NodoHoja nodoEncontrado	= (NodoHoja)buscarClave(regABuscar,this.raiz);
		if (this.estaClaveEnHoja(claveABuscar, nodoEncontrado)){
			int offset = this.offsetClaveEnHoja(claveABuscar, nodoEncontrado);
			Registro registroEncontrado = this.archivoSecuencial.obtenerRegistro(offset,claveABuscar);
			return registroEncontrado.getDocumentos();
		}
		else return null;
	}

/**
 * busca de forma recursiva el nodo hoja donde se deberia encontrar la clave a buscar
 * @param registro que contiene la clave que quiero buscar
 * @param raizArbol 
 * @return
 */
	public Nodo buscarClave(Registro registro,Nodo raizArbol){
		if (raizArbol!=null){

			if (raizArbol.getClass()==NodoInterno.class){
//				recorro la lista de registros del nodo hasta encontrar una clave mayor
				List regs = (List) raizArbol.getRegistros();
				Registro regAux= null;
				Registro regAnterior=null;
				int i =0;
				int resultadoComparacion=-1;
				int nodoAnt=raiz.getNodoComodin(); // por default 
				Iterator it = regs.iterator();

				while ((it.hasNext())&& (resultadoComparacion<=0)){
					if (i!=0) regAnterior =(Registro) regAux.clone();
					regAux=((Registro)it.next());
					String claveArbol =regAux.getClave();
					resultadoComparacion= claveArbol.compareTo(registro.getClave());
					i++;


				}
// si salgo por no encontrar una clave mayor
				if (resultadoComparacion>0){
					if (regAnterior==null)  {
						nodoAnt= raizArbol.getNodoComodin();
					}else{
						nodoAnt=regAnterior.getOffset();
					}
							
				}else{
					nodoAnt = regAux.getOffset();
					
				}
				
				raizArbol= this.getNodo(nodoAnt);
				return  buscarClave(registro, raizArbol);
				
			}//fin si es hoja
			if (raizArbol.getClass()==NodoHoja.class) return raizArbol;
		}//fin si la raiz es nula
		return null;
	}

	/**
	 * Devuelve la lista de nodos que contiene el arbol.
	 * @return lista de nodos del arbol.
	 */
	public List<Nodo> getNodos()
	{
		return null;
		//return this.nodos;
	}

	/**
	 * Se carga la lista de nodos pasada como parametro al arbol.
	 * @param nodos: nodos que forman el arbol.
	 */
	public  void setNodos(List<Nodo> nodos)
	{
		//this.nodos = nodos;
	}
	
	/**
	 * Se muestra por consola el contenido de los nodos.
	 */

	public void mostrarAllNodos()
	{
		//Iterator it = this.nodos.iterator();
		
//		while (it.hasNext())
//		{
//			Nodo nodin = (Nodo)it.next();
//			nodin.mostrarNodo();
//		}
//			
	}
		
	public void mostrarAllNodosArchivo()
	{
		int cantidadDeNodos = this.getCantidadDeNodos();
		int i =1;
	try {
			byte datoNodo[] = new byte[this.tamanioNodo];
			
			while (i<=cantidadDeNodos){
				this.archivo.seek(i*this.tamanioNodo);
				this.archivo.read(datoNodo);
				//obtengo el nivel del nodo
				int nivel = Utilidades.byteArrayToInt(datoNodo,0);
				
				if (nivel==0){
					NodoHoja nodo  = new NodoHoja(i-1, tamanioNodo);
					nodo.hidratar(datoNodo);
					nodo.mostrarNodo();
					
				}else{
					NodoInterno nodo= new NodoInterno(i-1,tamanioNodo);
					nodo.hidratar(datoNodo);
					nodo.mostrarNodo();
				}
				i++;
					
			}		
			
		}catch (Exception e) {
		// TODO: handle exception
		}

}

	public ArchivoSecuencialDocumentos getArchivoSecuencial() {
		return archivoSecuencial;
	}

	public void setArchivoSecuencial(ArchivoSecuencialDocumentos archivoSecuencial) {
		this.archivoSecuencial = archivoSecuencial;
	}

	public RandomAccessFile getArchivo() {
		return archivo;
	}

	public void setArchivo(RandomAccessFile archivo) {
		this.archivo = archivo;
	}
	

}

