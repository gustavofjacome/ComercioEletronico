package modelo.pagamento;

import modelo.FormaPagamento;

public class Cartao extends FormaPagamento {
    private int parcelas;

    public Cartao(int parcelas) {
        this.parcelas = parcelas;
    }

    @Override
    public double calcularTaxa(double valorTotal) {
        return valorTotal * (0.02 * parcelas);
    }

    @Override
    public String getDescricao() {
        return "Cartão (" + parcelas + "x)";
    }

    public int getParcelas() { return parcelas; }
}