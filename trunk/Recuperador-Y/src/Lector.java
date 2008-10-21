import java.io.*;
/**
 *
 * Clase que modela un lector de datos.
 * 
 */

public class Lector {
    
    int numDoc;
    String linea;
    BufferedReader Archivo;
    
    Lector(String Path) throws FileNotFoundException{
        File f = new File( Path );
        Archivo = new BufferedReader(new FileReader( f ));
    }
    
    public void leerNDoc() throws FileNotFoundException, IOException{
        numDoc = Integer.parseInt(Archivo.readLine());
    }
    
    public void leerLinea() throws FileNotFoundException, IOException{
        linea = Archivo.readLine();
    }
    
    public void cerrar(){
    	try {
			this.Archivo.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
    }
}
