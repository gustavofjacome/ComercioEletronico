package modelo;

public class VendaItem {
    private int id;
    private int qtd;
    private double preco;
    private int idVenda;
    private int idProduto;

    public VendaItem() {
    }

    public VendaItem(int id, int qtd, double preco) {
        this.id = id;
        this.qtd = qtd;
        this.preco = preco;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    @Override
    public String toString() {
        return String.format("Item ID: %-3d | Venda ID: %-3d | Produto ID: %-3d | Qtd: %-3d | Preço: R$ %-8.2f",
                id, idVenda, idProduto, qtd, preco);
    }
}
