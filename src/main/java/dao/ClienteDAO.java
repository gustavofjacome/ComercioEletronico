package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import exception.ClienteNaoEncontradoException;
import modelo.Cliente;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements DAO<Cliente> {

    private List<Cliente> objetos = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "clientes.json";

    public ClienteDAO() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        abrir();
    }

    @Override
    public void salvar() {
        try { mapper.writeValue(new File(ARQUIVO), objetos); }
        catch (Exception e) { System.out.println("Erro ao salvar: " + e.getMessage()); }
    }

    @Override
    public void abrir() {
        try {
            File arquivo = new File(ARQUIVO);
            if (arquivo.exists()) {
                objetos = mapper.readValue(arquivo, new TypeReference<List<Cliente>>(){});
            }
        } catch (Exception e) { objetos = new ArrayList<>(); }
    }

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

    @Override
    public List<Cliente> listar() { return objetos; }

    @Override
    public Cliente listarId(int id) {
        return objetos.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ClienteNaoEncontradoException(
                        "Cliente com ID " + id + " não encontrado."
                ));
    }

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
