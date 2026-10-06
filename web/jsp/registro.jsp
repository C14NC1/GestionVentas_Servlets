<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registro</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f4efe9; margin: 0; color: #2b1d14; }
        .wrap { max-width: 420px; margin: 80px auto; background: white; padding: 30px; border-radius: 16px; box-shadow: 0 12px 22px rgba(0,0,0,0.08); }
        h2 { text-align: center; }
        form { display: flex; flex-direction: column; gap: 12px; }
        input, select { padding: 10px; border: 1px solid #d7c3ad; border-radius: 8px; }
        button { background: #c77b3a; color: white; border: none; padding: 12px; border-radius: 8px; font-weight: bold; cursor: pointer; }
        .info { margin-top: 16px; padding: 12px; border-radius: 8px; background: #fffaf5; border-left: 4px solid #8a4d26; }
        .link { display: block; text-align: center; margin-top: 18px; }
    </style>
</head>
<body>
    <div class="wrap">
        <h2>Registrar nuevo usuario</h2>
        <form action="${pageContext.request.contextPath}/RegistroUsuarioServlet" method="post">
            <label>Nombre:</label>
            <input type="text" name="nombre" required>
            <label>Correo:</label>
            <input type="email" name="correo" required>
            <label>Contraseña:</label>
            <input type="password" name="contrasena" required>
            <button type="submit">Registrar</button>
        </form>

        <div class="info">
            <strong>Importante:</strong><br>
            Las cuentas nuevas se registran con permisos de vendedor. La asignación de administrador debe realizarla una persona administradora del sistema.
        </div>

        <% 
            String registro = request.getParameter("registro");
            String error = request.getParameter("error");
            if ("ok".equals(registro)) {
        %>
            <p style="color:green; margin-top: 15px;">Usuario registrado correctamente. Ahora puede iniciar sesión.</p>
        <% } else if ("bd".equals(error)) { %>
            <p style="color:red; margin-top: 15px;">No se pudo guardar el usuario. Verifique la conexión y la base de datos.</p>
        <% } else if ("correo".equals(error)) { %>
            <p style="color:red; margin-top: 15px;">Ese correo ya está registrado. <a href="${pageContext.request.contextPath}/jsp/login.jsp">Inicia sesión aquí.</a></p>
        <% } else if ("datos".equals(error)) { %>
            <p style="color:red; margin-top: 15px;">Debe completar todos los datos del registro.</p>
        <% } %>

        <a class="link" href="${pageContext.request.contextPath}/jsp/login.jsp">Volver al inicio de sesión</a>
    </div>
</body>
</html>
