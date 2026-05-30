package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import modelo.Categoria;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por abstrair a persistência de dados da entidade {@link Categoria}.
 * Implementa o padrão estrutural DAO (Data Access Object) para isolar a lógica de
 * leitura e gravação em arquivos JSON da regra de negócio principal da aplicação.
 * @see modelo.Categoria
 * @see com.fasterxml.jackson.databind.ObjectMapper
 */
public class CategoriaDAO {

    /**
     * Lista mantida em memória que atua como cache dos dados armazenados no arquivo.
     * @see java.util.List
     * @see java.util.ArrayList
     */
    private List<Categoria> objetos = new ArrayList<>();

    /**
     * Instância do conversor responsável pela serialização e desserialização
     * dos objetos Java para o formato JSON.
     * @see com.fasterxml.jackson.databind.ObjectMapper
     */
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Nome do arquivo físico no sistema operacional onde os dados em formato JSON
     * serão persistidos.
     */
    private final String ARQUIVO = "categorias.json";

    /**
     * Construtor padrão da classe {@link CategoriaDAO}.
     * Inicializa o conversor JSON ativando a formatação identada para facilitar
     * a leitura humana do arquivo gerado e invoca a leitura prévia dos dados.
     * @see #abrir()
     */
    public CategoriaDAO() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        abrir();
    }

    /**
     * Realiza a serialização da lista de objetos atual e sobrescreve o arquivo JSON.
     * Em caso de falha de entrada/saída (I/O), como falta de permissão na pasta,
     * o erro será interceptado e impresso no console padrão.
     * @see ObjectMapper#writeValue(File, Object)
     */
    public void salvar() {
        try { mapper.writeValue(new File(ARQUIVO), objetos); }
        catch (Exception e) { System.out.println("Erro ao salvar: " + e.getMessage()); }
    }

    /**
     * Realiza a desserialização do arquivo JSON, convertendo o texto de volta
     * para uma coleção de objetos Java na memória RAM.
     * Se o arquivo não existir ou estiver corrompido, a lista é redefinida
     * para uma nova instância vazia.
     * @see ObjectMapper#readValue(File, TypeReference)
     */
    public void abrir() {
        try {
            File arquivo = new File(ARQUIVO);
            if (arquivo.exists()) {
                objetos = mapper.readValue(arquivo, new TypeReference<List<Categoria>>(){});
            }
        } catch (Exception e) { objetos = new ArrayList<>(); }
    }

    /**
     * Armazena um novo registro de categoria na base de dados em memória e
     * invoca a persistência no disco. O identificador (ID) é gerado de forma
     * autoincremental com base no maior valor existente.
     * @param obj A instância de {@link Categoria} que será inserida.
     * @see #salvar()
     */
    public void inserir(Categoria obj) {
        int novoId = 1;
        for (Categoria c : objetos) { if (c.getId() >= novoId) novoId = c.getId() + 1; }
        obj.setId(novoId);
        objetos.add(obj);
        salvar();
    }

    /**
     * Recupera o conjunto completo de categorias geridas por este DAO.
     * @return Uma {@link List} contendo todas as instâncias de {@link Categoria} armazenadas.
     */
    public List<Categoria> listar() { return objetos; }

    /**
     * Realiza a busca de uma categoria específica baseada no seu identificador exclusivo.
     * * @param id O valor numérico inteiro que representa a chave primária da categoria.
     * @return A instância de {@link Categoria} correspondente ao ID buscado,
     * ou {@code null} caso nenhum registro corresponda ao parâmetro.
     */
    public Categoria listarId(int id) {
        for (Categoria x : objetos) { if (x.getId() == id) return x; }
        return null;
    }

    /**
     * Modifica os atributos de uma categoria previamente cadastrada.
     * A localização do objeto a ser alterado é feita comparando o ID contido
     * no objeto passado por parâmetro com os IDs da lista. Ao finalizar a
     * atualização na memória, o estado é gravado no disco de forma automática.
     * * @param obj A instância de {@link Categoria} portando o ID de busca e a nova descrição.
     * @see #salvar()
     */
    public void atualizar(Categoria obj) {
        for (Categoria y : objetos) {
            if (y.getId() == obj.getId()) {
                y.setDescricao(obj.getDescricao());
                salvar();
                break;
            }
        }
    }

    /**
     * Remove permanentemente uma categoria da memória e sincroniza a exclusão
     * com o arquivo JSON no disco.
     * * @param obj A instância de {@link Categoria} cujo ID será utilizado para localizar
     * o registro a ser deletado.
     * @see #salvar()
     * @see List#remove(Object)
     */
    public void excluir(Categoria obj) {
        for (Categoria z : objetos) {
            if (z.getId() == obj.getId()) {
                objetos.remove(z);
                salvar();
                break;
            }
        }
    }
}