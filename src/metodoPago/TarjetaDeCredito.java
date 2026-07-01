package metodoPago;

import java.util.Date;

public class TarjetaDeCredito extends MedioDePago{
	
	private int nroTarjeta;
	private int cvv;
	private Date fechaVencimiento;
	
	public TarjetaDeCredito(int nroTarjeta, int cvv, Date fechaVencimiento) {
		this.nroTarjeta = nroTarjeta;
		this.cvv = cvv;
		this.fechaVencimiento = fechaVencimiento;
	}

	public int getCvv() {
		return cvv;
	}

	public int getNroTarjeta() {
		return nroTarjeta;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
		
}
 