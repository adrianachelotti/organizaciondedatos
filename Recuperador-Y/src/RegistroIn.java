import java.io.*;
import java.util.*;

/**
 *
 * @author martin
 */
public class RegistroIn {

    private RandomAccessFile file;

    public void abrir()
            throws IOException {
        file = new RandomAccessFile("textos/detrabajo/terminos.dat", "rw");
    }
    
    public void eliminar() {
        File f = new File("textos/detrabajo/terminos.dat");
        f.delete();
    }
    
    public void cerrar()
            throws IOException {
        file.close();
    }

    public Registro leer()
            throws IOException, ClassNotFoundException {
        Registro reg = new Registro(" ", 0);
        int longTerm = 0;
        String term = "";
        int CantDocu = 0;
        int Docu = 0;
        List<Integer> listaD = new ArrayList<Integer>();

        byte[] numero = new byte[4];
        file.read(numero);
        longTerm = Utilidades.byteArrayToInt(numero, 0);
        byte[] termb = new byte[longTerm];
        file.read(termb);
        term = Utilidades.byteArrayToString(termb, 0, longTerm);
        reg.setClave(term);
        file.read(numero);
        CantDocu = Utilidades.byteArrayToInt(numero, 0);

        for (int i = 0; i < CantDocu; i++) {
            file.read(numero);
            Docu = Utilidades.byteArrayToInt(numero, 0);
            listaD.add(Integer.valueOf(Docu));
        }
        
        reg.setDocumentos(listaD);
        if (reg.getClave().equals("")) {
            return null;
        } else {
            return reg;
        }

    }
}
