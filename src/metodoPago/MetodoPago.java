package metodoPago;

public abstract class MetodoPago <T extends MedioDePago>{

    public final void procesarPago(Double monto, T medio) {
        validarDatos(monto, medio);
        reservarFondos(monto, medio);
        ejecutarTransaccion(monto, medio);
        notificarResultado(monto, medio);
    }

    protected abstract void validarDatos(Double monto, T medio);
    protected abstract void reservarFondos(Double monto, T medio);
    protected abstract void ejecutarTransaccion(Double monto, T medio);

    protected void notificarResultado(Double monto, T medio) {
        System.out.println("Registrando transacci�n....");
    }
}