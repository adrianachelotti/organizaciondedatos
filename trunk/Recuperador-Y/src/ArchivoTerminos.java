
import java.util.ArrayList;
import java.util.List;

public class ArchivoTerminos extends Archivo {

    //Constructor
	List<String> lista;
    int contador;
    ArchivoPath ad;

    /**
     * @param pe Direccion archivo de entrada 
     * @param ps Direccion archivo de salida
     */
    public ArchivoTerminos(String pe, String ps) {
        super(pe, ps);
        this.ad = new ArchivoPath(ps);
        this.lista = new ArrayList<String>();
        this.contador = 0;
    }

    /**
     * @param pe Direccion archivo de entrada 
     */
    public ArchivoTerminos() {
        super("");
        this.ad = null;
        this.lista = new ArrayList<String>();
        this.contador = 0;
    }

    public void Procesa() {
        super.Leer();
    }

    private void AgregaTerminos(String[] terminos) {
        for (int j = 0; j < terminos.length; j++) {
            terminos[j] = terminos[j].replaceAll(" ", "");
            if ((terminos[j].trim().length() > 0) && (!terminos[j].equals(" ")) && this.Filtro(terminos[j])) {
                lista.add(terminos[j]);
            }
        }
    }

    private boolean Filtro(String termino) {
        byte[] t;
        t = Utilidades.stringToByteArray(termino);
        for (int i = 0; i < t.length; i++) {
            if (t[i] < 0) {
                return (false);
            }
        }
        return (true);
    }

    public void ProcesaLinea(String linea) {
        // TODO Auto-generated method stub
        ListaTermino ter = new ListaTermino(linea);
        String[] terminos = ter.Procesa();
        AgregaTerminos(terminos);
    }

    public String getTermino() {
        if (contador < lista.size()) {
            contador++;
            return (String) lista.get(contador - 1);
        }
        return null;
    }

    public String getNumeroArchivo() {
        return Integer.toString(ad.getIndice());
    }
}
