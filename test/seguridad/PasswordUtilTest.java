package seguridad;

public class PasswordUtilTest {
    public static void main(String[] args) {
        String firstHash = PasswordUtil.hash("Clave-de-prueba-123");
        String secondHash = PasswordUtil.hash("Clave-de-prueba-123");

        check(!firstHash.equals("Clave-de-prueba-123"), "La contraseña no debe almacenarse en texto plano");
        check(firstHash.length() <= 100, "El hash debe caber en la columna contraseña del esquema actual");
        check(!firstHash.equals(secondHash), "Cada hash debe usar una sal distinta");
        check(PasswordUtil.verify("Clave-de-prueba-123", firstHash), "Debe aceptar la contraseña correcta");
        check(!PasswordUtil.verify("otra-clave", firstHash), "Debe rechazar una contraseña incorrecta");
        check(!PasswordUtil.verify("Clave-de-prueba-123", "pbkdf2$invalid"), "Debe rechazar hashes malformados");
        check(!PasswordUtil.isEncoded("Clave-de-prueba-123"), "Debe identificar contraseñas heredadas sin hash");

        System.out.println("PasswordUtilTest: OK");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}