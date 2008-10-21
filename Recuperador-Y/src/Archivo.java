import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class Archivo {

    protected String pathEntrada;	//Archivo de Entrada
    protected String pathSalida;	//Archivo de Salida

    ///Constructor
    public Archivo(String pe, String ps) {
        this.pathEntrada = pe;
        this.pathSalida = ps;
    }

    public Archivo(String path) {
        this.pathEntrada = path;
        this.pathSalida = path;
    }

    //Metodos
    /**
     * Agrega en el archivo de Salida una nueva linea con el texto
     * Si el archivo no existe lo crea
     * @param texto
     */
    protected void Agregar(String texto, String path) {
        try {
            FileWriter archivo;
            if (path != null) {
                archivo = new FileWriter(path, true);
            } else {
                archivo = new FileWriter(this.pathSalida, true);
            }

            PrintWriter pw = new PrintWriter(archivo);
            pw.println(texto);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void Agregar(String texto) {
        this.Agregar(texto, null);
    }

    /**
     * Escribe una nueva linea en el archivo con el texto
     * Si el archivo existe lo sobreescribe
     * @param texto
     */
    protected void Escribir(String texto, String path) {
        try {
            FileWriter archivo;
            if (path != null) {
                archivo = new FileWriter(path);
            } else {
                archivo = new FileWriter(this.pathSalida);
            }

            PrintWriter pw = new PrintWriter(archivo);
            pw.println(texto);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void Escribir(String texto) {
        this.Escribir(texto, null);
    }

    /**
     * Lee el archivo de Entrada linea a linea 
     */
    protected void Leer() {
        File archivo = null;
        FileReader fr = null;
        try {
            archivo = new File(this.pathEntrada);
            String linea;
            fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            while ((linea = br.readLine()) != null) {
                this.ProcesaLinea(linea);
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    /**
     * @return  primer linea del archivo
     */
    protected String LeerLinea() {
        return this.LeerLinea(null);
    }

    protected String LeerLinea(String path) {
        File archivo = null;
        FileReader fr = null;
        String linea = null;
        try {
            if (path != null) {
                archivo = new File(path);
            } else {
                archivo = new File(this.pathEntrada);
            }

            fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            linea = br.readLine();
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return linea;
    }

    public boolean Borrar(String path) {
        File f = new File(path);
        return f.delete();
    }

    abstract protected void ProcesaLinea(String linea);

    abstract protected void Procesa();
}
