package bean;

import dao.CategoriaDAO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import modelo.Categoria;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class CategoriaBean implements Serializable {

    private CategoriaDAO dao = new CategoriaDAO();
    private Categoria categoria = new Categoria();

    public Categoria getCategoria(){
        return categoria;
    }
    public void setCategoria(Categoria categoria){
        this.categoria = categoria;
    }
    public List<Categoria> getLista(){return dao.listar();}

    public String salvar(){
        if (categoria.getId() == 0){
            dao.inserir(categoria);
        } else {
            dao.atualizar(categoria);
        }
        categoria = new Categoria();
        return "listar?faces-redirect=true";
    }
    public String excluir(Categoria c){
        dao.excluir(c);
        return "listar?faces-redirect=true";
    }
    public String editar(Categoria c){
        this.categoria = c;
        return "form?faces-redirect=true";
    }
}
