
public class AcumuladorDeBytesTransferidos {

	private Integer cantidadDeBytesTransferidos;
	
	private static AcumuladorDeBytesTransferidos acumulador;
	
	private static boolean instanciado = false;
	
	private AcumuladorDeBytesTransferidos(){
		this.cantidadDeBytesTransferidos = 0;
		AcumuladorDeBytesTransferidos.instanciado = true;
	}
	
	public static AcumuladorDeBytesTransferidos obtenerInstancia()
	{
		if (!instanciado) {
			AcumuladorDeBytesTransferidos.acumulador = new AcumuladorDeBytesTransferidos();
		}
		return AcumuladorDeBytesTransferidos.acumulador;
	}

	public Integer getCantidadDeBytesTransferidos() {
		return cantidadDeBytesTransferidos;
	}

	public void setCantidadDeBytesTransferidos(Integer cantidadDeBytesTransferidos) {
		this.cantidadDeBytesTransferidos = cantidadDeBytesTransferidos;
	}
	
	
	public void acumular(Integer cantidadDeBytes){
		this.cantidadDeBytesTransferidos += cantidadDeBytes;
	}
	
	public void reset(){
		this.cantidadDeBytesTransferidos = 0;
	}
}
