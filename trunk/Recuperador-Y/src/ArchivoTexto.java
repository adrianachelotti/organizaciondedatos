import java.io.*;

/**
 *Clase que modela un archivo de texto.
 */
public class ArchivoTexto {
    FileWriter arc;
    BufferedWriter bw;
    PrintWriter salida;
    
    ArchivoTexto (String Path) throws IOException {    
        arc = new FileWriter(Path);
        bw = new BufferedWriter(arc);
        salida = new PrintWriter(bw);   
    }
    
    public void escribir(String pal) {
        salida.println(pal);
    }
    
    public void cerrarArchivo() {
        salida.close();
    }
}
