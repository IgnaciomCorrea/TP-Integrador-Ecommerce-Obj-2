package sistema;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Catalogo.Catalogo;
import Catalogo.StockVendible;
import CriterioBusqueda.Criterio;
import pedido.ObservadorStock;
import pedido.Pedido;

public class Sistema {

	private Catalogo catalogo;
	private ArrayList<Pedido> pedidos;

	public Sistema(Catalogo catalogo) {
		this.catalogo = catalogo;
		this.pedidos = new ArrayList<>();
	}

	public List<StockVendible> filtrarCon(Criterio criterio) {
		return catalogo.getStock().stream()
				.filter(stock -> criterio.validar(stock.getVendible()))
				.collect(Collectors.toList());
	}


	public void armarPedido(Pedido pedido) {
		if (pedido == null) {
			throw new IllegalArgumentException("El pedido no puede ser nulo");
		}

		pedido.agregarObservador(new ObservadorStock(catalogo));
		pedidos.add(pedido);

		if (catalogo.verificarStockPedido(pedido)) {
			pedido.confirmarPedido();
		} else {
			pedido.cancelarPedido();
		}
	}
}