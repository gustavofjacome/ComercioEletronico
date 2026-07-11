package modelo;

import java.time.LocalDateTime;

public record Venda(int id, LocalDateTime date, boolean carrinho, double total, int idCliente) {
    @Override
    public String toString() {
        return String.format("ID: %-3d | Data: %-15s | Total: R$ %-8.2f | Cliente ID: %d",
                id, date.toString(), total, idCliente);
    }
}
