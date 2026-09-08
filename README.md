 # Gestión de Ventas

Proyecto académico de gestión de ventas desarrollado con Java, Servlets, JSP y MySQL.

## Funcionalidades

- Registro e inicio de sesión de usuarios.
- Gestión y consulta de clientes.
- Gestión y consulta de productos.
- Registro y consulta de ventas.
- Persistencia de información mediante MySQL y JDBC.

## Tecnologías

- Java y Java Servlets.
- JSP para la interfaz web.
- Apache Tomcat 9.
- MySQL.
- MySQL Connector/J.

## Estructura

- `src/modelo`: entidades del sistema.
- `src/dao`: conexión y operaciones con la base de datos.
- `src/servlets`: controladores de la aplicación.
- `web/jsp`: vistas web.
- `SQL.sql`: creación de la base de datos y sus tablas.
- `lib`: dependencias externas.

## Ejecución

Se requiere tener configurados MySQL y Apache Tomcat 9. La base de datos se crea ejecutando `SQL.sql` y las credenciales se configuran en `src/dao/Conexion.java`. Luego se compilan las clases, se despliega la aplicación en `webapps/GestionVentas` y se inicia Tomcat.

Aplicación:

```text
http://localhost:8080/GestionVentas/
```

Login:

```text
http://localhost:8080/GestionVentas/jsp/login.jsp
```
