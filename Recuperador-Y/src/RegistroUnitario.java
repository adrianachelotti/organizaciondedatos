/**
 *
 * Clase que modela un registro.
 * 
 */
public class RegistroUnitario implements Comparable<RegistroUnitario> {
    String termino;
    int numeroDeDocumento;
    
    RegistroUnitario (String ter, int numDoc) {
        termino = ter;
        numeroDeDocumento = numDoc;
    }
    
    int getNumeroDeDocumento () {
        return numeroDeDocumento;
    }
    
    String getTermino () {
        return termino;
    }
    
    public String toString() {
        return (termino + " (" + numeroDeDocumento + ")");
    }
    
    public void setNumeroDeDocumento(int num) {
        numeroDeDocumento = num;
    }
    
    public int compareTo(RegistroUnitario r1) {
                
        return termino.compareTo(r1.getTermino());
    }
}
