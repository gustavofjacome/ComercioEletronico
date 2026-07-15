package modelo.pagamento;

import modelo.FormaPagamento;

public class Pix implements FormaPagamento {
    @Override
    public double calcularTaxa(double valorTotal) {
        return valorTotal * -0.05;
    }

    @Override
    public String getDescricao() {
        return "Pix";
    }
}
