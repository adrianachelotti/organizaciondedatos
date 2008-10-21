/**
 * Clase que modela un archivo de consulta.
 *
 */
public class ArchivoConsulta extends Archivo {
   
	private String lineaConsulta;
    
    public ArchivoConsulta(String path,String linea) {
        super(path);
        lineaConsulta = linea;
    }

    protected void ProcesaLinea(String linea) {
        ListaTermino ter = new ListaTermino(linea);
        String[] terminos = ter.Procesa();
        AgregaTerminos(terminos);
    }

    protected void Procesa() {
        ProcesaLinea(this.lineaConsulta);
    }
    
    private void AgregaTerminos(String[] terminos){
		for(int j=0; j<terminos.length; j++)
			if(terminos[j]!="")
				super.Agregar(terminos[j]);
    }
}
