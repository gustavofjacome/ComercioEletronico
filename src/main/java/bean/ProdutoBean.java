package bean;

import dao.CategoriaDAO;
import dao.ProdutoDAO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import modelo.Categoria;
import modelo.Produto;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class ProdutoBean implements Serializable {

    private ProdutoDAO dao = new ProdutoDAO();
    private Produto produto = new Produto();
    private CategoriaDAO categoriaDao = new CategoriaDAO();

    public Produto getProduto(){return produto;}
    public void setProduto(Produto p){this.produto = p;}
    public List<Produto> getLista(){return dao.listar();}
    public List<Categoria> getCategorias() {return categoriaDao.listar();}

    public String nomeCategoria(int idCategoria) {
        try {
            return categoriaDao.listarId(idCategoria).getDescricao();
        } catch (Exception e) {
            return "Categoria não encontrada";
        }
    }

    public String salvar(){
        if (produto.getId() == 0){
            dao.inserir(produto);
        } else {
            dao.atualizar(produto);
        }
        produto = new Produto();
        return "listar?faces-redirect=true";
    }
    public String novo() {
        produto = new Produto();
        return "form?faces-redirect=true";
    }
    public String excluir(Produto p){
        dao.excluir(p);
        return "listar?faces-redirect=true";
    }
    public String editar(Produto p){
        this.produto = p;
        return "form?faces-redirect=true";
    }
}
