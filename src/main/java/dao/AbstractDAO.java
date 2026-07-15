package dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDAO<T> implements DAO<T> {

    protected Map<Integer, T> objetos = new HashMap<>();
    protected int nextId = 1;
    protected ObjectMapper mapper = new ObjectMapper();
    protected final String ARQUIVO;
    private final Class<T> entityClass;

    public AbstractDAO(String arquivo, Class<T> entityClass) {
        this.ARQUIVO = arquivo;
        this.entityClass = entityClass;
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        configurarMapper(mapper);
        abrir();
    }

    protected void configurarMapper(ObjectMapper mapper) {
    }

    protected abstract int getId(T obj);

    protected abstract void setId(T obj, int id);

    @Override
    public void salvar() {
        try {
            mapper.writeValue(new File(ARQUIVO), new ArrayList<>(objetos.values()));
        } catch (Exception e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    @Override
    public void abrir() {
        try {
            File arquivo = new File(ARQUIVO);
            if (arquivo.exists()) {
                List<T> lista = mapper.readValue(
                    arquivo,
                    mapper.getTypeFactory().constructCollectionType(List.class, entityClass)
                );
                objetos.clear();
                for (T obj : lista) {
                    objetos.put(getId(obj), obj);
                }
                nextId = objetos.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
            }
        } catch (Exception e) {
            objetos = new HashMap<>();
        }
    }

    @Override
    public void inserir(T obj) {
        int id = nextId++;
        setId(obj, id);
        objetos.put(id, obj);
        salvar();
    }

    @Override
    public List<T> listar() {
        return new ArrayList<>(objetos.values());
    }

    @Override
    public T listarId(int id) {
        T obj = objetos.get(id);
        if (obj == null) {
            throw new RuntimeException("ID " + id + " nao encontrado.");
        }
        return obj;
    }

    @Override
    public void atualizar(T obj) {
        int id = getId(obj);
        if (!objetos.containsKey(id)) {
            throw new RuntimeException("ID " + id + " nao encontrado.");
        }
        objetos.put(id, obj);
        salvar();
    }

    @Override
    public void excluir(T obj) {
        int id = getId(obj);
        if (objetos.remove(id) == null) {
            throw new RuntimeException("ID " + id + " nao encontrado.");
        }
        salvar();
    }
}
