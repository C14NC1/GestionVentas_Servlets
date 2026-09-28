package servlets;

import dao.ProductoDAO;
import modelo.Producto;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/productos")
public class ProductoApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        ProductoDAO dao = new ProductoDAO();

        List<Producto> productos = dao.listarProductos();

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        out.print("[");

        for (int i = 0; i < productos.size(); i++) {

            Producto p = productos.get(i);

            out.print("{");
            out.print("\"id\":" + p.getIdProducto() + ",");
            out.print("\"nombre\":" + jsonString(p.getNombre()) + ",");
            out.print("\"descripcion\":" + jsonString(p.getDescripcion()) + ",");
            out.print("\"precio\":" + p.getPrecio() + ",");
            out.print("\"stock\":" + p.getStock());
            out.print("}");

            if (i < productos.size() - 1) {
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