package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exception.VendaNaoEncontradaException;
import modelo.Venda;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class VendaDAO implements DAO<Venda> {

    private Map<Integer, Venda> objetos = new HashMap<>();
    private int nextId = 1;
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "vendas.json";

    public VendaDAO() {
        mapper.registerModule(new JavaTimeModule());
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
                List<Venda> lista = mapper.readValue(arquivo, new TypeReference<List<Venda>>() {});
                objetos.clear();
                for (Venda v : lista) {
                    objetos.put(v.id(), v);
                }
                nextId = lista.stream().mapToInt(Venda::id).max().orElse(0) + 1;
            }
        } catch (Exception e) { objetos = new HashMap<>(); }
    }

    @Override
    public void inserir(Venda obj) {
        int id = nextId++;
        objetos.put(id, new Venda(id, obj.date(), obj.carrinho(), obj.total(), obj.idCliente()));
        salvar();
    }

    @Override
    public List<Venda> listar() { return new ArrayList<>(objetos.values()); }

    @Override
    public Venda listarId(int id) {
        Venda v = objetos.get(id);
        if (v == null) throw new VendaNaoEncontradaException("Venda com ID " + id + " não encontrada.");
        return v;
    }

    @Override
    public void atualizar(Venda obj) {
        if (!objetos.containsKey(obj.id()))
            throw new VendaNaoEncontradaException("Venda com ID " + obj.id() + " não encontrada.");
        objetos.put(obj.id(), obj);
        salvar();
    }

    @Override
    public void excluir(Venda obj) {
        if (objetos.remove(obj.id()) == null)
            throw new VendaNaoEncontradaException("Venda com ID " + obj.id() + " não encontrada.");
        salvar();
    }
}
