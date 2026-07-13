package modelo;

public abstract class FormaPagamento {

    public abstract double calcularTaxa(double valorTotal);
    public abstract String getDescricao();
}
