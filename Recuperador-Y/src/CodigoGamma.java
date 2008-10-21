import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class CodigoGamma {

	private BitSet arrayBinary;
	
	private BitSet arrayBinaryDecode;
	
	private int tamanioUsadoCode ;
	
	private int tamanioUsadoDecode;
	
	private int cantidadDeNumeros;
	
	/**
	 * Contructor de la clase, inicializa los bitset, se debe pasar por argumento la cantidad de
	 * documentos que se codifican
	 *
	 */
	public CodigoGamma(int cantidadDocumentos){
		this.cantidadDeNumeros= cantidadDocumentos;
		this.arrayBinary = new BitSet();
		this.arrayBinaryDecode = new BitSet();
		//System.out.println(arrayBinary.size());
	}
	
	/**
	 * Coloca un 1 en un bit determinado.
	 * @param dato array de bytes a procesar.
	 * @param indexByte byte del array donde quiero realziar la operacion.
	 * @param indexBit indice donde quiero colocar un 1.
	 * @return Array de bytes modificado.
	 */
	public byte[] ponerUnos(byte dato[], int indexByte, int indexBit){
		int mask=(int)(1<<indexBit);
	//	System.out.println(mask);
		dato[indexByte] = (byte)(dato[indexByte]|mask);
	//	System.out.println(dato[indexByte]);
		return dato;
		
		
   }
	/**
	 * Coloca un 0 en un bit determinado.
	 * @param dato array de bytes a procesar.
	 * @param indexByte byte del array donde quiero realziar la operacion.
	 * @param indexBit indice donde quiero colocar un 0.
	 * @return Array de bytes modificado.
	 */
	
	public byte[] ponerCeros(byte dato[], int indexByte, int indexBit){
		
		int mask=(int)(1<<indexBit);
		mask=(byte) ((byte) 0xFF -mask);
		dato[indexByte] = (byte)(dato[indexByte]& mask);
		return dato;
   }
	/**
	 * Copia el bitSet en un array de bytes
	 * @return array e bytes listo para ser persitido en el archivo
	 */
	
	public byte[] copyCodigoGammaToArrayByte(){
		int tamanio = (int)(this.tamanioUsadoCode/8);
		int resto =  (int)(this.tamanioUsadoCode%8);
		if (resto!=0)tamanio++;
		byte dato[] = new byte[tamanio];
		int totales= tamanio*8;
		
		for (int i =0;i<this.tamanioUsadoCode;i++)
		{
			
		
		  if(arrayBinary.get(i)==true)
		  {
//			  System.out.println("i:" +i +"valor: "+ "1"  +"indexByte: " + i/8 +"indexBit"+ (7- i%8));
			  dato = ponerUnos(dato, i/8,7-i%8);
		  }else
		  {
//			  System.out.println("i:" +i +"valor: "+ "0"  +"indexByte: " + i/8 +"indexBit" +(7- i%8));
			  dato=  ponerCeros(dato, i/8,7-i%8);
		  }
		}
		
		for (int j =this.tamanioUsadoCode;j<totales;j++)
		{
			dato=  ponerCeros(dato, j/8,7-j%8);
		  
		}
				
		
		
			
		return dato;
	}
	
	/**
	 *  Devuelve la cantidad de unos consecutivos que hay en el bitset desde el indice recibido
	 * @param index:  indice del bitset desde donde se cuenta la cantidad de unos consecutivos
	 * @return: cantidad de unos consecutivos
	 */
	
	public int cantidadDeUnos(int index ){
		int tamanio = this.tamanioUsadoDecode;
		boolean esCero = false;
		int acumulador = 0;
		int i=index;
		while ((i<tamanio)&&(!esCero)){
			if (this.arrayBinaryDecode.get(i)){
				acumulador++;
			}else{
			esCero=true;
			}
			i++;
		}
		return acumulador;
		
	}
	
	
	
	/**
	 * Decodifica una tira de bytes codificada en una lista de enteros
	 * @param array de bytes de lista enteros codificadas
	 *	@return lista de enteros decodificada
	 */	
	public List<Integer> decodeCodigoGamma(byte dato[]){
			//obtengo el bitset del array de bytes
			this.arrayBinaryDecode = this.obtenerBitSet(dato);
			int tamanio = this.getTamanioUsadoDecode();
			int i=0;
			int acum = 0;
			List<Integer> listaDistancias = new LinkedList<Integer>();
			
			while ((i<tamanio)&&(acum<this.cantidadDeNumeros)){
				
				int unos = this.cantidadDeUnos(i);
				//salteo los unos
				i+= unos;
				//salte el cero separador
				i++;
//				System.out.println("index: " + i + " cant unos:" + unos);
				int entero = this.getNumeroEntero(i, i+unos);
			
				entero = (int) Math.pow(2,unos)+ entero;
				i+=unos;
                Integer num = new Integer(entero);
                	
				listaDistancias.add(num);
				acum++;
	
	
			
			}
			
			List<Integer> listaEnteros = this.getListIntFromListDist(listaDistancias);
			
			
			
			return listaEnteros;
		
		
	}
	
	/**
	 * Codifica la lista de enteros en codigo gamma
	 * @param listaNumeros: distancias entre numeros de documentos
	 */
	
	public void codeCodigoGamma (List<Integer> listaNumeros){
	    List<Integer> listaDeDistancia=	this.getListaDeDistancias(listaNumeros);
	    
		Iterator<Integer> it = listaDeDistancia.iterator();
		int i = 0;
		while(it.hasNext())     
		{
		      Integer numero = (Integer)it.next();
		      int num = numero.intValue();
		      int l = (int)(Math.log10(num)/Math.log10(2));
		      for (int a=0; a < l; a++)
		      {
		    	  arrayBinary.set(i,true);
		    	  i++;
		      }
		      arrayBinary.set(i,false);
		      i++;
		      for (int a=0; a < l; a++) 
		      {
		    	  int aux=  1<< (l-a-1);
		    	  int auxComparator = (int) (num & aux);
		    	  if (auxComparator==aux)
		    	  {
		        	arrayBinary.set(i, true);
		    	  }
		    	  else
		    		  {
		    		  arrayBinary.set(i, false);
		    		  }
		         i++;                
		      }
		}
		this.tamanioUsadoCode = i;
	
	}
	
	/**
	 * Devuelve el bitset donde se encuentra codificado la lista de documentos
	 * @return array binario
	 */
	public BitSet getArrayBinary() {
		return arrayBinary;
	}

	/**
	 * setea el array binario
	 * @param arrayBinary
	 */
	public void setArrayBinary(BitSet arrayBinary) {
		this.arrayBinary = arrayBinary;
	}

	/**
	 * 
	 * 
	 * @param indexBitInit: bit inicial donde se encuentra el entero dentro del bitset
	 * @param indexBitFinal: bit final donde se encuentra el entero dentro del bitset
	 * @return un numero entero representado por los bits entre el indexBitInit y indexBitFinal
	 */
	
	public int getNumeroEntero( int indexBitInit, int indexBitFinal){
		byte entero[]= new byte[4];
	
		int b=0;
  	 	for (int i = indexBitFinal-1; i>=indexBitInit;i--){
  	 		
			if (this.arrayBinaryDecode.get(i)){
//				System.out.println("byte " + (3-b/8) + "bit: "+ b%8);
				entero=	this.ponerUnos(entero,(3-b/8) ,b%8);
			}
			else{
				entero= this.ponerCeros(entero, (3-b/8) ,b%8);
//				System.out.println("byte " + (3-b/8) + "bit: "+ b%8);
			} 
		 b++;
		 }
  	 	
		int numero =  Utilidades.byteArrayToInt(entero, 0);
//		System.out.println(numero);
		return numero;
	
	}

	/**
	*Obtiene el bitSet de un array de bytes
	*/
 	public BitSet obtenerBitSet(byte dato[]){
		int tamanio = dato.length;
		int i=0;
		for(int j=0;j<tamanio;j++){
			byte myByte = dato[j];
		
			for (int a=0; a < 8; a++) 
			{
				int aux=  1<< (8-a-1);
				int auxComparator = (int) (myByte & aux);
				if (auxComparator==aux)
				{
					arrayBinaryDecode.set(i, true);
				}	
				else
				{
					arrayBinaryDecode.set(i, false);
				}
				i++;                
			}
		}  
		this.tamanioUsadoDecode = i;
//		System.out.println("tamanuio del bitset decodificado " +this.tamanioUsadoDecode);
		return arrayBinaryDecode;
	}
	
	/**
	 * Sobreescribo el metodo toString
     */
	public String toString(){
		int tamanio = this.arrayBinary.size();
		String cadena = "";
		for(int i=0;i<tamanio;i++)
		{
			if(arrayBinary.get(i)==false)
			{
				cadena += "0";
			}else
			{
				cadena +="1";
			}
		}
		return cadena;
	}
	
	
	public int getTamanioUsadoDecode() {
		return tamanioUsadoDecode;
	}

	public void setTamanioUsadoDecode(int tamanioUsadoDecode) {
		this.tamanioUsadoDecode = tamanioUsadoDecode;
	}
	
	
	/**
	 * Metodo para imprimir la lista decodificada    
     */
	
	public String toString2(){
		int tamanio = this.arrayBinaryDecode.size();
		String cadena = "";
		for(int i=0;i<tamanio;i++)
		{
			if(arrayBinaryDecode.get(i)==false)
			{
				cadena += "0";
			}else
			{
				cadena +="1";
			}
		}
		return cadena;
	}
	/**
	 * Crea una lista que contiene las distancias entre los enteros 
	 * consecutivos de la lista de documentos
	 * @param listaDocumentos: lista de enteros que representan al numeo}
	 * documento
	 * @return lista de distancias
	 */
	
	
	public List<Integer> getListaDeDistancias(List<Integer> listaDocumentos){
	
		List<Integer> listaDeDistancias = new LinkedList<Integer>();
		
		/** nos aseguramos que este ordenada la lista para no tener distancias
		negativas*/
		Collections.sort(listaDocumentos);
		Iterator it = listaDocumentos.iterator();
		int elementoAnterior = 0;
		if (it.hasNext()){
			elementoAnterior = (Integer)it.next();
			listaDeDistancias.add(elementoAnterior);
		}
		while (it.hasNext()){
			int elementoActual = (Integer) it.next();
			int distancia = elementoActual-elementoAnterior;
			if (distancia!=0)	listaDeDistancias.add(distancia);
			elementoAnterior = elementoActual;
		}
		
		return listaDeDistancias;
		
	}
	
	/**
	 * Rearma la lista de documentos a partir de la lista de distancias.
	 * @param listaDistancia: lista que tiene las distancias entre los documentos consecutivos
	 * @return la lista de documentos vuelta a armar
	 */
	public List<Integer> getListIntFromListDist(List<Integer> listaDistancia){
		List<Integer> listaDocumentos = new LinkedList<Integer>();
		Iterator it = listaDistancia.iterator();
		int elementoAnterior=0;
		if (it.hasNext()){
			elementoAnterior = (Integer)it.next();
			listaDocumentos.add(elementoAnterior);
		}
		while (it.hasNext()){
			int elementoActual = (Integer) it.next();
			int suma = elementoActual+elementoAnterior;
			listaDocumentos.add(suma);
			elementoAnterior = suma;
		}
		
		return listaDocumentos;
	}
	
	
	public int getTamanioUsadoCode() {
		return tamanioUsadoCode;
	}

	public void setTamanioUsadoCode(int tamanioUsadoCode) {
		this.tamanioUsadoCode = tamanioUsadoCode;
	}
	
	public int getCantidadDeBytesOcupados(){
		
		int tamanio = (int)(this.tamanioUsadoCode/8);
		int resto =  (int)(this.tamanioUsadoCode%8);
		if (resto!=0)tamanio++;
		return tamanio;
	
	}
	
}
