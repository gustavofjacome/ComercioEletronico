package modelo;

public interface FormaPagamento {
    double calcularTaxa(double valorTotal);
    String getDescricao();
}
