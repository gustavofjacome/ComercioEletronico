package view;

import dao.CategoriaDAO;
import dao.ClienteDAO;
import dao.ProdutoDAO;
import modelo.Categoria;
import modelo.Cliente;
import modelo.Produto;
import java.util.List;

public class View {

    public static ClienteDAO clienteDAO = new ClienteDAO();
    public static CategoriaDAO categoriaDAO = new CategoriaDAO();
    public static ProdutoDAO produtoDAO = new ProdutoDAO();

    public static List<Cliente> clienteListar() {
        return clienteDAO.listar();
    }

    public static void clienteInserir(String nome, String email, String fone){
        Cliente novoCliente = new Cliente(0, nome, email, fone);
        clienteDAO.inserir(novoCliente);
    }

    public static void atualizarCliente(int id, String nome, String email, String fone){
        Cliente novoCliente = new Cliente(id , nome, email, fone);
        clienteDAO.atualizar(novoCliente);
    }

    public static void excluirCliente(int id){
        Cliente seraRemovido = null;
        for (Cliente x : clienteDAO.listar()){
            if (x.getId() == id){
                seraRemovido = x;
                break;
            }
        }
        clienteDAO.excluir(seraRemovido);
    }

    public static List<Categoria> categoriaListar(){
        return categoriaDAO.listar();
    }

    public static void categoriaInserir(String descricao){
        Categoria novaCategoria = new Categoria(0, descricao);
        categoriaDAO.inserir(novaCategoria);
    }

    public static void categoriaAtualizar(int id, String descricao){
        Categoria novaCategoria = new Categoria(id, descricao);
        categoriaDAO.atualizar(novaCategoria);
    }

    public static void categoriaExcluir(int id){
        Categoria seraRemovido = null;
        for (Categoria x : categoriaListar()){
            if (x.getId() == id){
                seraRemovido = x;
                break;
            }
        }
        categoriaDAO.excluir(seraRemovido);
    }

    public static List<Produto> produtoListar(){
        return produtoDAO.listar();
    }

    public static void produtoInserir(String descricao, double preco, int estoque, int idCategoria){
        Produto novoProduto = new Produto(0, descricao, preco, estoque);
        novoProduto.setIdCategoria(idCategoria);
        produtoDAO.inserir(novoProduto);
    }

    public static void produtoAtualizar(int id, String descricao, double preco, int estoque, int idCategoria){
        Produto novoProduto = new Produto(id, descricao, preco, estoque);
        novoProduto.setIdCategoria(idCategoria);
        produtoDAO.atualizar(novoProduto);
    }

    public static void produtoExcluir(int id){
        Produto seraRemovido = null;
        for (Produto x : produtoDAO.listar()){
            if (x.getId() == id){
                seraRemovido = x;
                break;
            }
        }
        produtoDAO.excluir(seraRemovido);
    }

    public static void produtoReajustar(double percentual){
        //aplicável a todos os produtos
        for (Produto x : produtoDAO.listar()){
            x.setPreco(x.getPreco() * (1 + (percentual/100)));
            produtoDAO.atualizar(x);
        }

    }

}