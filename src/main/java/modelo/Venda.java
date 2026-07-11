package modelo;

import java.time.LocalDateTime;

/**
 * Representa a entidade Venda no domínio do sistema de comércio eletrônico.
 * <p>Esta classe atua como um modelo de dados (POJO - Plain Old Java Object)
 * que encapsula as informações referentes a uma transação comercial. Ela foi desenhada
 * para representar tanto um "carrinho de compras" ativo (em andamento) quanto um
 * pedido já processado e finalizado, dependendo do estado do seu atributo de controle.</p>
 * @see dao.VendaDAO
 * @see modelo.Cliente
 * @see modelo.VendaItem
 * @see java.time.LocalDateTime
 */
public class Venda {

    /**
     * Identificador numérico exclusivo (chave primária) da transação de venda.
     * @see #getId()
     * @see #setId(int)
     */
    private int id;

    /**
     * Data e hora exatas em que a venda foi iniciada ou registrada.
     * Utiliza a API de tempo do Java 8+ para precisão de carimbo de data/hora.
     * @see #getDate()
     * @see #setDate(LocalDateTime)
     */
    private LocalDateTime date;

    /**
     * Sinalizador booleano que indica o status atual da transação.
     * Se {@code true}, a venda é considerada um "carrinho" ainda aberto.
     * Se {@code false}, indica que o checkout foi realizado e a venda está finalizada.
     * @see #isCarrinho()
     * @see #setCarrinho(boolean)
     */
    private boolean carrinho;

    /**
     * Valor financeiro total acumulado da venda (soma dos preços dos itens vezes as suas quantidades).
     * @see #getTotal()
     * @see #setTotal(double)
     */
    private double total;

    /**
     * Identificador do cliente responsável por esta transação.
     * Funciona como uma chave estrangeira (Foreign Key) lógica referenciando a entidade {@link Cliente}.
     * @see #getIdCliente()
     * @see #setIdCliente(int)
     * @see modelo.Cliente#getId()
     */
    private int idCliente;

    /**
     * Construtor padrão sem argumentos.
     * <p>Sua existência é um requisito obrigatório para garantir o funcionamento
     * correto do processo de desserialização da biblioteca Jackson, permitindo
     * que os objetos sejam recriados na memória a partir do ficheiro JSON.</p>
     * @see com.fasterxml.jackson.databind.ObjectMapper
     */
    public Venda() {
    }

    /**
     * Construtor completo para a inicialização da entidade com todas as informações da transação.
     * @param id O identificador numérico único a ser atribuído à venda.
     * @param date O objeto {@link LocalDateTime} representando a data e hora do registro.
     * @param carrinho O estado da transação ({@code true} para carrinho aberto, {@code false} para venda fechada).
     * @param total O valor monetário total calculado para a transação.
     * @param idCliente O identificador do {@link Cliente} que está realizando a compra.
     */
    public Venda(int id, LocalDateTime date, boolean carrinho, double total, int idCliente) {
        this.id = id;
        this.date = date;
        this.carrinho = carrinho;
        this.total = total;
        this.idCliente = idCliente;
    }

    /**
     * Recupera o identificador exclusivo desta venda.
     * @return O valor inteiro numérico correspondente ao ID da transação.
     */
    public int getId() {
        return id;
    }

    /**
     * Atribui ou atualiza o identificador exclusivo da venda.
     * @param id O novo valor numérico inteiro que representará a chave desta transação.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Recupera a data e hora de registro da transação.
     * @return Um objeto {@link LocalDateTime} contendo o timestamp da venda.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Define ou altera a data e hora de registro da transação.
     * @param date Um objeto {@link LocalDateTime} contendo o novo timestamp a ser registrado.
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * Verifica se a transação atual ainda se encontra na fase de carrinho de compras.
     * @return {@code true} se o carrinho estiver aberto; {@code false} se a venda já foi finalizada.
     */
    public boolean isCarrinho() {
        return carrinho;
    }

    /**
     * Define o status operacional da transação (aberta ou finalizada).
     * @param carrinho {@code true} para classificar como carrinho em andamento, {@code false} para fechar a venda.
     */
    public void setCarrinho(boolean carrinho) {
        this.carrinho = carrinho;
    }

    /**
     * Recupera o valor financeiro total desta transação.
     * @return O valor total em ponto flutuante ({@code double}).
     */
    public double getTotal() {
        return total;
    }

    /**
     * Define ou atualiza o valor financeiro total da transação.
     * <p>Geralmente, este método é invocado sempre que novos itens são adicionados
     * ou removidos do carrinho, atualizando o somatório final.</p>
     * @param total O novo valor monetário total da venda.
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Recupera o identificador do cliente atrelado a esta transação.
     * @return O valor inteiro correspondente ao ID da entidade {@link Cliente}.
     */
    public int getIdCliente() {
        return idCliente;
    }

    /**
     * Vincula esta transação a um cliente específico do sistema.
     * @param idCliente O valor inteiro representando o ID do {@link Cliente} comprador.
     */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }



    /**
     * Retorna uma representação textual estruturada com os metadados principais da venda.
     * <p>A saída é formatada contendo alinhamentos para exibição clara no terminal,
     * destacando o identificador, a data de ocorrência, o valor total da operação
     * e o código do cliente envolvido.</p>
     * @return Uma {@link String} formatada com o resumo financeiro e relacional da transação.
     * @see java.lang.String#format(String, Object...)
     */
    @Override
    public String toString() {
        return String.format("ID: %-3d | Data: %-15s | Total: R$ %-8.2f | Cliente ID: %d",
                id, date.toString(), total, idCliente);
    }
}