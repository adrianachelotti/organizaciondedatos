
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class ParserBody extends HTMLEditorKit.ParserCallback {

    boolean body = false;
    int cantLienas = 0;

    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
    }

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        if ((t.toString().toLowerCase() == "body")) {
            body = true;
        }
    }

    public void handleEndTag(HTML.Tag t, int pos) {
        if ((t.toString().toLowerCase() == "body")) {
            body = false;
        }
    }

    public void handleText(char[] data, int pos) {
        if ((body)&&(this.cantLienas<3)) {
            this.cantLienas++;
            sacarBodyPantalla(new String(data));
        }
    }

    private void sacarBodyPantalla(String texto) {
        System.out.println("linea: "+texto);
    }
    
}