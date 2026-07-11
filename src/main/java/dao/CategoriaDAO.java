package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import exception.CategoriaNaoEncontradaException;
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
public class CategoriaDAO implements DAO<Categoria> {

    private List<Categoria> objetos = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "categorias.json";

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
    @Override
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
    @Override
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
    @Override
    public void inserir(Categoria obj) {
        int novoId = objetos.stream()
                .mapToInt(Categoria::getId)
                .max()
                .orElse(0) + 1;

        obj.setId(novoId);
        objetos.add(obj);
        salvar();
    }

    public List<Categoria> listar() { return objetos; }

    /**
     * Realiza a busca de uma categoria específica baseada no seu identificador exclusivo.
     * * @param id O valor numérico inteiro que representa a chave primária da categoria.
     * @return A instância de {@link Categoria} correspondente ao ID buscado,
     * ou {@code null} caso nenhum registro corresponda ao parâmetro.
     */
    @Override
    public Categoria listarId(int id) {
        return objetos.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new CategoriaNaoEncontradaException(
                        "Categoria com ID " + id + " não encontrada."
                ));
    }

    /**
     * Modifica os atributos de uma categoria previamente cadastrada.
     * A localização do objeto a ser alterado é feita comparando o ID contido
     * no objeto passado por parâmetro com os IDs da lista. Ao finalizar a
     * atualização na memória, o estado é gravado no disco de forma automática.
     * * @param obj A instância de {@link Categoria} portando o ID de busca e a nova descrição.
     * @see #salvar()
     */
    @Override
    public void atualizar(Categoria obj) {
        Categoria categoria = objetos.stream()
                .filter(c -> c.getId() == obj.getId())
                .findFirst()
                .orElseThrow(() -> new CategoriaNaoEncontradaException(
                        "Categoria com ID " + obj.getId() + " não encontrada."
                ));

        categoria.setDescricao(obj.getDescricao());
        salvar();
    }

    /**
     * Remove permanentemente uma categoria da memória e sincroniza a exclusão
     * com o arquivo JSON no disco.
     * * @param obj A instância de {@link Categoria} cujo ID será utilizado para localizar
     * o registro a ser deletado.
     * @see #salvar()
     * @see List#remove(Object)
     */
    @Override
    public void excluir(Categoria obj) {
        for (Categoria z : objetos) {
            if (z.getId() == obj.getId()) {
                objetos.remove(z);
                salvar();
                break;
            }
        }

        salvar();
    }
}