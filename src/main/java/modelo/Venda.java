package modelo;

import java.time.LocalDateTime;

public class Venda {
    private int id;
    private LocalDateTime date;
    private boolean carrinho;
    private double total;
    private int idCliente;


    public Venda() {
    }

    public Venda(int id, LocalDateTime date, boolean carrinho, double total, int idCliente) {
        this.id = id;
        this.date = date;
        this.carrinho = carrinho;
        this.total = total;
        this.idCliente = idCliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isCarrinho() {
        return carrinho;
    }

    public void setCarrinho(boolean carrinho) {
        this.carrinho = carrinho;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public String toString() {
        return "Venda{" +
                "id=" + id +
                ", date=" + date +
                ", carrinho=" + carrinho +
                ", total=" + total +
                ", idCliente=" + idCliente +
                '}';
    }
}

