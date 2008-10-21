import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

/**
 *
 * @author martin
 */
public class RegistroOut {

    private RandomAccessFile file;

    // Abrir el fichero
    public void abrir()
            throws IOException {
        file = new RandomAccessFile("textos/detrabajo/terminos.dat", "rw");
    }
    // Cerrar el fichero
    public void cerrar()
            throws IOException {
        file.close();
    }

    public void escribir(Registro reg) throws IOException {
        int longClave = reg.getClave().length();
        int CantReg = reg.getDocumentos().size();
        file.write(Utilidades.intToByteArray(longClave));
        file.write(Utilidades.stringToByteArray(reg.getClave()));
        file.write(Utilidades.intToByteArray(CantReg));
        Iterator it = reg.getDocumentos().iterator();
        while (it.hasNext()) {
            int a = (Integer) it.next();
            file.write(Utilidades.intToByteArray(a));
        }
    }
}
