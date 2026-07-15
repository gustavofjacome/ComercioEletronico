package dao;

import exception.CategoriaNaoEncontradaException;
import modelo.Categoria;

public class CategoriaDAO extends AbstractDAO<Categoria> {

    public CategoriaDAO() {
        super("categorias.json", Categoria.class);
    }

    @Override
    protected int getId(Categoria obj) {
        return obj.getId();
    }

    @Override
    protected void setId(Categoria obj, int id) {
        obj.setId(id);
    }

    @Override
    public Categoria listarId(int id) {
        Categoria c = super.listarId(id);
        if (c == null) {
            throw new CategoriaNaoEncontradaException("Categoria com ID " + id + " nao encontrada.");
        }
        return c;
    }

    @Override
    public void atualizar(Categoria obj) {
        if (!objetos.containsKey(obj.getId())) {
            throw new CategoriaNaoEncontradaException("Categoria com ID " + obj.getId() + " nao encontrada.");
        }
        objetos.put(obj.getId(), obj);
        salvar();
    }

    @Override
    public void excluir(Categoria obj) {
        if (objetos.remove(obj.getId()) == null) {
            throw new CategoriaNaoEncontradaException("Categoria com ID " + obj.getId() + " nao encontrada.");
        }
        salvar();
    }
}
