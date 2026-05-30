package modelo;

/**
 * Representa a entidade VendaItem no domínio do sistema de comércio eletrônico.
 * <p>Esta classe atua como um modelo de dados (POJO - Plain Old Java Object) e
 * representa uma entidade associativa que resolve a relação de muitos-para-muitos
 * entre {@link Venda} e {@link Produto}. Ela armazena os detalhes individuais de cada
 * item adicionado ao carrinho, incluindo a quantidade adquirida e o preço praticado
 * no momento exato da transação.</p>

 * @see dao.VendaItemDAO
 * @see modelo.Venda
 * @see modelo.Produto
 */
public class VendaItem {

    /**
     * Identificador numérico exclusivo (chave primária) do item da venda.
     * @see #getId()
     * @see #setId(int)
     */
    private int id;

    /**
     * Quantidade de unidades do produto selecionadas para esta transação.
     * @see #getQtd()
     * @see #setQtd(int)
     */
    private int qtd;

    /**
     * Valor monetário unitário do produto no exato momento da venda.
     * <p>Este valor é copiado do produto para garantir que o histórico da venda
     * não seja afetado caso o preço do produto seja alterado futuramente no catálogo.</p>
     * @see #getPreco()
     * @see #setPreco(double)
     */
    private double preco;

    /**
     * Identificador da transação à qual este item pertence.
     * Funciona como uma chave estrangeira (Foreign Key) referenciando a entidade {@link Venda}.
     * * @see #getIdVenda()
     * @see #setIdVenda(int)
     */
    private int idVenda;

    /**
     * Identificador do produto que está sendo comercializado neste item.
     * Funciona como uma chave estrangeira (Foreign Key) referenciando a entidade {@link Produto}.
     * @see #getIdProduto()
     * @see #setIdProduto(int)
     */
    private int idProduto;

    /**
     * Construtor padrão sem argumentos.
     * <p>A presença deste construtor é estritamente obrigatória para permitir a
     * instanciação automática da classe durante o processo de desserialização
     * executado pela biblioteca Jackson.</p>
     * @see com.fasterxml.jackson.databind.ObjectMapper
     */
    public VendaItem() {
    }

    /**
     * Construtor para inicialização da entidade com os dados básicos do item.
     * <p>Nota: As chaves estrangeiras ({@code idVenda} e {@code idProduto}) não são
     * definidas neste construtor, devendo ser injetadas posteriormente através dos
     * seus respectivos métodos modificadores (setters) ao montar o carrinho.</p>
     * @param id O identificador numérico único a ser atribuído a este item.
     * @param qtd A quantidade de unidades selecionadas para a compra.
     * @param preco O valor monetário unitário registrado no momento da compra.
     */
    public VendaItem(int id, int qtd, double preco) {
        this.id = id;
        this.qtd = qtd;
        this.preco = preco;
    }

    /**
     * Recupera o identificador exclusivo deste item de venda.
     * @return O valor inteiro numérico correspondente ao ID do item.
     */
    public int getId() {
        return id;
    }

    /**
     * Atribui ou atualiza o identificador exclusivo do item de venda.
     * @param id O novo valor numérico inteiro que representará a chave deste item.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Recupera a quantidade de unidades selecionadas para este produto.
     * @return Um número inteiro representando a quantidade adquirida.
     */
    public int getQtd() {
        return qtd;
    }

    /**
     * Define ou altera a quantidade de unidades deste item no carrinho.
     * @param qtd O novo valor inteiro correspondente à quantidade.
     */
    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    /**
     * Recupera o preço unitário praticado para este item na transação.
     * @return O valor monetário em ponto flutuante ({@code double}).
     */
    public double getPreco() {
        return preco;
    }

    /**
     * Define ou atualiza o preço unitário praticado para este item.
     * @param preco O novo valor monetário a ser registrado no histórico.
     */
    public void setPreco(double preco) {
        this.preco = preco;
    }

    /**
     * Recupera o identificador da venda à qual este item está vinculado.
     * @return O valor inteiro correspondente ao ID da entidade {@link Venda}.
     */
    public int getIdVenda() {
        return idVenda;
    }

    /**
     * Vincula este item a uma transação (venda ou carrinho) específica.
     * @param idVenda O valor inteiro representando o ID da {@link Venda} correspondente.
     */
    public void setIdVenda(int idVenda) {
        this.idVenda = idVenda;
    }

    /**
     * Recupera o identificador do produto que compõe este item.
     * @return O valor inteiro correspondente ao ID da entidade {@link Produto}.
     */
    public int getIdProduto() {
        return idProduto;
    }

    /**
     * Vincula este item de venda a um produto do catálogo do sistema.
     * @param idProduto O valor inteiro representando o ID do {@link Produto} adquirido.
     */
    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    /**
     * Retorna uma representação textual estruturada com os dados do item.
     * <p>A formatação gerada apresenta o registro em colunas bem definidas,
     * expondo as chaves primárias e estrangeiras, a quantidade e o preço faturado,
     * facilitando a conferência em recibos exibidos na linha de comando.</p>
     * @return Uma {@link String} formatada contendo as informações detalhadas deste item.
     * @see java.lang.String#format(String, Object...)
     */
    @Override
    public String toString() {
        return String.format("Item ID: %-3d | Venda ID: %-3d | Produto ID: %-3d | Qtd: %-3d | Preço: R$ %-8.2f",
                id, idVenda, idProduto, qtd, preco);
    }
}