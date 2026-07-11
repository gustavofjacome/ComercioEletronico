package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import exception.CategoriaNaoEncontradaException;
import modelo.Categoria;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriaDAO implements DAO<Categoria> {

    private Map<Integer, Categoria> objetos = new HashMap<>();
    private int nextId = 1;
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "categorias.json";

    public CategoriaDAO() {
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
                List<Categoria> lista = mapper.readValue(arquivo, new TypeReference<List<Categoria>>() {});
                objetos.clear();
                for (Categoria c : lista) {
                    objetos.put(c.getId(), c);
                }
                nextId = lista.stream().mapToInt(Categoria::getId).max().orElse(0) + 1;
            }
        } catch (Exception e) { objetos = new HashMap<>(); }
    }

    @Override
    public void inserir(Categoria obj) {
        int id = nextId++;
        objetos.put(id, new Categoria(id, obj.getDescricao()));
        salvar();
    }

    @Override
    public List<Categoria> listar() { return new ArrayList<>(objetos.values()); }

    @Override
    public Categoria listarId(int id) {
        Categoria c = objetos.get(id);
        if (c == null) throw new CategoriaNaoEncontradaException("Categoria com ID " + id + " não encontrada.");
        return c;
    }

    @Override
    public void atualizar(Categoria obj) {
        if (!objetos.containsKey(obj.getId()))
            throw new CategoriaNaoEncontradaException("Categoria com ID " + obj.getId() + " não encontrada.");
        objetos.put(obj.getId(), obj);
        salvar();
    }

    @Override
    public void excluir(Categoria obj) {
        if (objetos.remove(obj.getId()) == null)
            throw new CategoriaNaoEncontradaException("Categoria com ID " + obj.getId() + " não encontrada.");
        salvar();
    }
}
