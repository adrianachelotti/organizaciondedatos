import java.io.*;
/**
 *
 *Clase que modela el filtro de una frase.
 *
 */
public class FiltroFrase extends Filtro {
    
    String Comparador[] = new String[5];
    
    
    FiltroFrase(ArchivoTerminos archivo, String Path) throws FileNotFoundException {
        this.terminos =  archivo;
        PathArchivo = Path;
    }
    
   
    public String Filtrar() throws FileNotFoundException, IOException {
        String palRet;
        int compa = -1;
        // se crea nuevamente para que comienze a leer desde el principio.
        decomparacion = new Lector(PathArchivo);
        decomparacion.leerLinea();
        while ((decomparacion.linea != null) && (compa==-1)) {
            CargaComparador(decomparacion.linea,0);
            compa = comparador();
            decomparacion.leerLinea();
        }
        
        if (compa!=-1){
            ReordenarTubo(compa);
            decomparacion.cerrar();
            return("FILTRADO");
        }
        else {
            palRet = tubo[0];
            ReordenarTubo(1);
            decomparacion.cerrar();
            return(palRet);
        }
    }
    
    public int comparador(){
        int i = 0;
        boolean ok = true;
        while ((Comparador[i]!= null) && (ok == true)) {
            //para que no compare con null y pinche
            if (tubo[i]!= null) {
                if (tubo[i].equals(Comparador[i])){
                    i++;
                }
                else {
                    ok = false;
                    i++;
                }
            }
            else {
                   //para poder salir del while ya que termino el texto.
                   vaciarComparador();
                   ok = false;
            }
        }
        if (ok) {
            return (i);
        }
        else {
            return (-1);
        }
    }
    
    public void CargaComparador(String lin,int vuelta) {
        //limpiamos el comparador la primera vuelta nada mas
        if (vuelta == 0) {
            vaciarComparador();
        }
        
        String pal;
        String resto;
        int pos = lin.indexOf(' ');
        //si encuentra un espacio, parte la frase en dos.
        if (pos!=-1) {
             pal = lin.substring(0,pos);
             resto = lin.substring(pos+1);
             Comparador[vuelta] = pal;
             vuelta++;
             //se vuelve a llamar para seguir partiendo la palabra
             CargaComparador(resto,vuelta);
        }
        else {
            Comparador[vuelta] = lin;
        }
    }
    
    public void vaciarComparador() {
        for (int i = 0; i < Comparador.length ; i++) {
            Comparador[i] = null;
        }
    }
}
