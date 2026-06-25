package sistema;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Catalogo.Catalogo;
import Catalogo.StockVendible;
import CriterioBusqueda.Criterio;
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
		//ya existe una validacion de esto
		if (pedido == null) {
			throw new IllegalArgumentException("El pedido no puede ser nulo");
		}

		pedidos.add(pedido);
		pedido.confirmarPedido();

		//validar si tiene el stock que el pedido solicita

		//caso todo ok
		pedido.pasarAEnviado();
		//caso que no
		pedido.cancelarPedido();

	}

	public List<Pedido> getPedidos() {
		return new ArrayList<>(pedidos);
	}
}