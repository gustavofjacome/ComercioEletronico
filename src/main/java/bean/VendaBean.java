package bean;

import dao.ClienteDAO;
import dao.ProdutoDAO;
import dao.VendaDAO;
import dao.VendaItemDAO;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import modelo.*;
import modelo.pagamento.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class VendaBean implements Serializable {

    private ClienteDAO clienteDao = new ClienteDAO();
    private ProdutoDAO produtoDao = new ProdutoDAO();
    private VendaDAO vendaDao = new VendaDAO();
    private VendaItemDAO vendaItemDao = new VendaItemDAO();

    private Venda vendaAtual;
    private int idClienteSelecionado;
    private int idProdutoSelecionado;
    private int quantidade = 1;
    private String tipoPagamentoSelecionado = "Boleto";
    private int parcelas = 1;

    public List<Cliente> getClientes() { return clienteDao.listar(); }
    public List<Produto> getProdutos() { return produtoDao.listar(); }

    public String iniciarCarrinho() {
        vendaAtual = new Venda();
        vendaAtual.setIdCliente(idClienteSelecionado);
        vendaAtual.setDate(LocalDateTime.now());
        vendaAtual.setCarrinho(true);
        vendaAtual.setTotal(0);
        vendaDao.inserir(vendaAtual);

        return null;
    }

    public String adicionarItem() {
        Produto p = produtoDao.listarId(idProdutoSelecionado);

        VendaItem item = new VendaItem();
        item.setIdVenda(vendaAtual.getId());
        item.setIdProduto(p.getId());
        item.setQtd(quantidade);
        item.setPreco(p.getPreco());

        vendaItemDao.inserir(item);
        quantidade = 1;
        return null;
    }

    public List<VendaItem> getItensCarrinho() {
        if (vendaAtual == null) return List.of();
        return vendaItemDao.listar().stream()
                .filter(i -> i.getIdVenda() == vendaAtual.getId())
                .collect(Collectors.toList());
    }

    public String nomeProduto(int idProduto) {
        try {
            return produtoDao.listarId(idProduto).getDescricao();
        } catch (Exception e) {
            return "Produto removido";
        }
    }

    public double getTotalCarrinho() {
        return getItensCarrinho().stream()
                .mapToDouble(i -> i.getPreco() * i.getQtd())
                .sum();
    }

    public String removerItem(VendaItem item) {
        vendaItemDao.excluir(item);
        return null;
    }

    public String finalizarVenda() {
        FormaPagamento forma = criarFormaPagamento();

        double subtotal = getTotalCarrinho();
        double taxa = forma.calcularTaxa(subtotal);
        double totalFinal = subtotal + taxa;

        vendaAtual.setTotal(totalFinal);
        vendaAtual.setCarrinho(false);
        vendaAtual.setTipoPagamento(forma.getDescricao());
        vendaAtual.setParcelas(tipoPagamentoSelecionado.equals("Cartão") ? parcelas : 0);

        vendaDao.atualizar(vendaAtual);

        vendaAtual = null;
        return "listar?faces-redirect=true";
    }

    private FormaPagamento criarFormaPagamento() {
        return switch (tipoPagamentoSelecionado) {
            case "Cartão" -> new Cartao(parcelas);
            case "Pix" -> new Pix();
            default -> new Boleto();
        };
    }

    public List<Venda> getLista() { return vendaDao.listar(); }

    public ClienteDAO getClienteDao() {
        return clienteDao;
    }

    public void setClienteDao(ClienteDAO clienteDao) {
        this.clienteDao = clienteDao;
    }

    public ProdutoDAO getProdutoDao() {
        return produtoDao;
    }

    public void setProdutoDao(ProdutoDAO produtoDao) {
        this.produtoDao = produtoDao;
    }

    public VendaDAO getVendaDao() {
        return vendaDao;
    }

    public void setVendaDao(VendaDAO vendaDao) {
        this.vendaDao = vendaDao;
    }

    public VendaItemDAO getVendaItemDao() {
        return vendaItemDao;
    }

    public void setVendaItemDao(VendaItemDAO vendaItemDao) {
        this.vendaItemDao = vendaItemDao;
    }

    public Venda getVendaAtual() {
        return vendaAtual;
    }

    public void setVendaAtual(Venda vendaAtual) {
        this.vendaAtual = vendaAtual;
    }

    public int getIdClienteSelecionado() {
        return idClienteSelecionado;
    }

    public void setIdClienteSelecionado(int idClienteSelecionado) {
        this.idClienteSelecionado = idClienteSelecionado;
    }

    public int getIdProdutoSelecionado() {
        return idProdutoSelecionado;
    }

    public void setIdProdutoSelecionado(int idProdutoSelecionado) {
        this.idProdutoSelecionado = idProdutoSelecionado;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getTipoPagamentoSelecionado() {
        return tipoPagamentoSelecionado;
    }

    public void setTipoPagamentoSelecionado(String tipoPagamentoSelecionado) {
        this.tipoPagamentoSelecionado = tipoPagamentoSelecionado;
    }

    public int getParcelas() {
        return parcelas;
    }

    public void setParcelas(int parcelas) {
        this.parcelas = parcelas;
    }
}