package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import exception.ClienteNaoEncontradoException;
import modelo.Cliente;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por abstrair a persistência de dados da entidade {@link Cliente}.
 * Implementa o padrão estrutural DAO (Data Access Object) para isolar a lógica de
 * leitura e gravação em arquivos JSON da regra de negócio principal da aplicação.
 * @see modelo.Cliente
 * @see com.fasterxml.jackson.databind.ObjectMapper
 */
public class ClienteDAO implements DAO<Cliente>{

    private List<Cliente> objetos = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "clientes.json";

    public ClienteDAO() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        abrir();
    }

    /**
     * Realiza a serialização da coleção atual de clientes e sobrescreve o arquivo JSON
     * no sistema de arquivos. Em caso de falha de entrada/saída (I/O), como restrições
     * de leitura/escrita do sistema operacional, o erro será interceptado e impresso no console.
     * @see ObjectMapper#writeValue(File, Object)
     */
    @Override
    public void salvar() {
        try { mapper.writeValue(new File(ARQUIVO), objetos); }
        catch (Exception e) { System.out.println("Erro ao salvar: " + e.getMessage()); }
    }

    /**
     * Realiza a desserialização do arquivo JSON "clientes.json", convertendo o texto
     * de volta para uma coleção de objetos {@link Cliente} na memória RAM.
     * Caso o arquivo não exista no diretório raiz ou ocorra um erro de conversão,
     * a lista é redefinida para uma nova instância vazia, prevenindo exceções nulas.
     * @see ObjectMapper#readValue(File, TypeReference)
     */
    @Override
    public void abrir() {
        try {
            File arquivo = new File(ARQUIVO);
            if (arquivo.exists()) {
                objetos = mapper.readValue(arquivo, new TypeReference<List<Cliente>>(){});
            }
        } catch (Exception e) { objetos = new ArrayList<>(); }
    }

    /**
     * Armazena um novo registro de cliente na base de dados em memória e
     * aciona automaticamente a persistência no disco. O identificador (ID) é gerado
     * de forma autoincremental, iterando sobre os itens e somando 1 ao maior ID encontrado.
     * @param obj A instância de {@link Cliente} contendo os novos dados a serem inseridos.
     * @see #salvar()
     */
    @Override
    public void inserir(Cliente obj) {
        int novoId = objetos.stream()
                .mapToInt(Cliente::getId)
                .max()
                .orElse(0) + 1;
        obj.setId(novoId);
        objetos.add(obj);
        salvar();
    }

    /**
     * Recupera o conjunto completo de clientes atualmente geridos por este DAO
     * e carregados em memória.
     * @return Uma {@link List} contendo todas as instâncias de {@link Cliente} armazenadas.
     */
    @Override
    public List<Cliente> listar() { return objetos; }

    /**
     * Realiza a busca detalhada de um cliente específico utilizando o seu identificador
     * exclusivo (chave primária) como critério de filtro.
     * @param id O valor numérico inteiro que representa a chave do cliente desejado.
     * @return A instância de {@link Cliente} correspondente ao ID informado,
     * ou {@code null} caso nenhum registro corresponda a este parâmetro na lista.
     */
    @Override
    public Cliente listarId(int id) {
        for (Cliente x : objetos) { if (x.getId() == id) return x; }
        return null;
    }

    /**
     * Modifica os atributos cadastrais de um cliente previamente registrado.
     * A localização do objeto a ser alterado é realizada comparando o ID contido
     * na instância passada por parâmetro com os IDs da lista ativa. Modifica atributos
     * como nome, e-mail e telefone, finalizando com a sincronização no disco.
     * @param obj A instância de {@link Cliente} portando o ID de busca e as informações atualizadas.
     * @see #salvar()
     */
    @Override
    public void atualizar(Cliente obj) {
        Cliente cliente = objetos.stream()
                .filter(c -> c.getId() == obj.getId())
                .findFirst()
                .orElseThrow(() -> new ClienteNaoEncontradoException(
                        "Cliente com ID " + obj.getId() + " não encontrado."
                ));

        cliente.setNome(obj.getNome());
        cliente.setEmail(obj.getEmail());
        cliente.setFone(obj.getFone());
        salvar();
    }

    /**
     * Remove permanentemente um cliente do cache de memória e sincroniza imediatamente
     * a exclusão com o arquivo JSON físico para refletir a nova estrutura de dados.
     * * @param obj A instância de {@link Cliente} cujo ID será utilizado para localizar
     * e validar o registro a ser removido.
     * @see #salvar()
     * @see List#remove(Object)
     */
    @Override
    public void excluir(Cliente obj) {
        if (!objetos.removeIf(cliente -> cliente.getId() == obj.getId())) {
            throw new ClienteNaoEncontradoException(
                    "Cliente com ID " + obj.getId() + " não encontrado."
            );
        }

        salvar();
    }
}
