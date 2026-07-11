package modelo;

/**
 * Representa a entidade Produto no domínio do sistema de comércio eletrônico.
 * <p>Esta classe atua como um modelo de dados (POJO - Plain Old Java Object)
 * destinado a encapsular as informações detalhadas dos itens disponíveis para
 * venda no catálogo da loja, incluindo o seu preço, disponibilidade em estoque
 * e o vínculo com a sua respectiva categoria.</p>

 * @see dao.ProdutoDAO
 * @see modelo.Categoria
 * @see modelo.VendaItem
 */
public class Produto {

    /**
     * Identificador numérico exclusivo (chave primária) do produto no catálogo.
     * @see #getId()
     * @see #setId(int)
     */
    private int id;

    /**
     * Descrição textual ou nome comercial do produto.
     * @see #getDescricao()
     * @see #setDescricao(String)
     */
    private String descricao;

    /**
     * Valor monetário unitário de venda do produto.
     * @see #getPreco()
     * @see #setPreco(double)
     */
    private double preco;

    /**
     * Quantidade atual de unidades disponíveis para venda física ou virtual.
     * @see #getEstoque()
     * @see #setEstoque(int)
     */
    private int estoque;

    /**
     * Identificador numérico da categoria à qual este produto pertence.
     * Funciona como uma chave estrangeira (Foreign Key) lógica para a entidade {@link Categoria}.
     * @see #getIdCategoria()
     * @see #setIdCategoria(int)
     * @see modelo.Categoria#getId()
     */
    private int idCategoria;

    /**
     * Construtor padrão sem argumentos.
     * <p>A presença deste construtor é estritamente necessária para permitir a
     * instanciação automática da classe durante o processo de desserialização
     * executado pela biblioteca Jackson (leitura do ficheiro JSON).</p>
     * @see com.fasterxml.jackson.databind.ObjectMapper
     */
    public Produto() {
    }

    /**
     * Construtor para inicialização da entidade com os seus atributos principais.
     * <p>Nota: O identificador da categoria ({@code idCategoria}) não é definido neste
     * construtor inicial, devendo ser atribuído posteriormente via método modificador (setter).</p>
     * @param id O identificador numérico único a ser atribuído a este produto.
     * @param descricao Uma {@link String} contendo o nome ou detalhes textuais do produto.
     * @param preco O valor monetário unitário do produto expresso em ponto flutuante.
     * @param estoque A quantidade inteira inicial de unidades disponíveis em estoque.
     */
    public Produto(int id, String descricao, double preco, int estoque) {
        this.id = id;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
    }

    /**
     * Recupera o identificador exclusivo do produto.
     * @return O valor inteiro numérico correspondente ao ID do produto.
     */
    public int getId() {
        return id;
    }

    /**
     * Atribui ou atualiza o identificador exclusivo do produto.
     * @param id O novo valor numérico inteiro que representará a chave primária deste produto.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Recupera a descrição comercial do produto.
     * @return Uma {@link String} contendo a descrição atual do produto.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define ou altera a descrição comercial do produto.
     * @param descricao Uma {@link String} contendo a nova descrição a ser atribuída.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Recupera o valor de venda do produto.
     * @return O preço unitário atual expresso em {@code double}.
     */
    public double getPreco() {
        return preco;
    }

    /**
     * Define ou altera o valor de venda do produto.
     * @param preco O novo valor monetário a ser definido para comercialização.
     */
    public void setPreco(double preco) {
        this.preco = preco;
    }

    /**
     * Recupera a quantidade de unidades disponíveis em estoque.
     * @return Um número inteiro representando o saldo atual do produto em armazém.
     */
    public int getEstoque() {
        return estoque;
    }

    /**
     * Define ou atualiza a quantidade de unidades em estoque.
     * @param estoque O novo valor numérico inteiro a ser registado como saldo de estoque.
     */
    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    /**
     * Recupera o identificador da categoria associada a este produto.
     * @return O valor inteiro correspondente ao ID da {@link Categoria} à qual o produto pertence.
     */
    public int getIdCategoria() {
        return idCategoria;
    }

    /**
     * Vincula o produto a uma categoria específica através do seu identificador.
     * @param idCategoria O ID da {@link Categoria} a ser associada a este produto.
     */
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Retorna uma representação em formato de texto estruturado dos dados do produto.
     * <p>A saída é formatada de forma tabular e padronizada, incluindo o símbolo da moeda local (R$),
     * sendo ideal para exibição amigável em interfaces de consola ou relatórios.</p>
     * @return Uma {@link String} devidamente formatada contendo o ID, descrição, preço, estoque e ID da categoria do produto.
     * @see java.lang.String#format(String, Object...)
     */
    @Override
    public String toString() {
        return String.format("ID: %-3d | Descrição: %-20s | Preço: R$ %-8.2f | Estoque: %-5d | Cat. ID: %d",
                id, descricao, preco, estoque, idCategoria);
    }
}