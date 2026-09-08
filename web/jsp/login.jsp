<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
    <h2>Iniciar sesión</h2>
    <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
        <label>Correo:</label>
        <input type="text" name="correo" required><br>
        <label>Contraseña:</label>
        <input type="password" name="contrasena" required><br>
        <button type="submit">Ingresar</button>
    </form>

    <% 
        String error = request.getParameter("error");
        if ("1".equals(error)) {
    %>
        <p style="color:red;">Correo o contraseña incorrectos.</p>
    <% } %>
</body>
</html>
