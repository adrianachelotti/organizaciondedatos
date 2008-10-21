import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author martin
 */
public class ArchivoPath {

    private RandomAccessFile archivoPath;
    private final int TAMANIO = 100;
    
    public ArchivoPath (String nombre) {
        try {
            this.archivoPath = new RandomAccessFile(nombre,"rw");
        }
        catch (Exception e ){
            System.out.println("No se pudo crear el archivo " + nombre);
        }
    }    
    
    public void agregarPath(String url) {
        try {
            byte[] cadena = new byte[TAMANIO];
            byte[] cadenaBloque = new byte[TAMANIO];
            cadena = Utilidades.stringToByteArray(url);
            Utilidades.copyByteArrayInByteArray(cadenaBloque, cadena, 0, cadena.length);
            int tamanioArchivo = (int) this.archivoPath.length();
            this.archivoPath.seek(tamanioArchivo);
            this.archivoPath.write(cadenaBloque);
            
        } catch (IOException ex) {
            Logger.getLogger(ArchivoPath.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public int getIndice () {
        try {
            int tam = (int) (this.archivoPath.length() / TAMANIO);
            return tam;
        } catch (IOException ex) {
            Logger.getLogger(ArchivoPath.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public String recuperarPath(int numeroDeDocumento) {
        String path = null;
        try {
            byte[] cadena = new byte[TAMANIO];
            int posicion = (numeroDeDocumento - 1) * TAMANIO;
            if (posicion < this.archivoPath.length()) {
                this.archivoPath.seek(posicion);
                this.archivoPath.read(cadena);
                path = Utilidades.byteArrayToString(cadena, 0, TAMANIO);
            }
        } catch (IOException ex) {
            Logger.getLogger(ArchivoPath.class.getName()).log(Level.SEVERE, null, ex);
            return(path);
        }
        return(path);
    }
    
    public void cerrar() {
        try {
            this.archivoPath.close();
        } catch (IOException ex) {
            Logger.getLogger(ArchivoPath.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
