package reportes;

import Catalogo.Producto;
import Catalogo.Paquete;
import Catalogo.Vendible;

public interface ReportVisitor {
    void visit(Vendible vendible, int cantidadVendida, double precioTotal);
}