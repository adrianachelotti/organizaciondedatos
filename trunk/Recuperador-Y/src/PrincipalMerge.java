import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class PrincipalMerge {

	public static int TAMANIO_BLOQUE = 0;
	public static final String NOMBRE_ARCHIVO_ARBOL = "miArbol.dat"; 
	public static final String NOMBRE_SECUENCIAL = "SecuencialmiArbol.dat";
	public static final String NOMBRE_ARCHIVO_PATH = "textos/docum.dat";
	public static final String NOMBRE_HTML = "test/html";
	public static final String NOMBRE_HTML_FILTRADO = "test/htmlFiltrado";
	public static final String AUXILIAR_TEXTO = "Auxiliar.txt";
	
	
	private static void MoverArchivo(String origen, String destino) {
	        try {
	            FileChannel srcChannel = new FileInputStream(origen).getChannel();
	            FileChannel dstChannel = new FileOutputStream(destino).getChannel();

	            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

	            srcChannel.close();
	            dstChannel.close();

	            //Borra el archivo del origen
	            File f = new File(origen);
	            f.delete();

	        } catch (Exception e) {
	            System.out.println(e);

	        }

	    }
	 private static void ElminarArchivos() {
	       
	       	File f1 = new File(NOMBRE_ARCHIVO_ARBOL);
	        f1.delete();
	        f1 = new File(NOMBRE_SECUENCIAL);
	        f1.delete();
	        f1 = new File(NOMBRE_ARCHIVO_PATH );
	        f1.delete();
	        f1 = new File("textos/detrabajo/terminos.dat");
	        f1.delete();
	        File dir = new File(NOMBRE_HTML_FILTRADO);
	        String[] listaHtml = dir.list();
	        int i = 0;
	        while (listaHtml.length != 0) {
	            MoverArchivo(NOMBRE_HTML_FILTRADO+"/" + listaHtml[i], NOMBRE_HTML+"/" + listaHtml[i]);
	            listaHtml = dir.list();
	        }
	    }


	 private static boolean existeArbol(){
		 
		 boolean existe;
		 File arbol = new File (NOMBRE_ARCHIVO_ARBOL);
		 existe = arbol.exists();
		 if ((existe)&& (TAMANIO_BLOQUE == 0)){
			 byte[] tamanioDeNodoByte = new byte[4];
			 try {
				RandomAccessFile arbolArchivo = new RandomAccessFile(NOMBRE_ARCHIVO_ARBOL,"r");
				arbolArchivo.seek(0);
				arbolArchivo.read(tamanioDeNodoByte);
				TAMANIO_BLOQUE = Utilidades.byteArrayToInt(tamanioDeNodoByte,0);
				arbolArchivo.close();
			 } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		 }	 
		 return existe;
	 }
		
	/**
	 * Metodo principal.
	 */
	public static void main(String[] args) {
		String op = "";
		ArbolBP arbol=null;
		ArchivoPath ad = null;
	 	Date inicioConsulta = null;
	 	Date finConsulta = null;
	 
		while (!op.equals("5")) {
	 			File dir = new File(NOMBRE_HTML);
	 			String consu = null;
	 			Menu m = new Menu();
	 			op = m.mostrarMenu();
	 			
	 			 if (op.equals("1"))
		 			{
	 				 	AcumuladorDeBytesTransferidos acumulador = AcumuladorDeBytesTransferidos.obtenerInstancia();
		 				acumulador.acumular(0);
	 				 	try{
	 				 		if (!existeArbol()) {
	 	                        TAMANIO_BLOQUE = m.tomarTamanio();
	 	                    }
	 				 		
		 					String[] listaHtml = dir.list();
		 					ProcesoFiltrado f = new ProcesoFiltrado();
		 					System.out.println("Procesando documentos.........");
		 					if (listaHtml.length != 0)
		 					{
		 						int i;
		 						ad = new ArchivoPath(NOMBRE_ARCHIVO_PATH );
		 						
		 						for (i = 0; i < listaHtml.length; i++)
		 						{
		 							ArchHTML2 p = new ArchHTML2("test/html/" + listaHtml[i]);
		 							p.Procesa();
		 							ad.agregarPath(NOMBRE_HTML_FILTRADO+"/" + listaHtml[i]);
		 							ArchivoTerminos at = new ArchivoTerminos(AUXILIAR_TEXTO, NOMBRE_ARCHIVO_PATH );
		 							at.Procesa();
		 							f.filtrar(at);
		 							at.Borrar(AUXILIAR_TEXTO);
		 							MoverArchivo(NOMBRE_HTML+"/" + listaHtml[i], NOMBRE_HTML_FILTRADO+"/" + listaHtml[i]);
		 						}
		 					}
		 					else
		 					{
		 						System.out.println("No hay archivos para filtrar en la carpeta html.");
		 					}
		 					f.OrdenaryAgrupar();
		         	 		
		 					// creo el indice 
		 					RegistroIn registroIn = new RegistroIn();
		        	 	
		 					Registro registro1= new Registro("",0);
		 					registroIn.abrir();
		 					registro1 = registroIn.leer();
		        	 			
		 					if(registro1!=null)
		 					{
		 						Nodo raiz = new NodoHoja(TAMANIO_BLOQUE,0,0);
		 						raiz.addRegistro(registro1);
		 						arbol  = new ArbolBP(raiz,TAMANIO_BLOQUE,NOMBRE_ARCHIVO_ARBOL);
		 						while (registro1!=null)
		 						{
		 							registro1 = registroIn.leer();
		 							if (registro1!=null)
		 							{	
		 								arbol.insertar(registro1);
		 							}			
		 						}	 
		 						
		 					}
		 					registroIn.cerrar();
		 					registroIn.eliminar();
		        		
		 				}catch (Exception e) {
							System.out.println("Error en la carga de los documentos.");
							e.printStackTrace();
						}
		 				System.out.println("Finaliza el procesamiento de documentos en forma exitosa.");
		 			}
	 			
	 			 else if (op.equals("2"))
	 			{
	 				if (existeArbol()){
		 				try 
		 				{
		 					
		 					AcumuladorDeBytesTransferidos acumulador = AcumuladorDeBytesTransferidos.obtenerInstancia();
		 					acumulador.reset();
		 					inicioConsulta = new Date(System.currentTimeMillis());
		 					consu = m.tomarConsulta();
		 					Consulta miConsulta = new Consulta(consu);
		 					miConsulta.Filtrar();
						
		 					
		 					miConsulta.MostrarConsulta();
		 					List listaDeTerminos = miConsulta.getTerminos();
		 					
		 					Iterator it = listaDeTerminos.iterator();
		 					String cadenaActual="";
		 					arbol  = new ArbolBP(null,TAMANIO_BLOQUE,NOMBRE_ARCHIVO_ARBOL);
		 					ad = new ArchivoPath(NOMBRE_ARCHIVO_PATH );
		 					while(it.hasNext())
		 					{
		 						cadenaActual=(String)(it.next());	
		 						System.out.println( "Termino buscado: " + cadenaActual);	
		 						List<Integer> lista1 =  arbol.buscar(cadenaActual);
		 						OperacionesConListas.mostrarLista(lista1,ad);
		 					}
		 					finConsulta = new Date(System.currentTimeMillis());
		 					Long tiempoDeConsulta = finConsulta.getTime() - inicioConsulta.getTime();
		 					System.out.println("La consulta tardó " + (tiempoDeConsulta/1000) + " segundos en efectuarse.");
		 					System.out.println("Se trasfirieron " + (acumulador.getCantidadDeBytesTransferidos()/1000) + " KBytes en la consulta.\n");
		 					System.out.println("Gracias por la consulta!!!\n");
		 				} catch (Exception e){
							System.out.println("Error en la opcion de consulta.");
							e.printStackTrace();
						}
	 				}
	 				else
	 				{
	 					System.out.println("No se puede realizar la consulta, debido a que el árbol no está creado.");
	 				}
	 			}
			
	 			else if(op.equals("3")){
	 				if (existeArbol())
	 				{
	 					arbol = new ArbolBP(null,TAMANIO_BLOQUE,NOMBRE_ARCHIVO_ARBOL);
	 					arbol.mostrarAllNodosArchivo();
	 				}
	 				else
	 				{
	 					System.out.println("No se puede mostrar el árbol, debido a que el árbol no está creado. ");
	 				}
	 			}
	 			else if (op.equals("4")) {
	                ElminarArchivos();
	                arbol= null;
	                System.out.println("Los Archivos han sido borrados. Listo para comenzar.");
	            }
	 			else if(op.equals("5")){
	 				if (arbol!=null){
	 					arbol.cerrarArchivo();
	 					if (ad!=null) ad.cerrar();
	 				}
	 				System.out.println("Fin de la aplicacion.");
	 			}
	 	}
	      
	}
}
