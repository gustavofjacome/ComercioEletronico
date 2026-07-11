package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import exception.ProdutoNaoEncontradoException;
import modelo.Produto;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe principal responsável por fornecer a Interface de Linha de Comando (CLI) do sistema.
 * <p>Esta classe atua como a camada de visualização (View) e controle primário da aplicação.
 * Ela exibe menus interativos, captura a entrada de dados do utilizador através da consola
 * e delega as operações de negócio (CRUD) para as respetivas classes de acesso a dados (DAOs).</p>
 * @see dao.ClienteDAO
 * @see dao.ProdutoDAO
 * @see dao.CategoriaDAO
 */
public class ProdutoDAO implements DAO<Produto>{
    private List<Produto> objetos = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "produtos.json";

    public ProdutoDAO() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        abrir();
    }

    /**
     * Serializa a coleção de produtos carregada em memória e a grava no arquivo físico correspondente.
     * Caso o sistema operacional negue a gravação ou o caminho do arquivo seja inválido,
     * a exceção é capturada e a mensagem de erro é exibida via saída padrão do console.
     * @see ObjectMapper#writeValue(File, Object)
     */
    @Override
    public void salvar() {
        try { mapper.writeValue(new File(ARQUIVO), objetos); }
        catch (Exception e) { System.out.println("Erro ao salvar: " + e.getMessage()); }
    }

    /**
     * Tenta localizar e ler o arquivo de persistência de produtos no disco.
     * Caso o arquivo exista, desserializa o seu conteúdo JSON e o converte para a lista
     * de objetos {@link Produto} na memória RAM. Se o arquivo não for encontrado ou estiver
     * vazio/corrompido, a lista interna é inicializada vazia para evitar falhas de execução.
     * @see ObjectMapper#readValue(File, TypeReference)
     */
    @Override
    public void abrir() {
        try {
            File arquivo = new File(ARQUIVO);
            if (arquivo.exists()) {
                objetos = mapper.readValue(arquivo, new TypeReference<List<Produto>>(){});
            }
        } catch (Exception e) { objetos = new ArrayList<>(); }
    }

    /**
     * Realiza a inserção de um novo produto no cache de memória e aciona a gravação no disco.
     * O sistema percorre a lista atual de produtos para determinar o maior valor de ID existente
     * e, em seguida, gera um ID sequencial seguro para o novo registro.
     * @param obj A instância contendo os dados do novo {@link Produto} a ser cadastrado.
     * @see #salvar()
     */
    @Override
    public void inserir(Produto obj) {
        int novoId = objetos.stream()
                .mapToInt(Produto::getId)
                .max()
                .orElse(0) + 1;
        obj.setId(novoId);
        objetos.add(obj);
        salvar();
    }

    /**
     * Recupera todos os registros de produtos que estão atualmente cacheados na memória.
     * @return Uma estrutura do tipo {@link List} contendo todas as instâncias de {@link Produto}.
     */
    @Override
    public List<Produto> listar() { return objetos; }

    /**
     * Executa uma busca linear na coleção de produtos para encontrar o registro exato
     * que corresponda à chave primária fornecida.
     * @param id O valor numérico (inteiro) que representa a chave exclusiva do produto.
     * @return O objeto {@link Produto} se a correspondência for exata; caso contrário, retorna {@code null}.
     */
    @Override
    public Produto listarId(int id) {
        return objetos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ProdutoNaoEncontradoException(
                        "Produto com ID " + id + " não encontrado."
                ));
    }

    /**
     * Processa a modificação dos atributos de um produto preexistente na base de dados.
     * Identifica o alvo da alteração através do ID contido no objeto recebido, atualiza
     * sua descrição, preço, quantidade em estoque e a chave estrangeira da categoria associada,
     * e então sincroniza as alterações no arquivo JSON.
     * @param obj O objeto {@link Produto} preenchido com o ID do registro alvo e seus novos valores de atributos.
     * @see #salvar()
     */
    @Override
    public void atualizar(Produto obj) {
        Produto produto = objetos.stream()
                .filter(p -> p.getId() == obj.getId())
                .findFirst()
                .orElseThrow(() -> new ProdutoNaoEncontradoException(
                        "Produto com ID " + obj.getId() + " não encontrado."
                ));

        produto.setDescricao(obj.getDescricao());
        produto.setPreco(obj.getPreco());
        produto.setEstoque(obj.getEstoque());
        produto.setIdCategoria(obj.getIdCategoria());
        salvar();
    }

    /**
     * Remove um produto específico da coleção em memória e atualiza a base de dados JSON
     * para refletir permanentemente essa deleção.
     * @param obj A instância do {@link Produto} utilizada para extrair o ID e efetuar a exclusão do registro correspondente.
     * @see #salvar()
     * @see List#remove(Object)
     */
    @Override
    public void excluir(Produto obj) {
        if (!objetos.removeIf(produto -> produto.getId() == obj.getId())) {
            throw new ProdutoNaoEncontradoException(
                    "Produto com ID " + obj.getId() + " não encontrado."
            );
        }

        salvar();
    }
}
