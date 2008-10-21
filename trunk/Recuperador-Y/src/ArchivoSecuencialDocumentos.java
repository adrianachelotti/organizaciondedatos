import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
/**
 * Clase que modela un archivo secuencial.
 * En ella se guardaran los terminos con su correspondiente lista de documentos.
 * El archivo persistido en disco cuenta con un header que contiene informacion administrativa 
 * de la siguiente manera: (tamanioDeBloque,CantidadDeBloques)
 * A su vez la definicion logica del archivo esta dada por: ArchivoSecuencialDocumentos(espacioLibre,cantidadDeRegistros,(registro((termino)i,(documento)+))+)
 *
 */
public class ArchivoSecuencialDocumentos {
	
	private final int TAMANIO_INFO_ADMINISTRATIVA = 8; 
	
	private int colchonEspacioLibre ;
	
	private RandomAccessFile archivo;
	
	private int tamanioBloque;
	
	private int cantidadDeBloques;
	
	private String nombre;
	
	/**
	 * Constructor con parametros.
	 * @param nombre: nombre del archivo.
	 * @param tamanioBloque: tamanio de bloque del archivo.
	 */
	public ArchivoSecuencialDocumentos(String nombre,int tamanioBloque){
		
		AcumuladorDeBytesTransferidos acumulador = AcumuladorDeBytesTransferidos.obtenerInstancia();
		this.nombre = nombre;
		this.tamanioBloque = tamanioBloque;
		this.colchonEspacioLibre = (int)((0.2)*tamanioBloque) ;
		this.archivo=null;
		try {
			
			this.archivo = new RandomAccessFile(nombre,"rw");
			byte bloqueHeader[] = new byte[tamanioBloque];
			int posicionActual =0;
			
			if (this.archivo.length()==0)
			{
				// cargo en el bloque header el tamaño del bloque
				byte bloqueInt[]=Utilidades.intToByteArray(tamanioBloque);
				Utilidades.copyByteArrayInByteArray(bloqueHeader, bloqueInt, posicionActual,4);
				posicionActual+= 4;
				//cargo en el bloque header la cantidad de bloques
				bloqueInt= Utilidades.intToByteArray(0);
				//seteo en el obejeto la cantidad inicial de bloques
				this.cantidadDeBloques =0;
				Utilidades.copyByteArrayInByteArray(bloqueHeader, bloqueInt, posicionActual,4);
				posicionActual+= 4;
				this.archivo.write(bloqueHeader, 0,tamanioBloque);
				acumulador.acumular(this.tamanioBloque);
				
			}
			else
			{
				this.archivo.read(bloqueHeader,0,tamanioBloque);
				acumulador.acumular(this.tamanioBloque);
				posicionActual += 4;
				// obtengo la cantidad de bloques.
				this.cantidadDeBloques = Utilidades.byteArrayToInt(bloqueHeader,posicionActual);
											
			}
								
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	
	
	}
	
	
	/**
	 * Se realiza el alta de un registro en el archivo.
	 * @return numero de bloque en donde se ingreso el registro.
	 */
	public int darAltaRegistro(Registro registro){
		
		AcumuladorDeBytesTransferidos acumulador = AcumuladorDeBytesTransferidos.obtenerInstancia();
		byte[] bloque = obtenerBloque();
		boolean hayNuevoBloque = true;
		int posicionDeEscritura = 1;
		
		if(this.getCantidadDeBloques()!=0)
		{		
			//TODO ver si dejo un colchon de espacio libre
			int tamanioNecesario = registro.getTamanioConDocumentos();
			int numeroBloqueActual = 1;
			boolean espacioEncontrado = false;
			
			while((numeroBloqueActual<=this.getCantidadDeBloques())&&(!espacioEncontrado))
			{
				
				try {
					this.archivo.seek(numeroBloqueActual*this.getTamanioBloque());
					this.archivo.read(bloque);
					acumulador.acumular(this.tamanioBloque);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				int espacioLibre = Utilidades.byteArrayToInt(bloque, 0);
				espacioLibre = espacioLibre -this.colchonEspacioLibre;
				if(espacioLibre>tamanioNecesario) espacioEncontrado =true;
				else numeroBloqueActual++;
			}
			
			if (espacioEncontrado)
			{
				hayNuevoBloque = false;
			}
			else
			{
				bloque = obtenerBloque();
							
			}
			posicionDeEscritura = numeroBloqueActual;
		}
				
		cargarRegistroEnBloque(bloque, registro);
		actualizarHeaderBloque(bloque, registro,true);
		if (hayNuevoBloque)
		{
			this.setCantidadDeBloques(this.getCantidadDeBloques()+1);
			actualizarHeader();	
		}
		try {
			this.archivo.seek((this.getTamanioBloque()*posicionDeEscritura));
			this.archivo.write(bloque);
			acumulador.acumular(this.tamanioBloque);
		} catch (IOException e) {
		
			e.printStackTrace();
		}
			
		return posicionDeEscritura;
			
	}

	/**
	 * Crea un nuevo bloque del archivo inicializado.
	 * @return nuevo bloque del archivo.
	 */
	private byte[] obtenerBloque(){
		byte[] nuevoBloque = new byte [this.getTamanioBloque()];
		int posicionActual = 0;
		//cargo el espacio libre del bloque
		int espacioLibre = this.getTamanioBloque() - TAMANIO_INFO_ADMINISTRATIVA ;
		byte[] bloqueInt=Utilidades.intToByteArray(espacioLibre);
		Utilidades.copyByteArrayInByteArray(nuevoBloque, bloqueInt, posicionActual,4);
		posicionActual+= 4;
		//cargo la cantidad inicial de registros del nodo.
		int cantidadDeRegistros = 0;
		bloqueInt=Utilidades.intToByteArray(cantidadDeRegistros);
		Utilidades.copyByteArrayInByteArray(nuevoBloque, bloqueInt, posicionActual,4);
		return nuevoBloque;
		
	}
	
	/**
	 * Actualiza el header del bloque pasado como parametro.
	 * @param bloque: bloque al cual se le realiza la actualizacion del header.
	 * @param registro: registro que produce la modificacion del header.
	 * @param esUnAlta: true en caso que se realiza un alta.
	 * 					false en caso contrario.
	 */
	
	private void actualizarHeaderBloque(byte[] bloque,Registro registro,boolean esUnAlta){
		int posicionActual = 0;
		//leo el espacio libre del bloque
		int espacioLibre = Utilidades.byteArrayToInt(bloque, posicionActual);
		//Actualizo el espacio libre
		if(esUnAlta)
			espacioLibre-= registro.getTamanioConDocumentos();
		else espacioLibre+= registro.getTamanioConDocumentos();
		byte[] bloqueInt=Utilidades.intToByteArray(espacioLibre);
		Utilidades.copyByteArrayInByteArray(bloque, bloqueInt, posicionActual,4);
		posicionActual+= 4;
		
		//leo la cantidad inicial de registros del nodo.
		int cantidadDeRegistros = Utilidades.byteArrayToInt(bloque, posicionActual);
		//Actualizo la cantidad de registros.
		if (esUnAlta)cantidadDeRegistros++;
		else cantidadDeRegistros--;
		bloqueInt=Utilidades.intToByteArray(cantidadDeRegistros);
		Utilidades.copyByteArrayInByteArray(bloque, bloqueInt, posicionActual,4);
		
	}
		
	
	/**
	 * Obtiene la posicion del bloque desde donde se debe empezar a cargar el registro.
	 * @param bloque: bloque del cual se determina la posicion.
	 * @param registro: registro a cargar.
	 * @return
	 */
	
	private int obtenerPosicionDeInicioDeCarga(byte[] bloque,Registro registro){
		
		int posicionActual = 4;
		int cantidadDeRegistros = Utilidades.byteArrayToInt(bloque, posicionActual);
		posicionActual+=4;
		
		if(cantidadDeRegistros==0)
		{
			posicionActual=8;			
		}
		else
		{
			while(cantidadDeRegistros!=0)
			{
				int tamanioClave = Utilidades.byteArrayToInt(bloque, posicionActual);
				//salto el campo de control de tamanio de la clave , el tamanio de la clave y la cantidad de documentos
				posicionActual+= 4 + tamanioClave + 4;
			//salto el campo de la cantidad de bytes
				int cantidadDeBytes = Utilidades.byteArrayToInt(bloque,posicionActual);
				posicionActual+= 4 + cantidadDeBytes;
				cantidadDeRegistros--;
			}
		}
		
		return posicionActual;
		
	}
	
	/**
	 * Carga el registro pasado como parametro en el bloque.
	 * Realiza la carga despues del ultimo registro existente.
	 * @param bloque: bloque en donde se carga el registro.
	 * @param registro: registro a cargar.
	 */
	
	private void cargarRegistroEnBloque(byte[] bloque,Registro registro)
	{

		int posicionActual = obtenerPosicionDeInicioDeCarga(bloque, registro);
				
		//Primero cargo la longitud del termino.
		int tamanioClave = registro.getClave().length();
		byte[] bloqueInt = Utilidades.intToByteArray(tamanioClave);
		Utilidades.copyByteArrayInByteArray(bloque,bloqueInt, posicionActual, 4);
		posicionActual+=4;
		//Cargo el termino
		byte[] bloqueString = Utilidades.stringToByteArray(registro.getClave());
		Utilidades.copyByteArrayInByteArray(bloque, bloqueString, posicionActual, tamanioClave);
		posicionActual+=tamanioClave;
		
		//Cargo la cantidad de documentos
		int cantidadDeDocumentos = registro.getDocumentos().size();
		bloqueInt = Utilidades.intToByteArray(cantidadDeDocumentos);
		Utilidades.copyByteArrayInByteArray(bloque, bloqueInt, posicionActual, 4);
		posicionActual+=4;

		//Cargo la cantidad de bytes
		CodigoGamma codigo = new CodigoGamma(cantidadDeDocumentos);
		codigo.codeCodigoGamma(registro.getDocumentos());
		byte codigoGamma[]= codigo.copyCodigoGammaToArrayByte();
	 	int cantidadDeBytes = codigo.getCantidadDeBytesOcupados();
	 	bloqueInt = Utilidades.intToByteArray(cantidadDeBytes);
		Utilidades.copyByteArrayInByteArray(bloque, bloqueInt, posicionActual, 4);
		posicionActual+=4;
		
		
		//Cargo las distancias codificadas en codigo gamma
		Utilidades.copyByteArrayInByteArray(bloque,codigoGamma,posicionActual,cantidadDeBytes);
		//Cargo las distancias codificadas en codigo gamma
		/*int i=0;
		for (int j=posicionActual;j<posicionActual+cantidadDeBytes;j++){
			bloque[j]=codigoGamma[i];
			i++;
			
		}*/
		posicionActual+= cantidadDeBytes;
		
}
	
	
	/**
	 * Actualiza el header del archivo.
	 */
	private void actualizarHeader(){
		AcumuladorDeBytesTransferidos acumulador = AcumuladorDeBytesTransferidos.obtenerInstancia();
		byte[] bloque = new byte[this.getTamanioBloque()];

		try {
			this.archivo.seek(0);
			this.archivo.read(bloque);
			acumulador.acumular(this.tamanioBloque);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int posicionActual = 4;
		
		//Actualizo la cantidad de bloques.
		byte[]bloqueInt=Utilidades.intToByteArray(this.getCantidadDeBloques());
		Utilidades.copyByteArrayInByteArray(bloque, bloqueInt, posicionActual,4);
		
		try {
			this.archivo.seek(0);
			this.archivo.write(bloque);
			acumulador.acumular(this.tamanioBloque);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * Retorna la cantidad de bloques del archivo.
	 * @return cantidad de bloques del archivo.
	 */
	public int getCantidadDeBloques() {
		return cantidadDeBloques;
	}

	/**
	 * Setea la cantidad de bloques del archivo.
	 * @param cantidadDeBloques: cantidad de bloques del archivo.
	 */

	public void setCantidadDeBloques(int cantidadDeBloques) {
		this.cantidadDeBloques = cantidadDeBloques;
	}

	/**
	 * Retorna el nombre del archivo.
	 * @return nombre del archivo.
	 */
	public String getNombre() {
		return nombre;
	}


	/**
	 * Setea el nombre del archivo.
	 * @param nombre: nombre del archivo.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Retorna el tamanio de los bloques del archivo.
	 * @return tamanio de los bloques del archivo.
	 */
	public int getTamanioBloque() {
		return tamanioBloque;
	}

	
	/**
	 * Setea el tamanio de los bloques del archivo.
	 * @param tamanioBloque: tamanio de los bloques del archivo.
	 */
	public void setTamanioBloque(int tamanioBloque) {
		this.tamanioBloque = tamanioBloque;
	}

	/**
	 * Imprime por pantalla el contenido del header del archivo.
	 */	
	public void imprimirHeader(){
		byte[] bloque = new byte[this.getTamanioBloque()];

		try {
			this.archivo.seek(0);
			this.archivo.read(bloque);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int posicionActual = 0;

		//Leo el tamanio de los bloques.
		int tamanioDeBloques = Utilidades.byteArrayToInt(bloque, posicionActual);
		System.out.println("Tamanio de los bloques: " + tamanioDeBloques);
		posicionActual+=4;
				
		//Leo la cantidad de bloques.
		int cantidadDeBloques=Utilidades.byteArrayToInt(bloque,posicionActual);
		this.setCantidadDeBloques(cantidadDeBloques);
		System.out.println("Cantidad de bloques: " + cantidadDeBloques);

	}
	
	/**
	 * Imprime los bloques del archivo.
	 * ArchivoSecuencialDocumentos(espacioLibre,cantidadDeRegistros,(registro((termino)i,(documento)+))+)
	 */
	public void imprimirBloquesArchivo(){
		
		int cantidadBlq = this.getCantidadDeBloques();
		byte[] bloqueActual = new byte[this.tamanioBloque];
		for (int i=1;i<=cantidadBlq;i++)
		{	
			System.out.println("____ Bloque: " +i);
			try {
				this.archivo.seek(i*this.tamanioBloque);
				this.archivo.read(bloqueActual);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			int posicionActual = 0;
			//Leo el espacio libre del bloque.
			int espacioLibreBloque = Utilidades.byteArrayToInt(bloqueActual, posicionActual);
			System.out.println("Espacio libre: " + espacioLibreBloque);
			posicionActual+=4;
					
			//Leo la cantidad de registros.
			int cantidadDeRegistros=Utilidades.byteArrayToInt(bloqueActual,posicionActual);
			System.out.println("Cantidad de registros: " + cantidadDeRegistros);
			posicionActual+=4;
			
			while (cantidadDeRegistros!=0)
			{
				int tamanioDeLaClave = Utilidades.byteArrayToInt(bloqueActual, posicionActual);
				posicionActual+=4;
			    String clave = Utilidades.byteArrayToString(bloqueActual,posicionActual, tamanioDeLaClave);
				posicionActual+=tamanioDeLaClave;
				int cantidadDeDocumentos = Utilidades.byteArrayToInt(bloqueActual, posicionActual);
				posicionActual+=4;
				
				//obtengo la cantidad de byte contiene la codificacion de la lista
				int cantidadDeBytes = Utilidades.byteArrayToInt(bloqueActual, posicionActual);
				posicionActual+=4;
				
				byte listaCodificada[] = new byte[cantidadDeBytes];
				for (int k=0;k<cantidadDeBytes;k++){
					listaCodificada[k]=bloqueActual[posicionActual+k];
					
				}
				
				posicionActual+= cantidadDeBytes;
				
				CodigoGamma codigo = new CodigoGamma(cantidadDeDocumentos);
				List<Integer> documentos = codigo.decodeCodigoGamma(listaCodificada);
			
				System.out.print("["+tamanioDeLaClave+" ; " + clave +" ; " + cantidadDeDocumentos +" : " );
				for (Integer doc: documentos){
					System.out.print( doc + " ");
									
				}
				System.out.println("] ");
				
				cantidadDeRegistros--;
				
		    }
		}
			
	}
		
		
		
	/**
	 * Obtiene el registro que posee la clave dada como parametro. 
	 * @param numeroBloque: numero de bloque donde se encuentra el registro.
	 * @param clave: clave del registro a buscar.
	 * @return registro: registro que posee la clave pasada como parametro.
	 *		   null: en caso de que la clave no se encuentre.
	 */        
	public Registro obtenerRegistro(int numeroBloque,String clave){
		
		byte[] bloqueActual = new byte[this.tamanioBloque];
		AcumuladorDeBytesTransferidos acumulador = AcumuladorDeBytesTransferidos.obtenerInstancia();
		
		try {
			this.archivo.seek(numeroBloque*this.tamanioBloque);
			this.archivo.read(bloqueActual);
			acumulador.acumular(this.tamanioBloque);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean encontrado=false;
		int posicionActual = 4;
		//Leo la cantidad de registros.
		int cantidadDeRegistros=Utilidades.byteArrayToInt(bloqueActual,posicionActual);
		posicionActual+=4;
		
		Registro registroEncontrado = null;
		while ((cantidadDeRegistros!=0)&&(!encontrado))
		{
			int tamanioDeLaClave = Utilidades.byteArrayToInt(bloqueActual, posicionActual);
			posicionActual+=4;
		    String claveABuscar = Utilidades.byteArrayToString(bloqueActual,posicionActual, tamanioDeLaClave);
			posicionActual+=tamanioDeLaClave;
			int cantidadDeDocumentos = Utilidades.byteArrayToInt(bloqueActual, posicionActual);
			posicionActual+=4;
			
			//obtengo la cantidad de byte contiene la codificacion de la lista
			int cantidadDeBytes = Utilidades.byteArrayToInt(bloqueActual, posicionActual);
			posicionActual+=4;
			
			byte listaCodificada[] = new byte[cantidadDeBytes];
			for (int k=0;k<cantidadDeBytes;k++){
				listaCodificada[k]=bloqueActual[posicionActual+k];
				
			}
			
			posicionActual+= cantidadDeBytes;
			
			CodigoGamma codigo = new CodigoGamma(cantidadDeDocumentos);
			List<Integer> documentos = codigo.decodeCodigoGamma(listaCodificada);
			
			if (clave.compareTo(claveABuscar)==0) {
				registroEncontrado = new Registro(clave,numeroBloque);
				registroEncontrado.setDocumentos(documentos);
				encontrado = true;
			}
			cantidadDeRegistros--;
			
	    }
		
		return registroEncontrado;
	 }
	
	/**
	 * Actualiza el registro con la misma  clave  que la del registro pasado como parametro.
	 * En caso de ser posible el ingreso  del registo en el mismo bloque lo realiza en este,
	 * caso contrario realiza la insercion en el primer bloque con espacio.
	 * @param numeroBloque: numero de bloque donde se encuentra el registro.
	 * @param registro: registro a actualizar.
	 * @return numero de bloque donde se guardo el registro.
	 */
	public int actualizarRegistro(int numeroBloque ,Registro registro){
		AcumuladorDeBytesTransferidos acumulador = AcumuladorDeBytesTransferidos.obtenerInstancia();
		byte[] bloqueActual = new byte[this.tamanioBloque];
		String clave = registro.getClave();
		int nroBloqueInsercion = numeroBloque;
		
		try {
			this.archivo.seek(numeroBloque*this.tamanioBloque);
			this.archivo.read(bloqueActual);
			acumulador.acumular(this.tamanioBloque);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean encontrado=false;
		int posicionActual = 4;
		int posicionAnterior =4;
		//Leo la cantidad de registros.
		int cantidadDeRegistros=Utilidades.byteArrayToInt(bloqueActual,posicionActual);
		posicionActual+=4;
		
		Registro registroEncontrado = null;
		while ((cantidadDeRegistros!=0)&&(!encontrado))
		{   
			posicionAnterior = posicionActual;
			int tamanioDeLaClave = Utilidades.byteArrayToInt(bloqueActual, posicionActual);
			posicionActual+=4;
		    String claveABuscar = Utilidades.byteArrayToString(bloqueActual,posicionActual, tamanioDeLaClave);
			posicionActual+=tamanioDeLaClave;
			
			int cantidadDeDocumentos = Utilidades.byteArrayToInt(bloqueActual, posicionActual);
			posicionActual+=4;
		
			//obtengo la cantidad de byte contiene la codificacion de la lista
			int cantidadDeBytes = Utilidades.byteArrayToInt(bloqueActual, posicionActual);
			posicionActual+=4;
			
			if (clave.compareTo(claveABuscar)==0) {
				
			
				
				byte listaCodificada[] = new byte[cantidadDeBytes];
				for (int k=0;k<cantidadDeBytes;k++){
					listaCodificada[k]=bloqueActual[posicionActual+k];
					
				}
				
				posicionActual+= cantidadDeBytes;
				
				CodigoGamma codigo = new CodigoGamma(cantidadDeDocumentos);
				List<Integer> documentos = codigo.decodeCodigoGamma(listaCodificada);
				
				registroEncontrado = new Registro(clave,numeroBloque);
				registroEncontrado.setDocumentos(documentos);
			
				// correr los registros hacia la izquierda
				byte [] bloqueResto = new byte[tamanioBloque-posicionActual];
				int j=0;
				for (int i=posicionActual;i<tamanioBloque;i++){
					bloqueResto[j]= bloqueActual[i];
					j++;
				}
				Utilidades.copyByteArrayInByteArray(bloqueActual, bloqueResto, posicionAnterior, tamanioBloque-posicionActual);
				try {
					this.archivo.seek(numeroBloque*this.tamanioBloque);
					this.archivo.write(bloqueActual);
					acumulador.acumular(this.tamanioBloque);
				} catch (IOException e) {
					e.printStackTrace();
				}
						
				this.actualizarHeaderBloque(bloqueActual, registroEncontrado, false);
				registroEncontrado.agregarDocumentos(registro.getDocumentos());
				//leo el espacio libre del bloque
				int espacioLibre = Utilidades.byteArrayToInt(bloqueActual,0);
							
				
				//Veo si puedo ingresar en el mismo bloque
				if(espacioLibre>registroEncontrado.getTamanioConDocumentos())
				{
					this.cargarRegistroEnBloque(bloqueActual, registroEncontrado);
					this.actualizarHeaderBloque(bloqueActual, registroEncontrado, true);
					try {
						this.archivo.seek(numeroBloque*this.tamanioBloque);
						this.archivo.write(bloqueActual);
						acumulador.acumular(this.tamanioBloque);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
										
				}
				else 
				{
					 nroBloqueInsercion = this.darAltaRegistro(registroEncontrado);
				}
				
				encontrado = true;
			}
			else
			{
				posicionActual+= cantidadDeBytes;
			}
					
			cantidadDeRegistros--;
			
	    }
		
		return nroBloqueInsercion;
	
			
	}
	
	
}

