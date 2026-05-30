package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import modelo.Cliente;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private List<Cliente> objetos = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "clientes.json";

    public ClienteDAO() {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        abrir();
    }

    public void salvar() {
        try { mapper.writeValue(new File(ARQUIVO), objetos); }
        catch (Exception e) { System.out.println("Erro ao salvar: " + e.getMessage()); }
    }

    public void abrir() {
        try {
            File arquivo = new File(ARQUIVO);
            if (arquivo.exists()) {
                objetos = mapper.readValue(arquivo, new TypeReference<List<Cliente>>(){});
            }
        } catch (Exception e) { objetos = new ArrayList<>(); }
    }

    public void inserir(Cliente obj) {
        int novoId = 1;
        for (Cliente c : objetos) { if (c.getId() >= novoId) novoId = c.getId() + 1; }
        obj.setId(novoId);
        objetos.add(obj);
        salvar();
    }

    public List<Cliente> listar() { return objetos; }

    public Cliente listarID(int id) {
        for (Cliente x : objetos) { if (x.getId() == id) return x; }
        return null;
    }

    public void atualizar(Cliente obj) {
        for (Cliente x : objetos) {
            if (x.getId() == obj.getId()) {
                x.setNome(obj.getNome());
                x.setEmail(obj.getEmail());
                x.setFone(obj.getFone());
                salvar();
                break;
            }
        }
    }

    public void excluir(Cliente obj) {
        for (Cliente x : objetos) {
            if (x.getId() == obj.getId()) {
                objetos.remove(x);
                salvar();
                break;
            }
        }
    }
}