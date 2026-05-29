package dao;

import modelo.Venda;

import java.util.ArrayList;
import java.util.List;

public class VendaDAO {
    private List<Venda> objetos = new ArrayList<>();

    public void inserir(Venda obj){
        objetos.add(obj);
    }

    public List<Venda> listar(){
        return objetos;
    }

    public Venda listarID(int id){
        for (Venda x : objetos) {
            if (x.getId() == id){
                return x;
            }
        }
        return null;
    }

    public void atualizar(Venda obj){
        for (Venda x : objetos){
            if (x.getId() == obj.getId()){
                x.setDate(obj.getDate());
                x.setTotal(obj.getTotal());
                x.setCarrinho(obj.isCarrinho());
                x.setIdCliente(obj.getIdCliente());
            }
        }
    }

    public void excluir(Venda obj){
        for (Venda x : objetos){
            if (x.getId() == obj.getId()){
                objetos.remove(x);
                break;
            }
        }
    }
}
