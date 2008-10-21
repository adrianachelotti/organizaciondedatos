import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class OperacionesConListas {
	
	
	
	/**
	 * Se fija si un numero esta incluido en la lista de enteros
	 * @param numero
	 * @return true si lo encuentra, caso contradrio false
	 */
	
	public static boolean estaEnLaLista(Integer numero, List<Integer> list){
		Iterator it = list.iterator();
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
	 * Realiza una interseccion entre dos listas
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static List<Integer> operacionAND(List<Integer> list1, List<Integer> list2){
		Collections.sort(list1);
		Collections.sort(list2);
		List<Integer> listaChica ;
		List<Integer> listaGrande;
		List<Integer> listaInterseccion= new LinkedList<Integer>();
		// solo para hacer mas facil el manejo
		if (list1.size() < list2.size()){
			listaChica = list1;
			listaGrande = list2;
		}else {
			listaChica = list2;
			listaGrande= list1;
		}
		Iterator it = listaChica.iterator();
		while (it.hasNext()){
			Integer numero = ((Integer)it.next());
			if (estaEnLaLista(numero,listaGrande)==true){
				listaInterseccion.add(numero);
				
				
			}
			
		}
		Collections.sort(listaInterseccion);
		return listaInterseccion;	 
		
		
		
		
	}
	/**
	 * Realiza una union de dos listas sacando los elementos repetidos
	 * @param list1
	 * @param list2
	 * @return listaUnion = list1 U list2
	 */
	public static List<Integer> operacionOR(List<Integer> list1, List<Integer> list2){
		Collections.sort(list1);
		Collections.sort(list2);
		List<Integer> listaChica ;
		List<Integer> listaGrande;
	
		// solo para hacer mas facil el manejo
		if (list1.size() < list2.size()){
			listaChica = list1;
			listaGrande = list2;
		}else {
			listaChica = list2;
			listaGrande= list1;
		}
		// ver de hacer un clone()
		List<Integer> listaUnion= listaGrande;
		
		Iterator it = listaChica.iterator();
		while (it.hasNext()){
			Integer numero = ((Integer)it.next());
			if (estaEnLaLista(numero,listaGrande)==false){
				listaUnion.add(numero);
				
				
			}
			
		}
		Collections.sort(listaUnion);
		return listaUnion;	 
		
		
		
		
		
	}
	/**
	 * Crea una lista que no tiene numeros que esten en ambas listas
	 * @param list1
	 * @param list2
	 * @return list1 - list2
	 */
	public static List<Integer> operacionRESTO(List<Integer> list1, List<Integer> list2){
		Collections.sort(list1);
		Collections.sort(list2);
		List<Integer> listaChica ;
		List<Integer> listaGrande;
		
		// solo para hacer mas facil el manejo
		if (list1.size() < list2.size()){
			listaChica = list1;
			listaGrande = list2;
		}else {
			listaChica = list2;
			listaGrande= list1;
		}
		List<Integer> listaResto= new LinkedList<Integer>();
		Iterator it = listaChica.iterator();
		while (it.hasNext()){
			Integer numero = ((Integer)it.next());
			if (estaEnLaLista(numero,listaGrande)==false){
				listaResto.add(numero);		
				
			}
			
		}
		Iterator it2 = listaGrande.iterator();
		while (it2.hasNext()){
			Integer numero = ((Integer)it2.next());
			if (estaEnLaLista(numero,listaChica)==false){
				listaResto.add(numero);		
				
			}
			
		}
		
		Collections.sort(listaResto);
		return listaResto;	 
		
		
		
		
	}
	
	public static void mostrarLista(List<Integer> lista, ArchivoPath archivo){
		
        
		if ((lista==null)||(lista.isEmpty()))
		{
			System.out.println("No hay resultados para la busqueda.\n");
        }
		else	
		{
			Integer cantidadDeDocumentosEncontrados = lista.size();
			System.out.println("Documentos donde se encontro el termino: ");
			ImpresorTituloBody impresor = null;
			for (Integer entero: lista)
			{
				String cadenaPath = archivo.recuperarPath(entero);
				impresor = new ImpresorTituloBody(cadenaPath);
	            System.out.println("Documento: " +cadenaPath);
	            impresor.imprimirPorPantalla();
	        }
			System.out.println("Se encontraron " + cantidadDeDocumentosEncontrados + "  documentos en la consulta.\n");
			
		}
				
	}

}
