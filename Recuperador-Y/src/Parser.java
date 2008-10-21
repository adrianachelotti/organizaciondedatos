import java.io.FileWriter;
import java.io.PrintWriter;

import javax.swing.text.html.*;
import javax.swing.text.*;

public class Parser extends HTMLEditorKit.ParserCallback {

	boolean body=false;
	
	public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
    }
	
    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
    	if ((t.toString().toLowerCase()=="body"))
    	    	body=true;
    }
    
    public void handleEndTag(HTML.Tag t, int pos) {
  	  if ((t.toString().toLowerCase()=="body"))
  	        	body=false;
  	}   
    
    public void handleText(char[] data, int pos) {
    	if(body)
    		Agregar(new String(data),"Auxiliar.txt");
    }
    
    private void Agregar(String texto, String path) {
		try {
            FileWriter archivo = new FileWriter(path,true);
	        PrintWriter pw = new PrintWriter(archivo);
            pw.println(texto);
            pw.close();
        } 
		catch (Exception e) {
            e.printStackTrace();
        }
	}
}

