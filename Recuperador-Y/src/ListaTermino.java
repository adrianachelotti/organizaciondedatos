
import java.util.HashMap;
import java.util.Iterator;

public class ListaTermino {
    //	Contiene una lista de terminos separados por '|'
    private String lista;
    private String[] terminos;
    int contador;
    //	Diccionario
    HashMap<String, String> global = new HashMap<String, String>();
    //	Lista de simbolos criticos
    String diacriticos = "  � ^ > < . : , ; - _ ! / | \\ ( ) [ ] { } + $ ~ ` * ? # = & % @ © » ' /t ° O/ " + (char) 0x0022 + " " + (char) 0x00BF + " ";

    //Constructor
    public ListaTermino(String l) {
        this.lista = l;
        this.CargarDic();
    }

    private String PasarMinuscula(String texto) {
        return texto.toLowerCase();
    }

    private void CargarDic() {

        // Insertamos valores "key"-"value" al HashMap
    	 global.put("a", "a|á|à|ä");
         global.put("e", "e|é|è|ë");
         global.put("i", "i|í|ì|ï");
         global.put("o", "o|ó|ò|ö");
         global.put("u", "u|ú|ù|ü");
         global.put("n", "ñ");
    }

    private String Elimina_Acentos(String texto) {

        for (Iterator it = global.keySet().iterator(); it.hasNext();) {
            String s = (String) it.next();
            String s1 = (String) global.get(s);
            String[] cadena = s1.split("|");
            for (int j = 1; j < cadena.length; j++) {
                if (cadena[j].charAt(0) != '|') {
                    texto = texto.replace(cadena[j].charAt(0), s.charAt(0));
                }
            }
        }
        return texto;
    }

    private String Elimina_Criticos(String texto) {
        String[] cadena = diacriticos.split(" ");
        for (int j = 0; j < cadena.length; j++) {
            if (cadena[j] != "") {
                texto = texto.replace(cadena[j].charAt(0), ' ');
            }
        }
        return texto;
    }

    private String[] getTerminos() {
        try {
            return lista.split(" ");
        } catch (Exception e) {
            return null;
        }
    }

    public String[] Procesa() {
        terminos = getTerminos();
        try {
            if (terminos != null) {
                for (int j = 0; j < terminos.length; j++) {
                    terminos[j] = PasarMinuscula(terminos[j]);
                    terminos[j] = Elimina_Acentos(terminos[j]);
                    terminos[j] = Elimina_Criticos(terminos[j]);
                    //
                    terminos[j] = terminos[j].replaceAll(" ", "");
                }
            }
        } catch (Exception e) {

        }
        return terminos;
    }

    public String getTermino() {
        String termino;
        termino = terminos[contador];
        contador++;

        return (termino);
    }
}
