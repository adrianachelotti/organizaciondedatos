import java.io.*;

/**
 *
 *Clase que modela el filtro de stemming.
 *
 */
public class FiltroStemming extends Filtro {

    String Palabra;

    FiltroStemming(String Path) {
        PathArchivo = Path;
    }

    public void CargarPalabra(String pal) {
        Palabra = pal;
    }

    public String Filtrar() throws FileNotFoundException, IOException {
        int compa = -1;
        decomparacion = new Lector(PathArchivo);
        decomparacion.leerLinea();
        while ((decomparacion.linea != null) && (compa == -1)) {
            if (Palabra.contains(decomparacion.linea)) {
                compa = comparador();
                decomparacion.leerLinea();
            } else {
                decomparacion.leerLinea();
            }
        }
        
        if (compa != -1) {
            Palabra = (Palabra.substring(0,Palabra.length() - compa)).concat("*");
            decomparacion.cerrar();
            return (Palabra);
        } else {
        	decomparacion.cerrar();
            return (Palabra);
        }
    }

    public int comparador() {
        int cantLetras = decomparacion.linea.length();
        String termina = Palabra.substring(Palabra.length() - cantLetras, Palabra.length()); 
        if (termina.equals(decomparacion.linea)) {
            return(cantLetras);
        } else {
        return(-1);
        }
    }
}
