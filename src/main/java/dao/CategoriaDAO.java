package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
        int novoId = 1;
        for (Categoria c : objetos) { if (c.getId() >= novoId) novoId = c.getId() + 1; }
        obj.setId(novoId);
        objetos.add(obj);
        salvar();
    }

    public List<Categoria> listar() { return objetos; }

    public Categoria listarId(int id) {
        for (Categoria x : objetos) { if (x.getId() == id) return x; }
        return null;
    }

    public void atualizar(Categoria obj) {
        for (Categoria y : objetos) {
            if (y.getId() == obj.getId()) {
                y.setDescricao(obj.getDescricao());
                salvar();
                break;
            }
        }
    }

    public void excluir(Categoria obj) {
        for (Categoria z : objetos) {
            if (z.getId() == obj.getId()) {
                objetos.remove(z);
                salvar();
                break;
            }
        }
    }
}