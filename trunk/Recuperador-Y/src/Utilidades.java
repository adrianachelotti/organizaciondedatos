/**
 * 
 * Clase utulizada para el manejo de cadenas y bytes.
 * 
 */
public class Utilidades {

	/**
	 * Se devuelve como entero el array de bytes pasado como parametro.
	 * @param bytes: array de bytes a convertir.
	 * @param index: posicion de inicio en el array desde donde se empieza a obtener el entero.
	 * @return valor entero del array pasado como parametro.
	 */
	public static int byteArrayToInt(byte[] bytes, int index) {

		return (bytes[index + 3] & 0xFF) |
		((bytes[index + 2] & 0xFF) << 8) |
		((bytes[index + 1] & 0xFF) << 16) |
		((bytes[index] & 0xFF) << 24);
	}

	/**
	 * Se devuelve en un array de bytes el entero pasado como parametro. 
	 * @param value: entero a convertir en un array de bytes.
	 * @return array de bytes resultante de la conversion del entero pasado como parametro.
	 */
	public static byte[] intToByteArray(int value) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			int offset = (b.length - 1 - i) * 8;
			b[i] = (byte) ((value >>> offset) & 0xFF);
		}
			
		return b;
	}
	
	
	/**
	 * Se devuelve como String el array de bytes pasado como parametro.
	 * @param bytes: array de bytes a convertir.
	 * @param index: posicion de inicio en el array desde donde se empieza a obtener la cadena.
	 * @param tamanio : tamanio de la cadena (en bytes).
	 * @return cadena obtenida del array de bytes pasado como parametro.
	 */
	public static String byteArrayToString(byte[] bytes,int index,int tamanio){
		byte[] bloqueString = new byte[tamanio];
		int j = 0;
		for(int i = index;i<index+tamanio;i++){
			 bloqueString[j] = bytes[i];
			 j++;
		}
				
		return (new String(bloqueString));
	}	
	
	/**
	 * Se devuelve en un array de bytes la cadena pasada como parametro.
	 * @param cadena: cadena a convertir a un arrayd e bytes.
	 * @return array de bytes resultante de la conversion de la cadena pasada como parametro.
	 */
	public static byte[] stringToByteArray(String cadena){
		
			return (cadena.getBytes());
	}
	
	/**
	 * Copia un array de bytes en otro array de bytes.
	 * @param bloque:  array de bytes en donde se copian los bytes.
	 * @param bytes:   array de bytes desde donde se obtienen los bytes a copiar.	
	 * @param index: pisicion inicial desde donde se empieza a realizar la copia.
	 * @param cant: cantidad de caracteres a copiar.
	 */
	public static void copyByteArrayInByteArray(byte[] bloque,byte[] bytes,int index,int cant){
		int j=0;
		
		for(int i=index;i<index+cant;i++){
			bloque[i]= bytes[j];
			j++;
		}
		
	}
	
	
}
