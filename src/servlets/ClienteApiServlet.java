package servlets;

import dao.ClienteDAO;
import modelo.Cliente;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/clientes")
// Lista los clientes como JSON para consultas externas.
public class ClienteApiServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		ClienteDAO dao = new ClienteDAO();
		List<Cliente> clientes = dao.listarClientes();

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		PrintWriter out = response.getWriter();
		out.print("[");

		for (int i = 0; i < clientes.size(); i++) {
			Cliente cliente = clientes.get(i);

			out.print("{");
			out.print("\"id\":" + cliente.getIdCliente() + ",");
			out.print("\"nombre\":" + jsonString(cliente.getNombre()) + ",");
			out.print("\"correo\":" + jsonString(cliente.getCorreo()) + ",");
			out.print("\"telefono\":" + jsonString(cliente.getTelefono()) + ",");
			out.print("\"direccion\":" + jsonString(cliente.getDireccion()));
			out.print("}");

			if (i < clientes.size() - 1) {
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
