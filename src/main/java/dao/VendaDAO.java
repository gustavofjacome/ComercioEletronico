package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import modelo.Venda;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por abstrair a persistência de dados da entidade {@link Venda}.
 * Implementa o padrão estrutural DAO (Data Access Object) para centralizar a lógica de
 * leitura e gravação em ficheiros JSON, separando-a das regras de negócio.
 * <p>Esta classe possui uma configuração especial no conversor JSON para suportar
 * a serialização nativa da API de Datas do Java 8 ({@code LocalDateTime}).</p>
 * @see modelo.Venda
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
 */
public class VendaDAO implements DAO<Venda>{

    /**
     * Lista mantida em memória que atua como cache do histórico de vendas
     * armazenado no ficheiro físico.
     * @see java.util.List
     * @see java.util.ArrayList
     */
    private List<Venda> objetos = new ArrayList<>();

    /**
     * Instância do conversor responsável pela serialização e desserialização
     * dos objetos da classe {@link Venda} para o formato JSON e vice-versa.
     * @see com.fasterxml.jackson.databind.ObjectMapper
     */
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Nome do ficheiro físico no sistema operativo onde os dados das vendas
     * em formato JSON serão persistidos de forma definitiva.
     * @see java.io.File
     */
    private final String ARQUIVO = "vendas.json";

    /**
     * Construtor padrão da classe {@link VendaDAO}.
     * Regista o módulo de tempo do Java para permitir a correta manipulação de datas,
     * inicializa a formatação indentada (pretty-printing) para facilitar a auditoria
     * do ficheiro gerado e invoca a leitura prévia dos dados armazenados no disco.
     * @see #abrir()
     * @see ObjectMapper#registerModule(com.fasterxml.jackson.databind.Module)
     * @see SerializationFeature#INDENT_OUTPUT
     */
    public VendaDAO() {
        mapper.registerModule(new JavaTimeModule()); // Obrigatório para LocalDateTime
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        abrir();
    }

    /**
     * Realiza a serialização da coleção atual de vendas e sobrescreve o ficheiro JSON
     * no sistema de ficheiros. Em caso de falha de entrada/saída (I/O), como permissões
     * insuficientes no sistema operativo, o erro será interceptado e impresso na consola.
     * @see ObjectMapper#writeValue(File, Object)
     */
    @Override
    public void salvar() {
        try { mapper.writeValue(new File(ARQUIVO), objetos); }
        catch (Exception e) { System.out.println("Erro ao salvar: " + e.getMessage()); }
    }

    /**
     * Realiza a desserialização do ficheiro JSON "vendas.json", convertendo o texto
     * de volta para uma coleção de objetos {@link Venda} na memória RAM.
     * Caso o ficheiro não exista no diretório raiz ou ocorra um erro de sintaxe JSON,
     * a lista é redefinida para uma nova instância vazia, prevenindo exceções em tempo de execução.
     * @see ObjectMapper#readValue(File, TypeReference)
     * @see java.io.File#exists()
     */
    @Override
    public void abrir() {
        try {
            File arquivo = new File(ARQUIVO);
            if (arquivo.exists()) {
                objetos = mapper.readValue(arquivo, new TypeReference<List<Venda>>(){});
            }
        } catch (Exception e) { objetos = new ArrayList<>(); }
    }

    /**
     * Armazena um novo registo de venda na base de dados em memória e
     * aciona automaticamente a persistência no disco. O identificador (ID) é gerado
     * de forma autoincremental, localizando o maior ID existente e somando 1.
     * @param obj A instância de {@link Venda} contendo os dados da transação a ser registada.
     * @see #salvar()
     */
    @Override
    public void inserir(Venda obj) {
        int novoId = 1;
        for (Venda v : objetos) { if (v.getId() >= novoId) novoId = v.getId() + 1; }
        obj.setId(novoId);
        objetos.add(obj);
        salvar();
    }

    /**
     * Recupera o histórico completo de vendas atualmente geridas por este DAO
     * e carregadas em memória.
     * @return Uma {@link List} contendo todas as instâncias de {@link Venda} registadas no sistema.
     * @see java.util.List
     */
    @Override
    public List<Venda> listar() { return objetos; }

    /**
     * Realiza a busca de uma transação de venda específica utilizando o seu identificador
     * exclusivo (chave primária).
     * @param id O valor numérico inteiro que representa a chave da venda procurada.
     * @return A instância de {@link Venda} correspondente ao ID informado,
     * ou {@code null} caso nenhum registo coincida na lista ativa.
     */
    @Override
    public Venda listarId(int id) {
        for (Venda x : objetos) { if (x.getId() == id) return x; }
        return null;
    }

    /**
     * Modifica os atributos de uma venda previamente registada.
     * A localização do objeto alvo é realizada comparando o ID contido na instância
     * recebida por parâmetro com os IDs do cache. Atualiza os dados da transação (data,
     * status do carrinho, valor total e cliente associado) e sincroniza no disco.
     * @param obj A instância de {@link Venda} portando o ID de busca e os valores a serem atualizados.
     * @see #salvar()
     */
    @Override
    public void atualizar(Venda obj) {
        for (Venda y : objetos) {
            if (y.getId() == obj.getId()) {
                y.setDate(obj.getDate());
                y.setCarrinho(obj.isCarrinho());
                y.setTotal(obj.getTotal());
                y.setIdCliente(obj.getIdCliente());
                salvar();
                break;
            }
        }
    }

    /**
     * Remove permanentemente o registo de uma venda da cache de memória e sincroniza
     * a exclusão com o ficheiro JSON físico, atualizando o histórico financeiro da aplicação.
     * @param obj A instância de {@link Venda} cujo ID será extraído para localizar
     * e eliminar a transação correspondente.
     * @see #salvar()
     * @see List#remove(Object)
     */
    @Override
    public void excluir(Venda obj) {
        for (Venda z : objetos) {
            if (z.getId() == obj.getId()) {
                objetos.remove(z);
                salvar();
                break;
            }
        }
    }
}