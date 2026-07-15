package dao;

import exception.ClienteNaoEncontradoException;
import modelo.Cliente;

public class ClienteDAO extends AbstractDAO<Cliente> {

    public ClienteDAO() {
        super("clientes.json", Cliente.class);
    }

    @Override
    protected int getId(Cliente obj) {
        return obj.getId();
    }

    @Override
    protected void setId(Cliente obj, int id) {
        obj.setId(id);
    }

    @Override
    public Cliente listarId(int id) {
        Cliente c = super.listarId(id);
        if (c == null) {
            throw new ClienteNaoEncontradoException("Cliente com ID " + id + " nao encontrado.");
        }
        return c;
    }

    @Override
    public void atualizar(Cliente obj) {
        if (!objetos.containsKey(obj.getId())) {
            throw new ClienteNaoEncontradoException("Cliente com ID " + obj.getId() + " nao encontrado.");
        }
        objetos.put(obj.getId(), obj);
        salvar();
    }

    @Override
    public void excluir(Cliente obj) {
        if (objetos.remove(obj.getId()) == null) {
            throw new ClienteNaoEncontradoException("Cliente com ID " + obj.getId() + " nao encontrado.");
        }
        salvar();
    }
}
