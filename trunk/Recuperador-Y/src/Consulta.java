import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Clase que modela una consulta.
 * 
 **/
public class Consulta {

    private List<String> terminos = new ArrayList<String>();
    private String lineaAConsultar;

    public Consulta(String linea) {
        this.lineaAConsultar = linea;
    }

    public void Filtrar() throws FileNotFoundException, IOException, ClassNotFoundException {
        ArchivoTerminos consulta = new ArchivoTerminos();
        consulta.ProcesaLinea(this.lineaAConsultar);
        ProcesoFiltrado f1 = new ProcesoFiltrado();
        f1.filtrar(consulta);
        OrdenadorDeTerminos or = f1.getOrdenador();
        for (int i=0 ; i<or.listaTerminos.size(); i++) {
            RegistroUnitario u = (RegistroUnitario) or.listaTerminos.get(i);
            this.terminos.add(u.termino);
        }
    }
    
    public void MostrarConsulta() {
        System.out.print("\nTerminos a buscar:");
    	System.out.println(this.terminos + "\n");
    }
    
    public List<String> getTerminos() {
        return (this.terminos);
    }
}


