package servlets;

import dao.VentaDAO;
import modelo.Venta;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/ventas")
// Expone las ventas y conserva nula la referencia si se elimina un cliente.
public class VentaApiServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		VentaDAO dao = new VentaDAO();
		List<Venta> ventas = dao.listarVentas();
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		PrintWriter out = response.getWriter();
		out.print("[");

		for (int i = 0; i < ventas.size(); i++) {
			Venta venta = ventas.get(i);

			out.print("{");
			out.print("\"id\":" + venta.getIdVenta() + ",");
			out.print("\"fecha\":" + jsonString(formatoFecha.format(venta.getFecha())) + ",");
			out.print("\"idCliente\":" + (venta.getIdCliente() > 0 ? venta.getIdCliente() : "null") + ",");
			out.print("\"idUsuario\":" + venta.getIdUsuario() + ",");
			out.print("\"total\":" + venta.getTotal());
			out.print("}");

			if (i < ventas.size() - 1) {
				out.print(",");
			}
		}

		out.print("]");
	}

	private String jsonString(String value) {
		if (value == null) {
			return "null";
		}
		return "\"" + value.replace("\\", "\\\\")
				.replace("\"", "\\\"")
				.replace("\n", "\\n")
				.replace("\r", "\\r") + "\"";
	}
}
