package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import exception.VendaItemNaoEncontradoException;
import modelo.VendaItem;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class VendaItemDAO implements DAO<VendaItem> {

    private Map<Integer, VendaItem> objetos = new HashMap<>();
    private int nextId = 1;
    private ObjectMapper mapper = new ObjectMapper();
    private final String ARQUIVO = "vendas_itens.json";

    public VendaItemDAO() {
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
                List<VendaItem> lista = mapper.readValue(arquivo, new TypeReference<List<VendaItem>>() {});
                objetos.clear();
                for (VendaItem v : lista) {
                    objetos.put(v.id(), v);
                }
                nextId = lista.stream().mapToInt(VendaItem::id).max().orElse(0) + 1;
            }
        } catch (Exception e) { objetos = new HashMap<>(); }
    }

    @Override
    public void inserir(VendaItem obj) {
        int id = nextId++;
        objetos.put(id, new VendaItem(id, obj.qtd(), obj.preco(), obj.idVenda(), obj.idProduto()));
        salvar();
    }

    @Override
    public List<VendaItem> listar() { return new ArrayList<>(objetos.values()); }

    @Override
    public VendaItem listarId(int id) {
        VendaItem v = objetos.get(id);
        if (v == null) throw new VendaItemNaoEncontradoException("VendaItem com ID " + id + " não encontrado.");
        return v;
    }

    @Override
    public void atualizar(VendaItem obj) {
        if (!objetos.containsKey(obj.id()))
            throw new VendaItemNaoEncontradoException("VendaItem com ID " + obj.id() + " não encontrado.");
        objetos.put(obj.id(), obj);
        salvar();
    }

    @Override
    public void excluir(VendaItem obj) {
        if (objetos.remove(obj.id()) == null)
            throw new VendaItemNaoEncontradoException("VendaItem com ID " + obj.id() + " não encontrado.");
        salvar();
    }
}
