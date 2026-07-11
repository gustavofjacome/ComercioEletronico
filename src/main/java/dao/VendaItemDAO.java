package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import exception.VendaItemNaoEncontradoException;
import modelo.VendaItem;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por abstrair a persistência de dados da entidade {@link VendaItem}.
 * Implementa o padrão estrutural DAO (Data Access Object) para centralizar a lógica de
 * gravação e leitura em arquivos JSON.
 * * <p>Esta classe gerencia a entidade associativa que conecta as instâncias de
 * {@link modelo.Venda} e {@link modelo.Produto}, detalhando as quantidades e
 * os preços praticados no momento exato da transação.</p>
 * @see modelo.VendaItem
 * @see modelo.Venda
 * @see modelo.Produto
 * @see com.fasterxml.jackson.databind.ObjectMapper
 */
public class VendaItemDAO implements DAO<VendaItem>{

    private List<VendaItem> objetos = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "vendas_itens.json";

    public VendaItemDAO() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        abrir();
    }

    /**
     * Realiza a serialização da coleção atual de itens de venda e sobrescreve
     * o arquivo JSON no sistema de arquivos. Em caso de falha de entrada/saída (I/O),
     * como falta de privilégios de gravação, o erro será interceptado e notificado no console.
     * @see ObjectMapper#writeValue(File, Object)
     */
    @Override
    public void salvar() {
        try { mapper.writeValue(new File(ARQUIVO), objetos); }
        catch (Exception e) { System.out.println("Erro ao salvar: " + e.getMessage()); }
    }

    /**
     * Realiza a desserialização do arquivo JSON "vendas_itens.json", convertendo a
     * estrutura de texto novamente para uma coleção de objetos {@link VendaItem} na RAM.
     * Se o arquivo não for localizado no diretório raiz ou ocorrer uma falha de conversão,
     * a lista é redefinida para uma nova instância vazia, garantindo a estabilidade do sistema.
     * @see ObjectMapper#readValue(File, TypeReference)
     * @see java.io.File#exists()
     */
    @Override
    public void abrir() {
        try {
            File arquivo = new File(ARQUIVO);
            if (arquivo.exists()) {
                objetos = mapper.readValue(arquivo, new TypeReference<List<VendaItem>>(){});
            }
        } catch (Exception e) { objetos = new ArrayList<>(); }
    }

    /**
     * Armazena um novo registro de item de venda na base de dados em memória e
     * invoca automaticamente a persistência no disco. O identificador (ID) é gerado
     * de forma autoincremental com base no maior valor atualmente em uso na lista.
     * @param obj A instância de {@link VendaItem} contendo os dados de ligação entre produto e venda.
     * @see #salvar()
     */
    @Override
    public void inserir(VendaItem obj) {
        int novoId = objetos.stream()
                .mapToInt(VendaItem::getId)
                .max()
                .orElse(0) + 1;
        obj.setId(novoId);
        objetos.add(obj);
        salvar();
    }

    /**
     * Recupera o histórico completo de itens de vendas atualmente gerenciados por este DAO
     * e cacheados em memória.
     * @return Uma {@link List} contendo todas as instâncias de {@link VendaItem} registradas.
     * @see java.util.List
     */
    @Override
    public List<VendaItem> listar() { return objetos; }

    /**
     * Realiza a busca detalhada de um item de venda específico utilizando o seu identificador
     * exclusivo (chave primária) como critério único de filtro.
     * @param id O valor numérico inteiro que representa a chave primária do item da venda.
     * @return A instância de {@link VendaItem} correspondente ao ID informado,
     * ou {@code null} caso não haja correspondência na lista atual.
     */
    @Override
    public VendaItem listarId(int id) {
        return objetos.stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElseThrow(() -> new VendaItemNaoEncontradoException(
                        "VendaItem com ID " + id + " não encontrado."
                ));
    }

    /**
     * Modifica os atributos cadastrais de um item de venda previamente inserido.
     * A localização do objeto a ser alterado é realizada cruzando o ID contido na instância
     * recebida via parâmetro com os IDs da lista. Atualiza a quantidade, preço unitário
     * e as chaves estrangeiras (ID Venda e ID Produto), salvando o estado no disco em seguida.
     * @param obj A instância de {@link VendaItem} com as informações atualizadas.
     * @see #salvar()
     */
    @Override
    public void atualizar(VendaItem obj) {
        VendaItem item = objetos.stream()
                .filter(v -> v.getId() == obj.getId())
                .findFirst()
                .orElseThrow(() -> new VendaItemNaoEncontradoException(
                        "VendaItem com ID " + obj.getId() + " não encontrado."
                ));

        item.setQtd(obj.getQtd());
        item.setPreco(obj.getPreco());
        item.setIdVenda(obj.getIdVenda());
        item.setIdProduto(obj.getIdProduto());
        salvar();
    }

    /**
     * Remove permanentemente o registro de um item de venda da cache de memória e
     * sincroniza imediatamente essa exclusão com o arquivo JSON físico.
     * @param obj A instância de {@link VendaItem} cujo ID será utilizado para localizar
     * e expurgar o registro.
     * @see #salvar()
     * @see List#remove(Object)
     */
    @Override
    public void excluir(VendaItem obj) {
        if (!objetos.removeIf(item -> item.getId() == obj.getId())) {
            throw new VendaItemNaoEncontradoException(
                    "VendaItem com ID " + obj.getId() + " não encontrado."
            );
        }

        salvar();
    }
}
