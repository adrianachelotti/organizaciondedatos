import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Para leer datos de la entrada
 */
public class InputReader {

    private BufferedReader br;

    public InputReader() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    public String read() {
        try {
            return new String(br.readLine()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
            return "";
        }
    }
}
