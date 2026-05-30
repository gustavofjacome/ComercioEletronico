package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
public class VendaItemDAO {

    /**
     * Lista mantida em memória que atua como cache dos itens de venda
     * armazenados no arquivo físico.
     * @see java.util.List
     * @see java.util.ArrayList
     */
    private List<VendaItem> objetos = new ArrayList<>();

    /**
     * Instância do conversor responsável pela serialização e desserialização
     * dos objetos da classe {@link VendaItem} para o formato JSON e vice-versa.
     * @see com.fasterxml.jackson.databind.ObjectMapper
     */
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Nome do arquivo físico no sistema operacional onde os dados referentes aos
     * itens do carrinho de compras serão persistidos de forma definitiva.
     * @see java.io.File
     */
    private final String ARQUIVO = "vendas_itens.json";

    /**
     * Construtor padrão da classe {@link VendaItemDAO}.
     * Inicializa a formatação indentada (pretty-printing) no conversor JSON
     * para facilitar a auditoria estrutural do arquivo gerado e invoca
     * a carga inicial dos dados armazenados no disco para a memória.
     * @see #abrir()
     * @see SerializationFeature#INDENT_OUTPUT
     */
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
    public void inserir(VendaItem obj) {
        int novoId = 1;
        for (VendaItem v : objetos) { if (v.getId() >= novoId) novoId = v.getId() + 1; }
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
    public List<VendaItem> listar() { return objetos; }

    /**
     * Realiza a busca detalhada de um item de venda específico utilizando o seu identificador
     * exclusivo (chave primária) como critério único de filtro.
     * @param id O valor numérico inteiro que representa a chave primária do item da venda.
     * @return A instância de {@link VendaItem} correspondente ao ID informado,
     * ou {@code null} caso não haja correspondência na lista atual.
     */
    public VendaItem listarId(int id) {
        for (VendaItem x : objetos) { if (x.getId() == id) return x; }
        return null;
    }

    /**
     * Modifica os atributos cadastrais de um item de venda previamente inserido.
     * A localização do objeto a ser alterado é realizada cruzando o ID contido na instância
     * recebida via parâmetro com os IDs da lista. Atualiza a quantidade, preço unitário
     * e as chaves estrangeiras (ID Venda e ID Produto), salvando o estado no disco em seguida.
     * @param obj A instância de {@link VendaItem} com as informações atualizadas.
     * @see #salvar()
     */
    public void atualizar(VendaItem obj) {
        for (VendaItem y : objetos) {
            if (y.getId() == obj.getId()) {
                y.setQtd(obj.getQtd());
                y.setPreco(obj.getPreco());
                y.setIdVenda(obj.getIdVenda());
                y.setIdProduto(obj.getIdProduto());
                salvar();
                break;
            }
        }
    }

    /**
     * Remove permanentemente o registro de um item de venda da cache de memória e
     * sincroniza imediatamente essa exclusão com o arquivo JSON físico.
     * @param obj A instância de {@link VendaItem} cujo ID será utilizado para localizar
     * e expurgar o registro.
     * @see #salvar()
     * @see List#remove(Object)
     */
    public void excluir(VendaItem obj) {
        for (VendaItem z : objetos) {
            if (z.getId() == obj.getId()) {
                objetos.remove(z);
                salvar();
                break;
            }
        }
    }
}