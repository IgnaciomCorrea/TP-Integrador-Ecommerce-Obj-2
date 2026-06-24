package metodoPago;

public class BilleteraVirtual extends MedioDePago{
	
	private Double saldo;
	
	public BilleteraVirtual(Double saldo) {

		this.saldo = saldo;
	}

	public Double getSaldo() {
		return saldo;
	}

}
