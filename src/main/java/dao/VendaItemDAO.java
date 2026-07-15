package dao;

import exception.VendaItemNaoEncontradoException;
import modelo.VendaItem;

public class VendaItemDAO extends AbstractDAO<VendaItem> {

    public VendaItemDAO() {
        super("vendas_itens.json", VendaItem.class);
    }

    @Override
    protected int getId(VendaItem obj) {
        return obj.getId();
    }

    @Override
    protected void setId(VendaItem obj, int id) {
        obj.setId(id);
    }

    @Override
    public VendaItem listarId(int id) {
        VendaItem v = super.listarId(id);
        if (v == null) {
            throw new VendaItemNaoEncontradoException("VendaItem com ID " + id + " nao encontrado.");
        }
        return v;
    }

    @Override
    public void atualizar(VendaItem obj) {
        if (!objetos.containsKey(obj.getId())) {
            throw new VendaItemNaoEncontradoException("VendaItem com ID " + obj.getId() + " nao encontrado.");
        }
        objetos.put(obj.getId(), obj);
        salvar();
    }

    @Override
    public void excluir(VendaItem obj) {
        if (objetos.remove(obj.getId()) == null) {
            throw new VendaItemNaoEncontradoException("VendaItem com ID " + obj.getId() + " nao encontrado.");
        }
        salvar();
    }
}
