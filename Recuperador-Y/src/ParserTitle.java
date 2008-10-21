
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class ParserTitle extends HTMLEditorKit.ParserCallback {

    boolean title = false;

    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
    }

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        if ((t.toString().toLowerCase() == "title")) {
            title = true;
        }
    }

    public void handleEndTag(HTML.Tag t, int pos) {
        if ((t.toString().toLowerCase() == "title")) {
            title = false;
        }
    }

    public void handleText(char[] data, int pos) {
        if (title) {
            sacarTituloPorPantalla(new String(data));
        }
    }

    private void sacarTituloPorPantalla(String texto) {
        try {
            System.out.println("Titulo: " + texto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}