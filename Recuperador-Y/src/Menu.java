/**
 *Clase que modela el menu de la aplicacion.
 * 
 */
public class Menu {
    
    public Menu(){
        
    }
    
    public String mostrarMenu(){
        String op = "";
        InputReader imput = new InputReader();
        System.out.println("MENU");
        System.out.println("----");
        System.out.println("1 - Filtrar Archivo HTML");
        System.out.println("2 - Buscar");
        System.out.println("3 - Mostrar arbol");
        System.out.println("4 - Limpiar archivos.");
        System.out.println("5 - Finalizar.");
        System.out.print("Elegir opcion (1-2-3-4-5): ");
        op = imput.read();
        
        if ((!op.equals("1")) && (!op.equals("2")) && (!op.equals("3"))&& (!op.equals("4")) &&(!op.equals("5"))){
            System.out.println("Opcion no valida");
            System.out.println();
            op = mostrarMenu();
        }
        return(op);
    }
    
    public String tomarConsulta(){
        String consul = null;
        System.out.print("Escriba la consulta: ");
        InputReader imput = new InputReader();
        consul = imput.read();
        return(consul);
    }
    
    private boolean esPotenciaDeDos(int numero) {
        double cantAux = (Math.log(numero) / Math.log(2));
        //le saco la parte entera 
        int cant = (int) cantAux / 1;

        int potencia = 1;
        for (int i = 1; i <= cant; i++) {
            potencia = potencia * 2;
        }
        if (potencia == numero) {
            return true;
        }
        return false;

    }

    public int tomarTamanio() {
         System.out.print("Ingrese Tamanio de Nodo: ");
        boolean negativo = false;
        int tam = 0;
        InputReader imput = new InputReader();
        try {
        tam = Integer.parseInt(imput.read());
        } catch (Exception e) {
            negativo = true;
        }
        double resto = tam % 512;
        boolean resultado = esPotenciaDeDos(tam);
        if ((resto != 0) || (!resultado) || (negativo)) {
            System.out.println("Tamanio no valido.Ingreselo nuevamente.");
            tam = tomarTamanio();
        }
        return(tam);
    }
}
