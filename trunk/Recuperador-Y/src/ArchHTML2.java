
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.text.html.parser.ParserDelegator;

public class ArchHTML2 extends Archivo {

    String auxiliar = "Auxiliar.txt";

    public ArchHTML2(String pe) {
        super(pe);
        super.Borrar(auxiliar);
    }

    protected void ProcesaLinea(String linea) {
    // TODO Auto-generated method stub

    }

    public void Procesa() {
        // TODO Auto-generated method stub
        try {


            FileInputStream fileInputStream = new FileInputStream(new File(this.pathEntrada));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "ISO-8859-1");
                
            Parser callback = new Parser();
            ParserDelegator pd = new ParserDelegator();
            pd.parse(inputStreamReader, callback, true);
        } catch (Exception e) {
        }
    }
}

