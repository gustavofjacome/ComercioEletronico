package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import exception.ProdutoNaoEncontradoException;
import modelo.Produto;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ProdutoDAO implements DAO<Produto> {

    private Map<Integer, Produto> objetos = new HashMap<>();
    private int nextId = 1;
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "produtos.json";

    public ProdutoDAO() {
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
                List<Produto> lista = mapper.readValue(arquivo, new TypeReference<List<Produto>>() {});
                objetos.clear();
                for (Produto p : lista) {
                    objetos.put(p.getId(), p);
                }
                nextId = lista.stream().mapToInt(Produto::getId).max().orElse(0) + 1;
            }
        } catch (Exception e) { objetos = new HashMap<>(); }
    }

    @Override
    public void inserir(Produto obj) {
        int id = nextId++;
        objetos.put(id, new Produto(obj.getId(), obj.getDescricao(), obj.getPreco(),obj.getEstoque()));
        salvar();
    }

    @Override
    public List<Produto> listar() { return new ArrayList<>(objetos.values()); }

    @Override
    public Produto listarId(int id) {
        Produto p = objetos.get(id);
        if (p == null) throw new ProdutoNaoEncontradoException("Produto com ID " + id + " não encontrado.");
        return p;
    }

    @Override
    public void atualizar(Produto obj) {
        if (!objetos.containsKey(obj.getId()))
            throw new ProdutoNaoEncontradoException("Produto com ID " + obj.getId() + " não encontrado.");
        objetos.put(obj.getId(), obj);
        salvar();
    }

    @Override
    public void excluir(Produto obj) {
        if (objetos.remove(obj.getId()) == null)
            throw new ProdutoNaoEncontradoException("Produto com ID " + obj.getId() + " não encontrado.");
        salvar();
    }
}
