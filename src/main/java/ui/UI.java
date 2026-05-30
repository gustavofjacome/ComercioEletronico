package ui;

import dao.CategoriaDAO;
import dao.ClienteDAO;
import dao.ProdutoDAO;
import modelo.Categoria;
import modelo.Cliente;
import modelo.Produto;

import java.util.List;
import java.util.Scanner;

/**
 * Classe principal responsável por fornecer a Interface de Linha de Comando (CLI) do sistema.
 * <p>Esta classe atua como a camada de visualização (View) e controle primário da aplicação.
 * Ela exibe menus interativos, captura a entrada de dados do utilizador através da consola
 * e delega as operações de negócio (CRUD) para as respetivas classes de acesso a dados (DAOs).</p>
 * @see dao.ClienteDAO
 * @see dao.ProdutoDAO
 * @see dao.CategoriaDAO
 */
public class UI {

    /**
     * Objeto global utilizado para capturar as entradas de texto e números digitadas
     * pelo utilizador no terminal (Standard Input).
     * @see java.util.Scanner
     */
    private static Scanner sc = new Scanner(System.in);

    /**
     * Instância do DAO responsável por intermediar as operações de persistência
     * relacionadas à entidade {@link Produto}.
     */
    private static ProdutoDAO produtoDAO = new ProdutoDAO();

    /**
     * Instância do DAO responsável por intermediar as operações de persistência
     * relacionadas à entidade {@link Cliente}.
     */
    private static ClienteDAO clienteDAO = new ClienteDAO();

    /**
     * Instância do DAO responsável por intermediar as operações de persistência
     * relacionadas à entidade {@link Categoria}.
     */
    private static CategoriaDAO categoriaDAO = new CategoriaDAO();

    /**
     * Ponto de entrada principal (Entry Point) da aplicação.
     * <p>Inicia um laço de repetição contínuo que exibe o menu principal e encaminha
     * a execução para os métodos de CRUD específicos com base na escolha do utilizador,
     * sendo encerrado apenas quando a opção 0 é selecionada.</p>
     * @param args Argumentos de linha de comando passados durante a inicialização do programa (não utilizados).
     * @see #menu()
     */
    public static void main(String[] args) {
        int opcao;
        do {
            opcao = menu();
            switch (opcao) {
                // Cliente
                case 1 -> clienteListar();
                case 2 -> clienteInserir();
                case 3 -> clienteAtualizar();
                case 4 -> clienteExcluir();

                // Categoria
                case 5 -> categoriaListar();
                case 6 -> categoriaInserir();
                case 7 -> categoriaAtualizar();
                case 8 -> categoriaExcluir();

                // Produto
                case 9 -> produtoListar();
                case 10 -> produtoInserir();
                case 11 -> produtoAtualizar();
                case 12 -> produtoExcluir();

                case 0 -> System.out.println("A encerrar o sistema...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    /**
     * Exibe o painel de opções formatado no terminal e captura a escolha do utilizador.
     * @return Um número inteiro correspondente à funcionalidade selecionada pelo utilizador.
     * @see Scanner#nextInt()
     */
    public static int menu() {
        System.out.println("\n===========================================");
        System.out.println("         SISTEMA DE E-COMMERCE             ");
        System.out.println("===========================================");
        System.out.println(" [ CLIENTES ]");
        System.out.println("  1 - Listar | 2 - Inserir | 3 - Atualizar | 4 - Excluir");
        System.out.println(" [ CATEGORIAS ]");
        System.out.println("  5 - Listar | 6 - Inserir | 7 - Atualizar | 8 - Excluir");
        System.out.println(" [ PRODUTOS ]");
        System.out.println("  9 - Listar | 10 - Inserir | 11 - Atualizar | 12 - Excluir");
        System.out.println("-------------------------------------------");
        System.out.println("  0 - Sair do Sistema");
        System.out.print("\nEscolha uma opção: ");

        return sc.nextInt();
    }

    // ==========================================
    // MÉTODOS PARA PRODUTO
    // ==========================================

    /**
     * Solicita ao {@link ProdutoDAO} a lista de todos os produtos cadastrados
     * e exibe-os sequencialmente na consola. Informa caso o catálogo esteja vazio.
     * @see ProdutoDAO#listar()
     */
    public static void produtoListar () {
        List<Produto> lista = produtoDAO.listar();

        if (lista.isEmpty()) {
            System.out.println("Não há produtos cadastrados no sistema.");
        } else {
            System.out.println("\n--- LISTA DE PRODUTOS ---");
            for (Produto p : lista) {
                System.out.println(p);
            }
        }
    }

    /**
     * Interage com o utilizador para recolher os dados necessários para criar um novo
     * registo de produto. Após a validação das entradas, repassa o objeto para gravação no disco.
     * @see ProdutoDAO#inserir(Produto)
     */
    public static void produtoInserir () {
        System.out.println("\n--- INSERIR NOVO PRODUTO ---");
        sc.nextLine();

        System.out.print("Digite a descrição: ");
        String descricao = sc.nextLine();

        System.out.print("Digite o preço: ");
        double preco = sc.nextDouble();

        System.out.print("Digite a quantidade em estoque: ");
        int estoque = sc.nextInt();

        System.out.print("Digite o ID da Categoria deste produto: ");
        int idCategoria = sc.nextInt();
        sc.nextLine();

        Produto novoProduto = new Produto(0, descricao, preco, estoque);
        novoProduto.setIdCategoria(idCategoria);
        produtoDAO.inserir(novoProduto);

        System.out.println("Produto inserido com sucesso!");
    }

    /**
     * Solicita o ID de um produto existente, recolhe as novas informações através
     * da consola e envia o objeto modificado para ser atualizado pelo DAO correspondente.
     * @see ProdutoDAO#listarId(int)
     * @see ProdutoDAO#atualizar(Produto)
     */
    public static void produtoAtualizar () {
        System.out.println("\n--- ATUALIZAR PRODUTO ---");
        System.out.print("Digite o ID do produto que deseja atualizar: ");
        int id = sc.nextInt();
        sc.nextLine();

        Produto p = produtoDAO.listarId(id);

        if (p == null) {
            System.out.println("Produto não encontrado!");
        } else {
            System.out.print("Digite a nova descrição: ");
            String descricao = sc.nextLine();

            System.out.print("Digite o novo preço: ");
            double preco = sc.nextDouble();

            System.out.print("Digite a nova quantidade em estoque: ");
            int estoque = sc.nextInt();

            System.out.print("Digite o novo ID da Categoria: ");
            int idCategoria = sc.nextInt();
            sc.nextLine();

            Produto produtoModificado = new Produto(id, descricao, preco, estoque);
            produtoModificado.setIdCategoria(idCategoria);

            produtoDAO.atualizar(produtoModificado);
            System.out.println("Produto atualizado com sucesso!");
        }
    }

    /**
     * Solicita o ID de um produto alvo, localiza-o na base de dados e instrui
     * a camada de persistência a efetuar a sua remoção definitiva.
     * @see ProdutoDAO#excluir(Produto)
     */
    public static void produtoExcluir () {
        System.out.println("\n--- EXCLUIR PRODUTO ---");
        System.out.print("Digite o ID do produto que deseja excluir: ");
        int id = sc.nextInt();

        Produto p = produtoDAO.listarId(id);

        if (p == null) {
            System.out.println("Produto não encontrado!");
        } else {
            produtoDAO.excluir(p);
            System.out.println("Produto excluído com sucesso!");
        }
    }

    // ==========================================
    // MÉTODOS PARA CLIENTE
    // ==========================================

    /**
     * Solicita ao {@link ClienteDAO} a lista de todos os clientes cadastrados
     * e exibe as suas informações tabulares na consola.
     * @see ClienteDAO#listar()
     */
    public static void clienteListar () {
        List<Cliente> lista = clienteDAO.listar();
        if (lista.isEmpty()) {
            System.out.println("Não há clientes cadastrados no sistema.");
        } else {
            System.out.println("\n--- LISTA DE CLIENTES ---");
            for (Cliente c : lista) {
                System.out.println(c);
            }
        }
    }

    /**
     * Conduz um formulário via consola para a criação de um novo registo de cliente,
     * enviando o novo modelo preenchido para o sistema de persistência.
     * @see ClienteDAO#inserir(Cliente)
     */
    public static void clienteInserir () {
        System.out.println("\n--- INSERIR NOVO CLIENTE ---");
        sc.nextLine();

        System.out.print("Digite o nome: ");
        String nome = sc.nextLine();

        System.out.print("Digite o e-mail: ");
        String email = sc.nextLine();

        System.out.print("Digite o telefone: ");
        String fone = sc.nextLine();

        Cliente novoCliente = new Cliente(0, nome, email, fone);
        clienteDAO.inserir(novoCliente);

        System.out.println("Cliente inserido com sucesso!");
    }

    /**
     * Pede a identificação do cliente e, se encontrado, solicita novos dados cadastrais
     * para efetuar a sobrescrita das informações.
     * @see ClienteDAO#atualizar(Cliente)
     */
    public static void clienteAtualizar () {
        System.out.println("\n--- ATUALIZAR CLIENTE ---");
        System.out.print("Digite o ID do cliente que deseja atualizar: ");
        int id = sc.nextInt();
        sc.nextLine();

        Cliente c = clienteDAO.listarID(id);

        if (c == null) {
            System.out.println("Cliente não encontrado!");
        } else {
            System.out.print("Digite o novo nome: ");
            String nome = sc.nextLine();

            System.out.print("Digite o novo e-mail: ");
            String email = sc.nextLine();

            System.out.print("Digite o novo telefone: ");
            String fone = sc.nextLine();

            Cliente clienteModificado = new Cliente(id, nome, email, fone);
            clienteDAO.atualizar(clienteModificado);
            System.out.println("Cliente atualizado com sucesso!");
        }
    }

    /**
     * Interage com o utilizador para obter o ID do cliente e executa a deleção do
     * registo correspondente na base de dados JSON.
     * @see ClienteDAO#excluir(Cliente)
     */
    public static void clienteExcluir () {
        System.out.println("\n--- EXCLUIR CLIENTE ---");
        System.out.print("Digite o ID do cliente que deseja excluir: ");
        int id = sc.nextInt();

        Cliente c = clienteDAO.listarID(id);

        if (c == null) {
            System.out.println("Cliente não encontrado!");
        } else {
            clienteDAO.excluir(c);
            System.out.println("Cliente excluído com sucesso!");
        }
    }

    // ==========================================
    // MÉTODOS PARA CATEGORIA
    // ==========================================

    /**
     * Imprime no ecrã do utilizador a listagem completa das categorias registadas
     * através da leitura realizada pelo {@link CategoriaDAO}.
     * @see CategoriaDAO#listar()
     */
    public static void categoriaListar () {
        List<Categoria> lista = categoriaDAO.listar();
        if (lista.isEmpty()) {
            System.out.println("Não há categorias cadastradas no sistema.");
        } else {
            System.out.println("\n--- LISTA DE CATEGORIAS ---");
            for (Categoria c : lista) {
                System.out.println(c);
            }
        }
    }

    /**
     * Solicita uma breve descrição para compor uma nova classificação e a adiciona
     * à lista persistida no ficheiro JSON de categorias.
     * @see CategoriaDAO#inserir(Categoria)
     */
    public static void categoriaInserir () {
        System.out.println("\n--- INSERIR NOVA CATEGORIA ---");
        sc.nextLine();

        System.out.print("Digite a descrição: ");
        String descricao = sc.nextLine();

        Categoria novaCategoria = new Categoria(0, descricao);
        categoriaDAO.inserir(novaCategoria);

        System.out.println("Categoria inserida com sucesso!");
    }

    /**
     * Promove a alteração da nomenclatura (descrição) de uma categoria previamente
     * cadastrada, com base no seu identificador primário.
     * @see CategoriaDAO#atualizar(Categoria)
     */
    public static void categoriaAtualizar () {
        System.out.println("\n--- ATUALIZAR CATEGORIA ---");
        System.out.print("Digite o ID da categoria que deseja atualizar: ");
        int id = sc.nextInt();
        sc.nextLine();

        Categoria c = categoriaDAO.listarId(id);

        if (c == null) {
            System.out.println("Categoria não encontrada!");
        } else {
            System.out.print("Digite a nova descrição: ");
            String descricao = sc.nextLine();

            Categoria categoriaModificada = new Categoria(id, descricao);
            categoriaDAO.atualizar(categoriaModificada);
            System.out.println("Categoria atualizada com sucesso!");
        }
    }

    /**
     * Exclui uma categoria da base de dados e do ficheiro JSON, solicitando o ID
     * da mesma via entrada do utilizador.
     * @see CategoriaDAO#excluir(Categoria)
     */
    public static void categoriaExcluir () {
        System.out.println("\n--- EXCLUIR CATEGORIA ---");
        System.out.print("Digite o ID da categoria que deseja excluir: ");
        int id = sc.nextInt();

        Categoria c = categoriaDAO.listarId(id);

        if (c == null) {
            System.out.println("Categoria não encontrada!");
        } else {
            categoriaDAO.excluir(c);
            System.out.println("Categoria excluída com sucesso!");
        }
    }
}