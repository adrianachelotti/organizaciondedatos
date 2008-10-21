
import java.io.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.html.parser.ParserDelegator;
/**
 *
 * @author martin
 */
public class ImpresorTituloBody {

    private String archivo;
    FileInputStream fileInputStream = null;
    
    ImpresorTituloBody(String path) {
        this.archivo = path;
   
    }

    public void imprimirPorPantalla() {
        {
            InputStreamReader inputStreamReader = null;
            try {
                fileInputStream = new FileInputStream(this.archivo);
                inputStreamReader = new InputStreamReader(fileInputStream, "ISO-8859-1");

                ParserTitle callback = new ParserTitle();
                ParserDelegator pd = new ParserDelegator();
                pd.parse(inputStreamReader, callback, true);

                fileInputStream = new FileInputStream(this.archivo);
                inputStreamReader = new InputStreamReader(fileInputStream, "ISO-8859-1");

                ParserBody callback1 = new ParserBody();
                pd = new ParserDelegator();
                pd.parse(inputStreamReader, callback1, true);
                System.out.println();
            } catch (IOException ex) {
                Logger.getLogger(ImpresorTituloBody.class.getName()).log(Level.SEVERE, null, ex);
            } 
            finally {
                try {
                    inputStreamReader.close();
                } catch (IOException ex) {
                    Logger.getLogger(ImpresorTituloBody.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
