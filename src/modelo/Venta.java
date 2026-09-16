package modelo;

import java.util.Date;

// Representa la información principal de una venta
public class Venta {
    private int idVenta;
    private Date fecha;
    private int idCliente;
    private int idUsuario;
    private double total;

    public Venta() {}

    // Crear una venta asociada a un cliente y usuario
    public Venta(int idVenta, Date fecha, int idCliente, int idUsuario, double total) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.idUsuario = idUsuario;
        this.total = total;
    }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
