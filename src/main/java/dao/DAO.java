package dao;

import java.util.List;

public interface DAO<T> {
    void inserir(T objeto);
    List<T> listar();
    void atualizar(T objeto);
    void excluir(T objeto);
    void abrir();
    void salvar();
    T listarId(int id);
}
