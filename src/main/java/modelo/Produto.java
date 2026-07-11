package modelo;

public record Produto(int id, String descricao, double preco, int estoque, int idCategoria) {
    @Override
    public String toString() {
        return String.format("ID: %-3d | Descrição: %-20s | Preço: R$ %-8.2f | Estoque: %-5d | Cat. ID: %d",
                id, descricao, preco, estoque, idCategoria);
    }
}
