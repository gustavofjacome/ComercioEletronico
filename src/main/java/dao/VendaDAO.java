package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import modelo.Venda;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {
    private List<Venda> objetos = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "vendas.json";

    public VendaDAO() {
        mapper.registerModule(new JavaTimeModule()); // Obrigatório para LocalDateTime
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
                objetos = mapper.readValue(arquivo, new TypeReference<List<Venda>>(){});
            }
        } catch (Exception e) { objetos = new ArrayList<>(); }
    }

    public void inserir(Venda obj) {
        int novoId = 1;
        for (Venda v : objetos) { if (v.getId() >= novoId) novoId = v.getId() + 1; }
        obj.setId(novoId);
        objetos.add(obj);
        salvar();
    }

    public List<Venda> listar() { return objetos; }

    public Venda listarId(int id) {
        for (Venda x : objetos) { if (x.getId() == id) return x; }
        return null;
    }

    public void atualizar(Venda obj) {
        for (Venda y : objetos) {
            if (y.getId() == obj.getId()) {
                y.setDate(obj.getDate());
                y.setCarrinho(obj.isCarrinho());
                y.setTotal(obj.getTotal());
                y.setIdCliente(obj.getIdCliente());
                salvar();
                break;
            }
        }
    }

    public void excluir(Venda obj) {
        for (Venda z : objetos) {
            if (z.getId() == obj.getId()) {
                objetos.remove(z);
                salvar();
                break;
            }
        }
    }
}