<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4efe9; margin: 0; color: #2b1d14; }
        .wrap { max-width: 420px; margin: 80px auto; background: white; padding: 30px; border-radius: 16px; box-shadow: 0 12px 22px rgba(0,0,0,0.08); }
        h2 { text-align: center; }
        form { display: flex; flex-direction: column; gap: 12px; }
        input, select { padding: 10px; border: 1px solid #d7c3ad; border-radius: 8px; }
        button { background: #8a4d26; color: white; border: none; padding: 12px; border-radius: 8px; font-weight: bold; cursor: pointer; }
        .info { margin-top: 16px; padding: 12px; border-radius: 8px; background: #fff8ef; border-left: 4px solid #c77b3a; }
        .link { display: block; text-align: center; margin-top: 18px; }
    </style>
</head>
<body>
    <div class="wrap">
        <h2>Iniciar sesión</h2>
        <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
            <label>Correo:</label>
            <input type="email" name="correo" required>
            <label>Contraseña:</label>
            <input type="password" name="contrasena" required>
            <button type="submit">Ingresar</button>
        </form>

        <div class="info">
            <strong>Roles:</strong><br>
            • Admin: puede gestionar precios, agregar, editar y eliminar productos; también puede agregar y eliminar clientes.<br>
            • Vendedor: solo puede consultar, pero no puede modificar productos ni clientes.
        </div>

        <% 
            String error = request.getParameter("error");
            if ("1".equals(error)) {
        %>
            <p style="color:red; margin-top: 15px;">Correo o contraseña incorrectos.</p>
        <% } else if ("login_required".equals(error)) { %>
            <p style="color:#8a4d26; margin-top: 15px;">Debes iniciar sesión para acceder a esta sección.</p>
        <% } %>

        <a class="link" href="${pageContext.request.contextPath}/jsp/registro.jsp">¿No tienes cuenta? Regístrate</a>
    </div>
</body>
</html>
