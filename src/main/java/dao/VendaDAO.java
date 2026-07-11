package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exception.VendaNaoEncontradaException;
import modelo.Venda;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VendaDAO {

    private List<Venda> objetos = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "vendas.json";

    public VendaDAO() {
        mapper.registerModule(new JavaTimeModule());
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
        int novoId = objetos.stream()
                .mapToInt(Venda::getId)
                .max()
                .orElse(0) + 1;
        obj.setId(novoId);
        objetos.add(obj);
        salvar();
    }

    public List<Venda> listar() { return objetos; }

    public Venda listarId(int id) {
        return objetos.stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElseThrow(() -> new VendaNaoEncontradaException(
                        "Venda com ID " + id + " não encontrada."
                ));
    }

    public void atualizar(Venda obj) {
        Venda venda = objetos.stream()
                .filter(v -> v.getId() == obj.getId())
                .findFirst()
                .orElseThrow(() -> new VendaNaoEncontradaException(
                        "Venda com ID " + obj.getId() + " não encontrada."
                ));

        venda.setDate(obj.getDate());
        venda.setCarrinho(obj.isCarrinho());
        venda.setTotal(obj.getTotal());
        venda.setIdCliente(obj.getIdCliente());
        salvar();
    }

    public void excluir(Venda obj) {
        if (!objetos.removeIf(venda -> venda.getId() == obj.getId())) {
            throw new VendaNaoEncontradaException(
                    "Venda com ID " + obj.getId() + " não encontrada."
            );
        }

        salvar();
    }
}
