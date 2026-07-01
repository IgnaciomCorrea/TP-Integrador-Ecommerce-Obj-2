package direccion;

public class Direccion {
    private String calle;
    private int numero;
    private String ciudad;
    private String codigoPostal;

    public Direccion(String calle, int numero, String ciudad, String codigoPostal) {
        this.calle = calle;
        this.numero = numero;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
    }

    public String getCalle() { return calle; }
    public int getNumero() { return numero; }
    public String getCiudad() { return ciudad; }
    public String getCodigoPostal() { return codigoPostal; }
}