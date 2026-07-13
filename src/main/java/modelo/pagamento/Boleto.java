package modelo.pagamento;

import modelo.FormaPagamento;

public class Boleto extends FormaPagamento {
    @Override
    public double calcularTaxa(double valorTotal) {
        return 0; // sem taxa nem desconto
    }

    @Override
    public String getDescricao() {
        return "Boleto";
    }
}