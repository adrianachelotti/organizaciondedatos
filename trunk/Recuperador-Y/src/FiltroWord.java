import java.io.*;

/**
 *
 * Clase que modela el filtro de stop-words.
 * 
 */
public class FiltroWord extends Filtro {
    String Palabra;
    
    FiltroWord (String Path) {
        PathArchivo = Path;
    }
    
    public void CargarPalabra(String pal) {
        Palabra = pal;
    }
    
    public String Filtrar() throws FileNotFoundException, IOException {
        int compa = -1;
        // se crea nuevamente para que comienze a leer desde el principio.
        decomparacion = new Lector(PathArchivo);
        decomparacion.leerLinea();
        while ((decomparacion.linea != null) && (compa == -1)) {
            compa = comparador();
            decomparacion.leerLinea();
        }

        if (compa != -1) {
        	decomparacion.cerrar();
            return ("FILTRADO");
        } else {
        	decomparacion.cerrar();
            return (Palabra);
        }
    }

    public int comparador() {
        if (decomparacion.linea.equals(Palabra))
        {
            return (0);
        }
        else {
            return(-1);
        }
    }
    
    public boolean contieneNumeros(String pal) {
        if (pal.contains("0") || pal.contains("1") || pal.contains("2") || pal.contains("3") ||
            pal.contains("4") || pal.contains("5") || pal.contains("6") || pal.contains("7") ||
            pal.contains("8") || pal.contains("9") || pal.trim().equals("")) {
            return(true);
        }
        else {
            return (false);    
        }
    }
    
}
