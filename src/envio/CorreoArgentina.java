package envio;

import direccion.Direccion;

public interface CorreoArgentina {
    float estimarEnvio(float peso, Direccion direccion);
}