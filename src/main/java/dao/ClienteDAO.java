package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import exception.ClienteNaoEncontradoException;
import modelo.Cliente;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ClienteDAO implements DAO<Cliente> {

    private Map<Integer, Cliente> objetos = new HashMap<>();
    private int nextId = 1;
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "clientes.json";

    public ClienteDAO() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        abrir();
    }

    @Override
    public void salvar() {
        try { mapper.writeValue(new File(ARQUIVO), new ArrayList<>(objetos.values())); }
        catch (Exception e) { System.out.println("Erro ao salvar: " + e.getMessage()); }
    }

    @Override
    public void abrir() {
        try {
            File arquivo = new File(ARQUIVO);
            if (arquivo.exists()) {
                List<Cliente> lista = mapper.readValue(arquivo, new TypeReference<List<Cliente>>() {});
                objetos.clear();
                for (Cliente c : lista) {
                    objetos.put(c.getId(), c);
                }
                nextId = lista.stream().mapToInt(Cliente::getId).max().orElse(0) + 1;
            }
        } catch (Exception e) { objetos = new HashMap<>(); }
    }

    @Override
    public void inserir(Cliente obj) {
        int id = nextId++;
        objetos.put(id, new Cliente(id, obj.getNome(), obj.getEmail(), obj.getFone()));
        salvar();
    }

    @Override
    public List<Cliente> listar() { return new ArrayList<>(objetos.values()); }

    @Override
    public Cliente listarId(int id) {
        Cliente c = objetos.get(id);
        if (c == null) throw new ClienteNaoEncontradoException("Cliente com ID " + id + " não encontrado.");
        return c;
    }

    @Override
    public void atualizar(Cliente obj) {
        if (!objetos.containsKey(obj.getId()))
            throw new ClienteNaoEncontradoException("Cliente com ID " + obj.getId() + " não encontrado.");
        objetos.put(obj.getId(), obj);
        salvar();
    }

    @Override
    public void excluir(Cliente obj) {
        if (objetos.remove(obj.getId()) == null)
            throw new ClienteNaoEncontradoException("Cliente com ID " + obj.getId() + " não encontrado.");
        salvar();
    }
}
