package sistema;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Catalogo.Catalogo;
import Catalogo.StockVendible;
import CriterioBusqueda.Criterio;
import envio.MetodoEnvio;
import exceptions.PedidoExcepcion;
import metodoPago.*;
import pedido.ObservadorStock;
import notificaciones.GeneradorFactura;
import pedido.Pedido;
import reportes.ReporteProductosMasVendidos;
import Catalogo.ItemVendible;

public class Sistema {

	private Catalogo catalogo;
	private ArrayList<Pedido> pedidos;
	private GeneradorFactura generadorFactura;

	public Sistema(Catalogo catalogo) {
		this.catalogo = catalogo;
		this.pedidos = new ArrayList<>();
		this.generadorFactura = new GeneradorFactura();
	}

	public List<StockVendible> filtrarCon(Criterio criterio) {
		return catalogo.getStock().stream()
				.filter(stock -> criterio.validar(stock.getVendible()))
				.collect(Collectors.toList());
	}


	public void armarPedido(Pedido pedido){
		if (pedido == null) {
			throw new PedidoExcepcion("El pedido no puede ser nulo");
		}

		pedido.agregarObservador(new ObservadorStock(catalogo));
		pedido.agregarObservador(generadorFactura);
		pedidos.add(pedido);


		if (catalogo.verificarStockPedido(pedido)) {
			pedido.confirmarPedido();
		} else {
			pedido.cancelarPedido();
		}
	}

	public ReporteProductosMasVendidos generarReporteProductosMasVendidos(LocalDate inicio, LocalDate fin) {
		ReporteProductosMasVendidos visitor = new ReporteProductosMasVendidos(inicio, fin);
		for (Pedido pedido : pedidos) {
			if (pedido.getEstado().esEntregado() && estaDentroDelPeriodo(pedido.getFecha(), inicio, fin)) {
				for (ItemVendible item : pedido.getVendibles()) {
					item.accept(visitor);
				}
			}
		}
		return visitor;
	}
	private boolean estaDentroDelPeriodo(LocalDate fecha, LocalDate inicio, LocalDate fin) {
		return !fecha.isBefore(inicio) && !fecha.isAfter(fin);
	}

	public void setMetodoPagoPedido(Pedido pedido, MetodoPago<?> metodoPago, MedioDePago medioDePago) {
		pedido.setMetodoDePago(metodoPago, medioDePago);
	}

	public void setMetodoEnvioPedido(Pedido pedido, MetodoEnvio metodoEnvio){
		pedido.setMetodoDeEnvio(metodoEnvio);
	}

	public ArrayList<Pedido> getPedidos() {
		return pedidos;
	}
}