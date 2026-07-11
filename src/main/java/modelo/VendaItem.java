package modelo;

public record VendaItem(int id, int qtd, double preco, int idVenda, int idProduto) {
    @Override
    public String toString() {
        return String.format("Item ID: %-3d | Venda ID: %-3d | Produto ID: %-3d | Qtd: %-3d | Preço: R$ %-8.2f",
                id, idVenda, idProduto, qtd, preco);
    }
}
