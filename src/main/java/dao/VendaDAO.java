package dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import exception.VendaNaoEncontradaException;
import modelo.Venda;

public class VendaDAO extends AbstractDAO<Venda> {

    public VendaDAO() {
        super("vendas.json", Venda.class);
    }

    @Override
    protected void configurarMapper(ObjectMapper mapper) {
        mapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected int getId(Venda obj) {
        return obj.getId();
    }

    @Override
    protected void setId(Venda obj, int id) {
        obj.setId(id);
    }

    @Override
    public Venda listarId(int id) {
        Venda v = super.listarId(id);
        if (v == null) {
            throw new VendaNaoEncontradaException("Venda com ID " + id + " nao encontrada.");
        }
        return v;
    }

    @Override
    public void atualizar(Venda obj) {
        if (!objetos.containsKey(obj.getId())) {
            throw new VendaNaoEncontradaException("Venda com ID " + obj.getId() + " nao encontrada.");
        }
        objetos.put(obj.getId(), obj);
        salvar();
    }

    @Override
    public void excluir(Venda obj) {
        if (objetos.remove(obj.getId()) == null) {
            throw new VendaNaoEncontradaException("Venda com ID " + obj.getId() + " nao encontrada.");
        }
        salvar();
    }
}
