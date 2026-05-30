package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import modelo.Produto;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private List<Produto> objetos = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "produtos.json";

    public ProdutoDAO() {
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
                objetos = mapper.readValue(arquivo, new TypeReference<List<Produto>>(){});
            }
        } catch (Exception e) { objetos = new ArrayList<>(); }
    }

    public void inserir(Produto obj) {
        int novoId = 1;
        for (Produto p : objetos) { if (p.getId() >= novoId) novoId = p.getId() + 1; }
        obj.setId(novoId);
        objetos.add(obj);
        salvar();
    }

    public List<Produto> listar() { return objetos; }

    public Produto listarId(int id) {
        for (Produto x : objetos) { if (x.getId() == id) return x; }
        return null;
    }

    public void atualizar(Produto obj) {
        for (Produto y : objetos) {
            if (y.getId() == obj.getId()) {
                y.setDescricao(obj.getDescricao());
                y.setPreco(obj.getPreco());
                y.setEstoque(obj.getEstoque());
                y.setIdCategoria(obj.getIdCategoria());
                salvar();
                break;
            }
        }
    }

    public void excluir(Produto obj) {
        for (Produto z : objetos) {
            if (z.getId() == obj.getId()) {
                objetos.remove(z);
                salvar();
                break;
            }
        }
    }
}