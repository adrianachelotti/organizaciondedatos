import java.io.*;

/**
 *Clase que modela un filtro.
 * 
 */
public abstract class Filtro {
    String tubo[] = new String[5];
    Lector decomparacion;
    Lector detexto;
    String PathArchivo;
    ArchivoTerminos terminos;
    
    Filtro () {
        
    }
    
    public void CargarTubo() throws FileNotFoundException, IOException {
        for (int i = 0; i < tubo.length  ;i++){
            tubo[i] = terminos.getTermino();
        }
    }
    
    public void ReordenarTubo(int posAord) throws FileNotFoundException, IOException {
        for (int i = 0; i < (tubo.length - posAord) ; i++) {
            tubo[i] = tubo [i+posAord];
        }
        for (int i=0; i < posAord ; i++){
            tubo[i+tubo.length-posAord] = terminos.getTermino();
        }
    }
    
    abstract public String Filtrar() throws FileNotFoundException, IOException;
    
    abstract public int comparador();
}