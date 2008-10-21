import java.io.*;
import java.util.*;

/**
 *
 * @author martin
 */
public class OrdenadorDeTerminos {

    List<RegistroUnitario> listaTerminos;

    OrdenadorDeTerminos() {
        listaTerminos = new ArrayList<RegistroUnitario>();
    }

    public void ordenar() {
        Collections.sort(listaTerminos);
    }

    public void addTermino(RegistroUnitario r) {
        listaTerminos.add(r);
    }
    
    public void addNumDoc (RegistroUnitario reg0, List<Integer> listaD) {
        if (listaD.size()!=0) {
            if (listaD.get(listaD.size()-1).hashCode() != reg0.getNumeroDeDocumento()) { 
                listaD.add(reg0.getNumeroDeDocumento());
            }
        }else {
            listaD.add(reg0.numeroDeDocumento);
        }
    }
    
    public void sacarPorPantalla () throws IOException, ClassNotFoundException {
        Registro reg ;
        RegistroIn entrada;
        entrada = new RegistroIn();
        entrada.abrir();
        do {
            reg = entrada.leer();
            if (reg!=null) {
            System.out.println(reg);
            }
        } while (reg != null);
        entrada.cerrar();
    }
    
    public void agrupar() throws IOException {
        int i = 0;
        List<Integer> listaD = new ArrayList<Integer>();
        RegistroUnitario reg1 = new RegistroUnitario(" ", 0);
        RegistroUnitario reg0 = new RegistroUnitario(" ", 0);
        Registro regnuevo1 = new Registro(" ",0);
        RegistroOut salida = new RegistroOut();
        salida.abrir();
        
        for (i = 0; i < listaTerminos.size(); i++) {
            reg0 = (RegistroUnitario) listaTerminos.get(i);
            //tomo el registro siguiente para compara, teniendo en cuenta el final de arreglo
            if (i != (listaTerminos.size() - 1)) {
                reg1 = (RegistroUnitario) listaTerminos.get(i + 1);
            } else {
                reg1 = new RegistroUnitario(" ", 0);
            }
            if (reg0.getTermino().equals(reg1.getTermino())) {
                this.addNumDoc(reg0, listaD);
                
            } else {
            	this.addNumDoc(reg0, listaD);
                regnuevo1 = new Registro(reg0.getTermino(),0);
                regnuevo1.agregarDocumentos(listaD);
                salida.escribir(regnuevo1);
                listaD.clear();
            }
        }
        
        salida.cerrar();
    }


}
