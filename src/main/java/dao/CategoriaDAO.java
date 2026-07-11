package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import exception.CategoriaNaoEncontradaException;
import modelo.Categoria;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private List<Categoria> objetos = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "categorias.json";

    public CategoriaDAO() {
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
                objetos = mapper.readValue(arquivo, new TypeReference<List<Categoria>>(){});
            }
        } catch (Exception e) { objetos = new ArrayList<>(); }
    }

    public void inserir(Categoria obj) {
        int novoId = objetos.stream()
                .mapToInt(Categoria::getId)
                .max()
                .orElse(0) + 1;
        obj.setId(novoId);
        objetos.add(obj);
        salvar();
    }

    public List<Categoria> listar() { return objetos; }

    public Categoria listarId(int id) {
        return objetos.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new CategoriaNaoEncontradaException(
                        "Categoria com ID " + id + " não encontrada."
                ));
    }

    public void atualizar(Categoria obj) {
        Categoria categoria = objetos.stream()
                .filter(c -> c.getId() == obj.getId())
                .findFirst()
                .orElseThrow(() -> new CategoriaNaoEncontradaException(
                        "Categoria com ID " + obj.getId() + " não encontrada."
                ));

        categoria.setDescricao(obj.getDescricao());
        salvar();
    }

    public void excluir(Categoria obj) {
        boolean removido = objetos.removeIf(categoria -> categoria.getId() == obj.getId());

        if (!removido) {
            throw new CategoriaNaoEncontradaException(
                    "Categoria com ID " + obj.getId() + " não encontrada."
            );
        }

        salvar();
    }
}
