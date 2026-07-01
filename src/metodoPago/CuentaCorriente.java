package metodoPago;

public class CuentaCorriente extends MedioDePago{
	
	private int cbu;
	private String alias;
	
	public int getCbu() {
		return cbu;
	}
	public String getAlias() {
		return alias;
	}

	public CuentaCorriente(int cbu, String alias) {
		this.cbu = cbu;
		this.alias = alias;
	}
}
