<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registro</title>
</head>
<body>
    <h2>Registrar nuevo usuario</h2>
    <form action="${pageContext.request.contextPath}/RegistroUsuarioServlet" method="post">
        <label>Nombre:</label>
        <input type="text" name="nombre" required><br>
        <label>Correo:</label>
        <input type="email" name="correo" required><br>
        <label>Contraseña:</label>
        <input type="password" name="contrasena" required><br>
        <label>Rol:</label>
        <select name="rol">
            <option value="admin">Admin</option>
            <option value="vendedor">Vendedor</option>
        </select><br>
        <button type="submit">Registrar</button>
    </form>

    <% 
        String registro = request.getParameter("registro");
        String error = request.getParameter("error");
        if ("ok".equals(registro)) {
    %>
        <p style="color:green;">Usuario registrado correctamente. Ahora puede iniciar sesión.</p>
    <% } else if ("bd".equals(error)) { %>
        <p style="color:red;">No se pudo guardar el usuario. Verifique la conexión y la base de datos.</p>
    <% } %>
</body>
</html>
