package reportes;

import Catalogo.Producto;
import Catalogo.Paquete;

public interface ReportVisitor {
    void visitProducto(Producto producto, int cantidadVendida, double precioTotal);
    void visitPaquete(Paquete paquete, int cantidadVendida, double precioTotal);
}