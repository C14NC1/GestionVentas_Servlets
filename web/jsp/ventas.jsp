<%@ page import="java.util.List" %>
<%@ page import="modelo.Venta" %>
<%@ page import="modelo.Producto" %>
<%@ page import="modelo.Cliente" %>
<%@ page import="modelo.Usuario" %>
<%@ page import="modelo.DetalleVenta" %>
<%
    List<Venta> ventas = (List<Venta>) request.getAttribute("ventas");
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("clientes");
    List<DetalleVenta> detalles = (List<DetalleVenta>) request.getAttribute("detalles");
    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
    Usuario usuario = (Usuario) session.getAttribute("usuario");

    String nombreCliente = "-";
    String nombreUsuario = "-";
    String nombreProducto = "-";
%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ventas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/estilos.css">
</head>
<body class="sale-page">
    <div class="wrap">
        <div class="topbar">
            <h2>Registro de ventas</h2>
            <a class="link" href="${pageContext.request.contextPath}/jsp/index.jsp">Volver al inicio</a>
        </div>

        <div class="admin-panel">
            <h3>Nueva venta</h3>
            <form class="admin-form" action="${pageContext.request.contextPath}/VentaServlet" method="post">
                <select name="id_cliente" required>
                    <option value="">Selecciona cliente</option>
                    <% if (clientes != null) { for (Cliente c : clientes) { %>
                        <option value="<%= c.getIdCliente() %>"><%= c.getNombre() %></option>
                    <% } } %>
                </select>

                <select name="id_producto" required>
                    <option value="">Selecciona producto</option>
                    <% if (productos != null) { for (Producto p : productos) { %>
                        <option value="<%= p.getIdProducto() %>"><%= p.getNombre() %> - $<%= String.format("%.2f", p.getPrecio()) %></option>
                    <% } } %>
                </select>

                <input type="number" name="cantidad" min="1" placeholder="Cantidad" required>
                <button type="submit" class="btn btn-primary">Guardar venta</button>
            </form>
        </div>

        <table class="data-table">
            <tr>
                <th>ID</th>
                <th>Fecha</th>
                <th>Cliente</th>
                <th>Vendedor</th>
                <th>Producto</th>
                <th>Cantidad</th>
                <th>Precio</th>
                <th>Total</th>
                <th>Acciones</th>
            </tr>
            <%
                if (ventas != null) {
                    for (Venta v : ventas) {
                        DetalleVenta detalleActual = null;
                        nombreCliente = "-";
                        nombreUsuario = "-";
                        nombreProducto = "-";
                        if (clientes != null && v.getIdCliente() > 0) {
                            for (Cliente c : clientes) {
                                if (c.getIdCliente() == v.getIdCliente()) {
                                    nombreCliente = c.getNombre();
                                    break;
                                }
                            }
                        }
                        if (usuarios != null) {
                            for (Usuario u : usuarios) {
                                if (u.getIdUsuario() == v.getIdUsuario()) {
                                    nombreUsuario = u.getNombre();
                                    break;
                                }
                            }
                        }
                        if (detalles != null) {
                            for (DetalleVenta d : detalles) {
                                if (d.getIdVenta() == v.getIdVenta()) {
                                    detalleActual = d;
                                    if (productos != null) {
                                        for (Producto p : productos) {
                                            if (p.getIdProducto() == d.getIdProducto()) {
                                                nombreProducto = p.getNombre();
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        }
            %>
            <tr>
                <td><%= v.getIdVenta() %></td>
                <td><%= v.getFecha() %></td>
                <td><%= nombreCliente %></td>
                <td><%= nombreUsuario %></td>
                <td><%= nombreProducto %></td>
                <td><%= detalleActual != null ? detalleActual.getCantidad() : "-" %></td>
                <td>$<%= detalleActual != null ? String.format("%.2f", detalleActual.getPrecioUnitario()) : "0.00" %></td>
                <td>$<%= String.format("%.2f", v.getTotal()) %></td>
                <td>
                    <% if (usuario != null && "admin".equalsIgnoreCase(usuario.getRol())) { %>
                    <div class="table-actions">
                        <form class="inline-form" action="${pageContext.request.contextPath}/VentaServlet" method="post">
                            <input type="hidden" name="accion" value="actualizar">
                            <input type="hidden" name="id_venta" value="<%= v.getIdVenta() %>">
                            <select name="id_cliente" required>
                                <% if (clientes != null) { for (Cliente c : clientes) { %>
                                    <option value="<%= c.getIdCliente() %>" <%= v.getIdCliente() > 0 && c.getIdCliente() == v.getIdCliente() ? "selected" : "" %>><%= c.getNombre() %></option>
                                <% } } %>
                            </select>
                            <select name="id_producto" required>
                                <% if (productos != null) { for (Producto p : productos) { %>
                                    <option value="<%= p.getIdProducto() %>" <%= detalleActual != null && p.getIdProducto() == detalleActual.getIdProducto() ? "selected" : "" %>><%= p.getNombre() %></option>
                                <% } } %>
                            </select>
                            <input type="number" name="cantidad" min="1" value="<%= detalleActual != null ? detalleActual.getCantidad() : 1 %>" required>
                            <button type="submit" class="btn btn-primary">Editar</button>
                        </form>

                        <form class="inline-form" action="${pageContext.request.contextPath}/VentaServlet" method="post">
                            <input type="hidden" name="accion" value="eliminar">
                            <input type="hidden" name="id_venta" value="<%= v.getIdVenta() %>">
                            <button type="submit" class="btn btn-danger">Eliminar</button>
                        </form>
                    </div>
                    <% } %>
                </td>
            </tr>
            <%
                    }
                }
            %>
        </table>
    </div>
</body>
</html>
