package modelo;

/**
 * Representa a entidade Cliente no domínio do sistema de comércio eletrônico.
 * <p>Esta classe atua como um modelo de dados (POJO - Plain Old Java Object)
 * destinado a armazenar as informações cadastrais e de contato das pessoas
 * que utilizam a plataforma para realizar as suas compras.</p>
 * @see dao.ClienteDAO
 * @see modelo.Venda
 */
public class Cliente {

    /**
     * Identificador numérico exclusivo (chave primária) do cliente no sistema.
     * @see #getId()
     * @see #setId(int)
     */
    private int id;

    /**
     * Nome completo ou razão social do cliente registado.
     * @see #getNome()
     * @see #setNome(String)
     */
    private String nome;

    /**
     * Endereço de correio eletrônico do cliente, utilizado para contato e identificação.
     * @see #getEmail()
     * @see #setEmail(String)
     */
    private String email;

    /**
     * Número de telefone para contato direto com o cliente.
     * @see #getFone()
     * @see #setFone(String)
     */
    private String fone;

    /**
     * Construtor padrão sem argumentos.
     * <p>A presença deste construtor é estritamente obrigatória para permitir a
     * instanciação automática da classe durante o processo de desserialização
     * executado pela biblioteca Jackson (aquando da leitura do ficheiro JSON).</p>
     * @see com.fasterxml.jackson.databind.ObjectMapper
     */
    public Cliente() {
    }

    /**
     * Construtor completo para a inicialização rápida da entidade com todos os seus atributos cadastrais.
     * @param id O identificador numérico único a ser atribuído a este cliente.
     * @param nome Uma {@link String} contendo o nome completo do cliente.
     * @param email Uma {@link String} contendo o endereço de e-mail do cliente.
     * @param fone Uma {@link String} contendo o número de telefone de contato do cliente.
     */
    public Cliente(int id, String nome, String email, String fone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.fone = fone;
    }

    /**
     * Recupera o identificador exclusivo do cliente atual.
     * @return O valor inteiro numérico correspondente ao ID do cliente.
     */
    public int getId() {
        return id;
    }

    /**
     * Atribui ou atualiza o identificador exclusivo do cliente no sistema.
     * @param id O novo valor numérico inteiro que representará a chave deste cliente.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Recupera o nome cadastrado do cliente.
     * @return Uma {@link String} contendo o nome atual do cliente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define ou altera o nome do cliente.
     * @param nome Uma {@link String} contendo o novo nome a ser atribuído.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Recupera o endereço de e-mail cadastrado do cliente.
     * @return Uma {@link String} contendo o e-mail atual associado a este registo.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define ou altera o endereço de e-mail do cliente.
     * @param email Uma {@link String} contendo o novo e-mail a ser registado.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Recupera o número de telefone cadastrado do cliente.
     * @return Uma {@link String} contendo o telefone atual de contato.
     */
    public String getFone() {
        return fone;
    }

    /**
     * Define ou altera o número de telefone de contato do cliente.
     * @param fone Uma {@link String} contendo o novo número de telefone a ser atribuído.
     */
    public void setFone(String fone) {
        this.fone = fone;
    }

    /**
     * Retorna uma representação em formato de texto dos dados encapsulados neste cliente.
     * <p>A saída gerada é formatada em colunas alinhadas verticalmente, sendo ideal para a exibição
     * estruturada e legível em interfaces de linha de comando (CLI/Consola).</p>
     * @return Uma {@link String} formatada de forma tabular contendo o ID, o nome, o e-mail e o telefone do cliente.
     * @see java.lang.String#format(String, Object...)
     */
    @Override
    public String toString() {
        return String.format("ID: %-3d | Nome: %-20s | E-mail: %-20s | Telefone: %s",
                id, nome, email, fone);
    }
}