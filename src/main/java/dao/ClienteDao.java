package dao;

import modelo.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteDao {
    private List<Cliente> objetos = new ArrayList<>();

    public void inserir(Cliente obj) {
        objetos.add(obj);
    }

    public List<Cliente> listar(){
        return objetos;
    }

    public Cliente listarID(int id){
        for (Cliente x : objetos){
            if (id == x.getId()){
                return x;
            }
        }
        return null;
    }

    public void atualizar(Cliente obj){
        for (Cliente x : objetos){
            if (x.getId() == obj.getId()){
                x.setEmail(obj.getEmail());
                x.setNome(obj.getNome());
                x.setFone(obj.getFone());
            }
        }
    }
    public void excluir(Cliente obj){
        for (Cliente x : objetos){
            if (x.getId() == obj.getId()){
                objetos.remove(x);
                break;
            }
        }
    }


}
