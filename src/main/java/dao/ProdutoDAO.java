package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import exception.ProdutoNaoEncontradoException;
import modelo.Produto;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO implements DAO<Produto> {

    private List<Produto> objetos = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "produtos.json";

    public ProdutoDAO() {
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
                objetos = mapper.readValue(arquivo, new TypeReference<List<Produto>>(){});
            }
        } catch (Exception e) { objetos = new ArrayList<>(); }
    }

    @Override
    public void inserir(Produto obj) {
        int novoId = objetos.stream()
                .mapToInt(Produto::getId)
                .max()
                .orElse(0) + 1;
        obj.setId(novoId);
        objetos.add(obj);
        salvar();
    }

    @Override
    public List<Produto> listar() { return objetos; }

    @Override
    public Produto listarId(int id) {
        return objetos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ProdutoNaoEncontradoException(
                        "Produto com ID " + id + " não encontrado."
                ));
    }

    @Override
    public void atualizar(Produto obj) {
        Produto produto = objetos.stream()
                .filter(p -> p.getId() == obj.getId())
                .findFirst()
                .orElseThrow(() -> new ProdutoNaoEncontradoException(
                        "Produto com ID " + obj.getId() + " não encontrado."
                ));

        produto.setDescricao(obj.getDescricao());
        produto.setPreco(obj.getPreco());
        produto.setEstoque(obj.getEstoque());
        produto.setIdCategoria(obj.getIdCategoria());
        salvar();
    }

    @Override
    public void excluir(Produto obj) {
        if (!objetos.removeIf(produto -> produto.getId() == obj.getId())) {
            throw new ProdutoNaoEncontradoException(
                    "Produto com ID " + obj.getId() + " não encontrado."
            );
        }

        salvar();
    }
}
