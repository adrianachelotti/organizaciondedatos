import java.io.*;

/**
 *
 *Clase que modela el proceso de filtrado.
 * 
 */
public class ProcesoFiltrado {

    private OrdenadorDeTerminos ordenador = new OrdenadorDeTerminos();

    public ProcesoFiltrado() {

    }

    /**
     * @param args the command line arguments
     */
    public void filtrar(ArchivoTerminos archivoTerminos) throws FileNotFoundException, IOException, ClassNotFoundException {
        String palabra;
        int numeroDeDocu = 0;


        //saco el numero de archivo y lo escribo en el archivo de terminos.
        if (archivoTerminos.ad!=null){
        	numeroDeDocu = Integer.parseInt(archivoTerminos.getNumeroArchivo());
        }

        //se crean los diferentes filtros con sus archivos de comparacion.
        FiltroFrase filF = new FiltroFrase(archivoTerminos,"textos/Diccionarios/frases.txt");
        FiltroWord filW = new FiltroWord("textos/Diccionarios/words.txt");
        FiltroStemming filS = new FiltroStemming("textos/Diccionarios/stemming.txt");

        //comienza el primer filtro llenando el tubo.
        filF.CargarTubo();
        while (filF.tubo[0] != null) {
            palabra = filF.Filtrar();
            //solo es un if de control
            if (!palabra.equals("FILTRADO"))	
            {
                filW.CargarPalabra(palabra);
                palabra = filW.Filtrar();
                //solo es un if de control
                if (!palabra.equals("FILTRADO") & !(filW.contieneNumeros(palabra)))
                {
                    if (!palabra.equals("*"))
                    {
                    	filS.CargarPalabra(palabra);
                    	palabra = filS.Filtrar();
                    	RegistroUnitario reg = new RegistroUnitario(palabra, numeroDeDocu);
                    	ordenador.addTermino(reg);
                    }
                }
            }
        }
        
     
       
    }

    public void OrdenaryAgrupar() throws IOException, ClassNotFoundException {
        ordenador.ordenar();
        ordenador.agrupar();
       // ordenador.SacarPorPantalla();
    }

	public OrdenadorDeTerminos getOrdenador() {
		return ordenador;
	}
}
