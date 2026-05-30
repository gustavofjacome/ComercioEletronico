package modelo;

/**
 * Representa a entidade Categoria no domínio do sistema de comércio eletrônico.
 * <p>Esta classe atua como um modelo de dados (POJO - Plain Old Java Object)
 * utilizado para organizar e agrupar os itens do catálogo de produtos. Cada
 * categoria é composta por uma identificação única e uma descrição textual.</p>
 * @see modelo.Produto
 * @see dao.CategoriaDAO
 */
public class Categoria {

    /**
     * Identificador numérico exclusivo (chave primária) da categoria.
     * @see #getId()
     * @see #setId(int)
     */
    private int id;

    /**
     * Descrição textual ou nome comercial da categoria (ex: "Eletrônicos", "Vestuário").
     * @see #getDescricao()
     * @see #setDescricao(String)
     */
    private String descricao;

    /**
     * Construtor padrão sem argumentos.
     * <p>A presença deste construtor é estritamente necessária para permitir a
     * instanciação correta da classe durante o processo de desserialização
     * executado pela biblioteca Jackson (leitura do arquivo JSON).</p>
     * @see com.fasterxml.jackson.databind.ObjectMapper
     */
    public Categoria() {
    }

    /**
     * Construtor completo para inicialização rápida da entidade com todos os seus atributos.
     * @param id O identificador único numérico a ser atribuído a esta categoria.
     * @param descricao Uma {@link String} contendo o nome descritivo da categoria.
     */
    public Categoria(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    /**
     * Recupera o identificador exclusivo da categoria atual.
     * @return O valor inteiro correspondente ao ID da categoria.
     */
    public int getId() {
        return id;
    }

    /**
     * Atribui um novo identificador exclusivo à categoria.
     * @param id O novo valor numérico inteiro que representará o ID desta categoria no sistema.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Recupera a descrição textual da categoria.
     * @return Uma {@link String} contendo o nome ou descrição atual da categoria.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define ou altera a descrição textual da categoria.
     * @param descricao Uma {@link String} contendo o novo nome ou descrição a ser atribuído.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Retorna uma representação em formato de texto dos dados encapsulados nesta categoria.
     * <p>O texto gerado é formatado em colunas alinhadas, facilitando a exibição
     * tabular e a leitura em interfaces de linha de comando (CLI/Console).</p>
     * @return Uma {@link String} formatada contendo o ID e a descrição da categoria.
     * @see java.lang.String#format(String, Object...)
     */
    @Override
    public String toString() {
        return String.format("ID: %-3d | Descrição: %s", id, descricao);
    }
}