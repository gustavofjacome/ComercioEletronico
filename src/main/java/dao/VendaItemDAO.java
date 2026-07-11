package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import exception.VendaItemNaoEncontradoException;
import modelo.VendaItem;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VendaItemDAO implements DAO<VendaItem> {

    private List<VendaItem> objetos = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "vendas_itens.json";

    public VendaItemDAO() {
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
                objetos = mapper.readValue(arquivo, new TypeReference<List<VendaItem>>(){});
            }
        } catch (Exception e) { objetos = new ArrayList<>(); }
    }

    @Override
    public void inserir(VendaItem obj) {
        int novoId = objetos.stream()
                .mapToInt(VendaItem::getId)
                .max()
                .orElse(0) + 1;
        obj.setId(novoId);
        objetos.add(obj);
        salvar();
    }

    @Override
    public List<VendaItem> listar() { return objetos; }

    @Override
    public VendaItem listarId(int id) {
        return objetos.stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElseThrow(() -> new VendaItemNaoEncontradoException(
                        "VendaItem com ID " + id + " não encontrado."
                ));
    }

    @Override
    public void atualizar(VendaItem obj) {
        VendaItem item = objetos.stream()
                .filter(v -> v.getId() == obj.getId())
                .findFirst()
                .orElseThrow(() -> new VendaItemNaoEncontradoException(
                        "VendaItem com ID " + obj.getId() + " não encontrado."
                ));

        item.setQtd(obj.getQtd());
        item.setPreco(obj.getPreco());
        item.setIdVenda(obj.getIdVenda());
        item.setIdProduto(obj.getIdProduto());
        salvar();
    }

    @Override
    public void excluir(VendaItem obj) {
        if (!objetos.removeIf(item -> item.getId() == obj.getId())) {
            throw new VendaItemNaoEncontradoException(
                    "VendaItem com ID " + obj.getId() + " não encontrado."
            );
        }

        salvar();
    }
}
