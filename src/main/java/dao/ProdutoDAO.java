package dao;

import exception.ProdutoNaoEncontradoException;
import modelo.Produto;

public class ProdutoDAO extends AbstractDAO<Produto> {

    public ProdutoDAO() {
        super("produtos.json", Produto.class);
    }

    @Override
    protected int getId(Produto obj) {
        return obj.getId();
    }

    @Override
    protected void setId(Produto obj, int id) {
        obj.setId(id);
    }

    @Override
    public Produto listarId(int id) {
        Produto p = super.listarId(id);
        if (p == null) {
            throw new ProdutoNaoEncontradoException("Produto com ID " + id + " nao encontrado.");
        }
        return p;
    }

    @Override
    public void atualizar(Produto obj) {
        if (!objetos.containsKey(obj.getId())) {
            throw new ProdutoNaoEncontradoException("Produto com ID " + obj.getId() + " nao encontrado.");
        }
        objetos.put(obj.getId(), obj);
        salvar();
    }

    @Override
    public void excluir(Produto obj) {
        if (objetos.remove(obj.getId()) == null) {
            throw new ProdutoNaoEncontradoException("Produto com ID " + obj.getId() + " nao encontrado.");
        }
        salvar();
    }
}
