package modelo.pagamento;

import modelo.FormaPagamento;

public class Boleto implements FormaPagamento {
    @Override
    public double calcularTaxa(double valorTotal) {
        return 0;
    }

    @Override
    public String getDescricao() {
        return "Boleto";
    }
}
