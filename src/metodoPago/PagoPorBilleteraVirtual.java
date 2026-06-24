package metodoPago;

public class PagoPorBilleteraVirtual extends MetodoPago<BilleteraVirtual>{

    protected void validarDatos(Double monto, BilleteraVirtual medio) {
    	ApiBilleteraVirtual.validarDatos(monto, medio.getSaldo());
    }
    
    protected void reservarFondos(Double monto, BilleteraVirtual medio) {
    	ApiBilleteraVirtual.reservarFondos(monto, medio.getSaldo());
    }
    
    protected void ejecutarTransaccion(Double monto, BilleteraVirtual medio) {
    	ApiBilleteraVirtual.ejecutarTransaccion(monto, medio.getSaldo());
    }
	
}
