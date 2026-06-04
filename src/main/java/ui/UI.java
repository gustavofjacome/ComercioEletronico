package ui;

import modelo.Categoria;
import modelo.Cliente;
import modelo.Produto;
import view.View;

import java.util.List;
import java.util.Scanner;

/**
 * Classe principal responsável por fornecer a Interface de Linha de Comando (CLI) do sistema.
 * <p>Esta classe atua estritamente como a camada de Interface de Usuário (UI).
 * Ela exibe menus interativos, captura a entrada de dados do utilizador através da consola
 * e delega as operações de controle e lógica de negócio para a classe de controle central.</p>
 * @see view.View
 */
public class UI {

    /**
     * Objeto global utilizado para capturar as entradas de texto e números digitadas
     * pelo utilizador no terminal (Standard Input).
     * @see java.util.Scanner
     */
    private static Scanner sc = new Scanner(System.in);

    /**
     * Ponto de entrada principal (Entry Point) da aplicação.
     * <p>Inicia um laço de repetição contínuo que exibe o menu principal e encaminha
     * a execução para os métodos de interface específicos com base na escolha do utilizador,
     * sendo encerrado apenas quando a opção 0 é selecionada.</p>
     * @param args Argumentos de linha de comando passados durante a inicialização do programa (não utilizados).
     * @see #menu()
     */
    public static void main(String[] args) {
        int opcao;
        do {
            opcao = menu();
            switch (opcao) {

                case 1 -> clienteListar();
                case 2 -> clienteInserir();
                case 3 -> clienteAtualizar();
                case 4 -> clienteExcluir();

                case 5 -> categoriaListar();
                case 6 -> categoriaInserir();
                case 7 -> categoriaAtualizar();
                case 8 -> categoriaExcluir();

                case 9 -> produtoListar();
                case 10 -> produtoInserir();
                case 11 -> produtoAtualizar();
                case 12 -> produtoExcluir();
                case 13 -> produtoReajustar();

                case 0 -> System.out.println("A encerrar o sistema...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    /**
     * Exibe o painel de opções formatado no terminal e captura a escolha do utilizador,
     * tratando a limpeza do buffer do teclado logo em seguida.
     * @return Um número inteiro correspondente à funcionalidade selecionada pelo utilizador.
     * @see Scanner#nextInt()
     */
    public static int menu() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║           SISTEMA DE E-COMMERCE              ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║ CLIENTES                                     ║");
        System.out.println("║   1. Listar     2. Inserir                   ║");
        System.out.println("║   3. Atualizar  4. Excluir                   ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║ CATEGORIAS                                   ║");
        System.out.println("║   5. Listar     6. Inserir                   ║");
        System.out.println("║   7. Atualizar  8. Excluir                   ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║ PRODUTOS                                     ║");
        System.out.println("║   9. Listar     10. Inserir                  ║");
        System.out.println("║   11. Atualizar 12. Excluir                  ║");
        System.out.println("║   13. Reajustar preços em lote               ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║   0. Sair                                    ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.print("\n➜ Escolha uma opção: ");

        int op = sc.nextInt();
        sc.nextLine();
        return op;
    }

    // ==========================================
    // MÉTODOS PARA PRODUTO
    // ==========================================

    /**
     * Solicita à camada de controle a listagem de todos os produtos cadastrados
     * e os exibe no console. Informa caso o catálogo esteja vazio.
     * @see view.View#produtoListar()
     */
    public static void produtoListar () {
        List<Produto> lista = View.produtoListar();

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
     * Interage com o utilizador para recolher os dados necessários de um produto
     * via console e os encaminha de forma limpa para processamento na View.
     * @see view.View#produtoInserir(String, double, int, int)
     */
    public static void produtoInserir () {
        System.out.println("\n--- INSERIR NOVO PRODUTO ---");

        System.out.print("Digite a descrição: ");
        String descricao = sc.nextLine();

        System.out.print("Digite o preço: ");
        double preco = sc.nextDouble();

        System.out.print("Digite a quantidade em estoque: ");
        int estoque = sc.nextInt();

        System.out.print("Digite o ID da Categoria deste produto: ");
        int idCategoria = sc.nextInt();
        sc.nextLine();

        View.produtoInserir(descricao, preco, estoque, idCategoria);
        System.out.println("Produto inserido com sucesso!");
    }

    /**
     * Solicita o identificador de um produto, verifica sua existência local na lista
     * de visualização e, se encontrado, recolhe as novas propriedades repassando-as para a View.
     * @see view.View#produtoListar()
     * @see view.View#produtoAtualizar(int, String, double, int, int)
     */
    public static void produtoAtualizar () {
        System.out.println("\n--- ATUALIZAR PRODUTO ---");
        System.out.print("Digite o ID do produto que deseja atualizar: ");
        int id = sc.nextInt();
        sc.nextLine();

        Produto produtoExistente = null;
        for (Produto p : View.produtoListar()) {
            if (p.getId() == id) {
                produtoExistente = p;
                break;
            }
        }

        if (produtoExistente == null) {
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

            View.produtoAtualizar(id, descricao, preco, estoque, idCategoria);
            System.out.println("Produto atualizado com sucesso!");
        }
    }

    /**
     * Pede o ID de um produto alvo, valida se ele de fato existe no sistema
     * e instrui a classe View a realizar a sua exclusão definitiva.
     * @see view.View#produtoListar()
     * @see view.View#produtoExcluir(int)
     */
    public static void produtoExcluir () {
        System.out.println("\n--- EXCLUIR PRODUTO ---");
        System.out.print("Digite o ID do produto que deseja excluir: ");
        int id = sc.nextInt();
        sc.nextLine();

        Produto produtoExistente = null;
        for (Produto p : View.produtoListar()) {
            if (p.getId() == id) {
                produtoExistente = p;
                break;
            }
        }

        if (produtoExistente == null) {
            System.out.println("Produto não encontrado!");
        } else {
            View.produtoExcluir(id);
            System.out.println("Produto excluído com sucesso!");
        }
    }

    /**
     * Solicita um valor de taxa percentual na consola e dispara o comando
     * de reajuste global de preços em lote para todos os produtos ativos.
     * @see view.View#produtoReajustar(double)
     */
    public static void produtoReajustar() {
        System.out.println("\n--- REAJUSTAR PREÇO DE PRODUTOS ---");
        System.out.print("Digite o percentual de reajuste (ex: 10 para +10% ou -5 para -5%): ");
        double percentual = sc.nextDouble();
        sc.nextLine();

        View.produtoReajustar(percentual);
        System.out.println("Reajuste aplicado com sucesso a todos os produtos!");
    }

    // ==========================================
    // MÉTODOS PARA CLIENTE
    // ==========================================

    /**
     * Solicita à camada de controle a listagem de todos os clientes ativos
     * e renderiza seus dados de forma textual no console.
     * @see view.View#clienteListar()
     */
    public static void clienteListar () {
        List<Cliente> lista = View.clienteListar();
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
     * Captura os dados textuais de identificação de um novo cliente e os envia
     * encapsulados através de parâmetros de método para a camada View.
     * @see view.View#clienteInserir(String, String, String)
     */
    public static void clienteInserir () {
        System.out.println("\n--- INSERIR NOVO CLIENTE ---");

        System.out.print("Digite o nome: ");
        String nome = sc.nextLine();

        System.out.print("Digite o e-mail: ");
        String email = sc.nextLine();

        System.out.print("Digite o telefone: ");
        String fone = sc.nextLine();

        View.clienteInserir(nome, email, fone);
        System.out.println("Cliente inserido com sucesso!");
    }

    /**
     * Lê o ID do cliente, assegura sua existência com apoio das listagens da View
     * e recolhe as novas entradas textuais para processamento da modificação cadastral.
     * @see view.View#clienteListar()
     * @see view.View#atualizarCliente(int, String, String, String)
     */
    public static void clienteAtualizar () {
        System.out.println("\n--- ATUALIZAR CLIENTE ---");
        System.out.print("Digite o ID do cliente que deseja atualizar: ");
        int id = sc.nextInt();
        sc.nextLine();

        Cliente clienteExistente = null;
        for (Cliente c : View.clienteListar()) {
            if (c.getId() == id) {
                clienteExistente = c;
                break;
            }
        }

        if (clienteExistente == null) {
            System.out.println("Cliente não encontrado!");
        } else {
            System.out.print("Digite o novo nome: ");
            String nome = sc.nextLine();

            System.out.print("Digite o novo e-mail: ");
            String email = sc.nextLine();

            System.out.print("Digite o novo telefone: ");
            String fone = sc.nextLine();

            View.atualizarCliente(id, nome, email, fone);
            System.out.println("Cliente atualizado com sucesso!");
        }
    }

    /**
     * Interage com o terminal para obter o ID de exclusão do cliente e delega
     * o comando de remoção para a camada controladora correspondente.
     * @see view.View#clienteListar()
     * @see view.View#excluirCliente(int)
     */
    public static void clienteExcluir () {
        System.out.println("\n--- EXCLUIR CLIENTE ---");
        System.out.print("Digite o ID do cliente que deseja excluir: ");
        int id = sc.nextInt();
        sc.nextLine();

        Cliente clienteExistente = null;
        for (Cliente c : View.clienteListar()) {
            if (c.getId() == id) {
                clienteExistente = c;
                break;
            }
        }

        if (clienteExistente == null) {
            System.out.println("Cliente não encontrado!");
        } else {
            View.excluirCliente(id);
            System.out.println("Cliente excluído com sucesso!");
        }
    }

    // ==========================================
    // MÉTODOS PARA CATEGORIA
    // ==========================================

    /**
     * Resgata a coleção completa de categorias cadastradas na View
     * e faz a sua impressão sequencial formatada na tela do console.
     * @see view.View#categoriaListar()
     */
    public static void categoriaListar () {
        List<Categoria> lista = View.categoriaListar();
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
     * Lê uma string de descrição textual e invoca a operação de inserção
     * de uma nova classificação de produtos na View.
     * @see view.View#categoriaInserir(String)
     */
    public static void categoriaInserir () {
        System.out.println("\n--- INSERIR NOVA CATEGORIA ---");

        System.out.print("Digite a descrição: ");
        String descricao = sc.nextLine();

        View.categoriaInserir(descricao);
        System.out.println("Categoria inserida com sucesso!");
    }

    /**
     * Captura o identificador numérico de uma categoria, valida sua presença e
     * recolhe a nova nomenclatura, acionando o método de atualização da View.
     * @see view.View#categoriaListar()
     * @see view.View#categoriaAtualizar(int, String)
     */
    public static void categoriaAtualizar () {
        System.out.println("\n--- ATUALIZAR CATEGORIA ---");
        System.out.print("Digite o ID da categoria que deseja atualizar: ");
        int id = sc.nextInt();
        sc.nextLine();

        Categoria categoriaExistente = null;
        for (Categoria c : View.categoriaListar()) {
            if (c.getId() == id) {
                categoriaExistente = c;
                break;
            }
        }

        if (categoriaExistente == null) {
            System.out.println("Categoria não encontrada!");
        } else {
            System.out.print("Digite a nova descrição: ");
            String descricao = sc.nextLine();

            View.categoriaAtualizar(id, descricao);
            System.out.println("Categoria atualizada com sucesso!");
        }
    }

    /**
     * Coleta o ID primário de uma categoria via teclado e, se ela existir,
     * ordena à camada controladora a realização de sua exclusão lógica/física.
     * @see view.View#categoriaListar()
     * @see view.View#categoriaExcluir(int)
     */
    public static void categoriaExcluir () {
        System.out.println("\n--- EXCLUIR CATEGORIA ---");
        System.out.print("Digite o ID da categoria que deseja excluir: ");
        int id = sc.nextInt();
        sc.nextLine();

        Categoria categoriaExistente = null;
        for (Categoria c : View.categoriaListar()) {
            if (c.getId() == id) {
                categoriaExistente = c;
                break;
            }
        }

        if (categoriaExistente == null) {
            System.out.println("Categoria não encontrada!");
        } else {
            View.categoriaExcluir(id);
            System.out.println("Categoria excluída com sucesso!");
        }
    }
}