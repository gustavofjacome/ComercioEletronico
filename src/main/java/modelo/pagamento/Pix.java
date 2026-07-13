package modelo.pagamento;

import modelo.FormaPagamento;

public class Pix extends FormaPagamento {
    @Override
    public double calcularTaxa(double valorTotal) {
        return valorTotal * -0.05; // desconto de 5%, taxa negativa
    }

    @Override
    public String getDescricao() {
        return "Pix";
    }
}