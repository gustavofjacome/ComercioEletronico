package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import modelo.VendaItem;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VendaItemDAO {
    private List<VendaItem> objetos = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "vendas_itens.json";

    public VendaItemDAO() {
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
                objetos = mapper.readValue(arquivo, new TypeReference<List<VendaItem>>(){});
            }
        } catch (Exception e) { objetos = new ArrayList<>(); }
    }

    public void inserir(VendaItem obj) {
        int novoId = 1;
        for (VendaItem v : objetos) { if (v.getId() >= novoId) novoId = v.getId() + 1; }
        obj.setId(novoId);
        objetos.add(obj);
        salvar();
    }

    public List<VendaItem> listar() { return objetos; }

    public VendaItem listarId(int id) {
        for (VendaItem x : objetos) { if (x.getId() == id) return x; }
        return null;
    }

    public void atualizar(VendaItem obj) {
        for (VendaItem y : objetos) {
            if (y.getId() == obj.getId()) {
                y.setQtd(obj.getQtd());
                y.setPreco(obj.getPreco());
                y.setIdVenda(obj.getIdVenda());
                y.setIdProduto(obj.getIdProduto());
                salvar();
                break;
            }
        }
    }

    public void excluir(VendaItem obj) {
        for (VendaItem z : objetos) {
            if (z.getId() == obj.getId()) {
                objetos.remove(z);
                salvar();
                break;
            }
        }
    }
}