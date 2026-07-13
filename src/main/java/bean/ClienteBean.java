package bean;

import dao.ClienteDAO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import modelo.Cliente;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class ClienteBean implements Serializable {

    private ClienteDAO dao = new ClienteDAO();
    private Cliente cliente = new Cliente();

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }


    public List<Cliente> getLista(){
        return dao.listar();
    }

    public String salvar(){
        if (cliente.getId() == 0){
            dao.inserir(cliente);
        } else {
            dao.atualizar(cliente);
        }
        cliente = new Cliente();
        return "listar?faces-redirect=true";
    }
    public String excluir(Cliente c){
        dao.excluir(c);
        return "listar?faces-redirect=true";
    }

    public String editar(Cliente c){
        this.cliente =c;
        return "form?faces-redirect=true";
    }
}
