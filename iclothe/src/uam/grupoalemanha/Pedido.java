package uam.grupoalemanha;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Scanner;

/*
 * UAM - Grupo Alemanha - Projeto iClothe - 3 Semestre - 2014
 * @author Paulo Ribeiro
 * 
 * @version 1.0
 * 
 * Classe Pedido - Pedidos do iClothe
 */
public class Pedido {
    /*
    Atributos da Classe Pedido
    */
    private int id;
    private int idCliente;
    private double valorTotal;
    private double valorImpostos;
    private String status;
    
    /*
    Gets e Sets de cada atributo
    */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getValorImpostos() {
        return valorImpostos;
    }

    public void setValorImpostos(double valorImpostos) {
        this.valorImpostos = valorImpostos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    /*
    Atributos de uso nas Operações do Banco
     */
    boolean resultadoOperacao = false;
    String stringDeConexao = "jdbc:sqlite:db/iclothe.db";
    
    /*
    Método menuPedido
    */
    public void menuPedido() throws SQLException {
        int opc = 1;
		
	while (opc != 0) {

            System.out.println("================================================");
            System.out.println(" iClothe - Controle de Pedidos                  ");
            System.out.println("================================================");
            System.out.println("1 - Listar Pedidos");
            System.out.println("2 - Cadastrar Pedido");
            System.out.println("3 - Editar Pedido");
            System.out.println("0 - Voltar");
            System.out.print("Qual a sua opção: ");
	
            Scanner scanner = new Scanner(System.in);

            String valorTela = scanner.nextLine();
            opc = Integer.parseInt(valorTela);

            if (opc == 1) {
		/*
                Método listarPedidos()
                */
                listarPedidos();
             }

            if (opc == 2) {
		/*
                Método inserirPedido()
                */
                inserirPedido();
            }
            
            if (opc == 3) {
		/*
                Método editarPedido
                */
                editarPedido();
            }
	}
        
    }
    
    /*
    Método listarPedidos() - Lista todos os Pedidos que ainda não foram Faturados
    */
    public void listarPedidos() throws SQLException  {
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        
        try {
            PreparedStatement comando = conexao.prepareStatement("SELECT ID_PEDIDO, ID_CLIENTE, NOME ,VALOR ,IMPOSTOS,"
                    + " STATUS FROM PEDIDOS INNER JOIN CLIENTES ON PEDIDOS.ID_CLIENTE = CLIENTES.ID WHERE STATUS = ?");
            comando.setString(1, "PENDENTE");
            ResultSet resultado = comando.executeQuery();
            System.out.println("===================================================================================");
            System.out.println("Relação de Pedidos                                                                 ");
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println("PEDIDO CLIENTE                                VALOR    IMPOSTOS       STATUS    ");
            System.out.println("-----------------------------------------------------------------------------------");

            while (resultado.next()) {
                /*
                 * Caracteres de Formatação - Ex: %-15s - 15 posições, alinhado a esquerda
                 */
                System.out.printf("%-6s"	, resultado.getInt("ID_PEDIDO"));
                System.out.printf("%-40s"	, resultado.getString("NOME"));
                System.out.printf("%-10s"	, resultado.getDouble("VALOR"));
                System.out.printf("%-10s"	, resultado.getDouble("IMPOSTOS"));
                System.out.printf("%-10s"	, resultado.getString("STATUS"));
                System.out.println("");
            }
            System.out.println("-----------------------------------------------------------------------------------");

        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            conexao.close();
        }
  
    }
    
    /*
    Método inserirPedido() - Insere Pedido no Sistema
    */
    public void inserirPedido() throws SQLException {
        boolean clienteEncontrado   = false;
        int opc = 1;      
        
        Scanner scanner             = new Scanner(System.in);
        Cliente cliente             = new Cliente();
        Pedido pedido               = new Pedido();
        
        System.out.println("==========================================");
        System.out.println("NOVO Pedido                               ");
        System.out.println("------------------------------------------");
        
        /*
        Primeiro pesquisa o ID do Cliente pelo nome
        */
        while(!clienteEncontrado) {
            System.out.print("Quem é o cliente? ");
            cliente.setNome(scanner.nextLine());
            if(cliente.getIdCliente(cliente.getNome()) == 0) {
                System.out.println("O cliente " + cliente.getNome() + " não foi localizado. Tente novamente!");
            }else{
                clienteEncontrado = true;
            }
        }
        /*
        Define os atributos do pedido a ser inserido
        */
        cliente.setId(cliente.getIdCliente(cliente.getNome()));
        pedido.setIdCliente(cliente.getIdCliente(cliente.getNome()));
        pedido.setValorTotal(0.0);
        pedido.setValorImpostos(0.0);
        pedido.setStatus("PENDENTE");
        
        /*
        Insere o Pedido na tabela PEDIDOS
        */
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {
            PreparedStatement comando = conexao.prepareStatement("INSERT INTO PEDIDOS (ID_CLIENTE, VALOR,"
                    + "IMPOSTOS, STATUS) VALUES (?, ?, ?, ?)");
            comando.setInt(1, pedido.getIdCliente());
            comando.setDouble(2, pedido.getValorTotal());
            comando.setDouble(3, pedido.getValorImpostos());
            comando.setString(4, pedido.getStatus()); 
            comando.execute();
                
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        /*
        Pega o ID que foi inserido na tabela Pedidos
        */
        try {
            PreparedStatement comando = conexao.prepareStatement("SELECT MAX(ID_PEDIDO) AS ID_PEDIDO FROM PEDIDOS");
            ResultSet linhas = comando.executeQuery();

            if (linhas.next()) {
                pedido.setId(linhas.getInt("ID_PEDIDO"));
            }else{
                pedido.setId(0);
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        } 
        
        System.out.println("=========================================");
        System.out.println("Pedido criado com o numero:" + pedido.getId());
        System.out.println("Va em '3 - Editar Pedido' para continuar");
        System.out.println("=========================================");
        
        conexao.close();  

    }
    
    /*
    método editarPedido() - Efetua a edição dos Pedidos Cadastrados
    */
    public void editarPedido() throws SQLException {
        
        int opc                     = 1;
        boolean pedidoEncontrado    = false;
        Scanner scanner             = new Scanner(System.in);
        Cliente cliente             = new Cliente();
        Pedido pedido               = new Pedido();
        
        System.out.println("==========================================");
        System.out.println("Editar Pedido                              ");
        System.out.println("------------------------------------------");
        System.out.print("Qual o número do Pedido? ");
        pedido.setId(Integer.parseInt(scanner.nextLine()));
        
        /*
        Verifica se o pedido está cadastrado
        */
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        try {

            PreparedStatement comando = conexao.prepareStatement("SELECT ID_PEDIDO, "
                    + "ID_CLIENTE, VALOR, IMPOSTOS, STATUS FROM PEDIDOS WHERE ID_PEDIDO = ?");
            comando.setInt(1, pedido.getId());
            ResultSet linhas = comando.executeQuery();

            if (linhas.next()) {
                pedido.setId(linhas.getInt("ID_PEDIDO"));
                pedido.setIdCliente(linhas.getInt("ID_CLIENTE"));
                pedido.setValorTotal(linhas.getDouble("VALOR"));
                pedido.setValorTotal(linhas.getDouble("IMPOSTOS"));
                pedido.setStatus(linhas.getString("STATUS"));
                pedidoEncontrado           = true;
                cliente.setNome(cliente.getNomeCliente(pedido.getIdCliente()));
            }else{
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("Pedido " + pedido.getId() + " nao foi encontrado!");
                System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        conexao.close();
        
        /*
        Se o Pedido foi encontrado, parte para a ediçao
        */
        if(pedidoEncontrado) {
            
            while(opc != 0) {

                System.out.println("=========================================================================");
                System.out.println("          Pedido: " + pedido.getId());
                System.out.println("         Cliente: " + cliente.getId() + " - " + cliente.getNome());
                System.out.println("-------------------------------------------------------------------------");

                conexao = DriverManager.getConnection(stringDeConexao);
                try {
                    PreparedStatement comando = conexao.prepareStatement("SELECT ID_ITEM, ID_PRODUTO, DESCRICAO, "
                        + "QTDE_PRODUTO, V_UNITARIO, V_TOTAL FROM PEDIDOS_ITENS INNER JOIN PRODUTOS "
                        + "ON PEDIDOS_ITENS.ID_PRODUTO = PRODUTOS.ID WHERE ID_PEDIDO = ?");
                    comando.setInt(1, pedido.getId());
                    ResultSet resultado = comando.executeQuery();
                    System.out.println("ID  PRODUTO                                QTDE      UNITARIO      TOTAL ");
                    System.out.println("-------------------------------------------------------------------------");

                    while (resultado.next()) {
                        /*
                         * Caracteres de Formatação - Ex: %-15s - 15 posições, alinhado a esquerda
                        */
                        System.out.printf("%-4s"	, resultado.getInt("ID_ITEM"));
                        System.out.printf("%-4s"	, resultado.getInt("ID_PRODUTO"));
                        System.out.printf("%-40s"	, resultado.getString("DESCRICAO"));
                        System.out.printf("%-10s"	, resultado.getInt("QTDE_PRODUTO"));
                        System.out.printf("%-10s"	, resultado.getDouble("V_UNITARIO"));
                        System.out.printf("%-10s"	, resultado.getString("V_TOTAL"));
                        System.out.println("");
                    }
                    System.out.println("-------------------------------------------------------------------------");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                conexao.close();
                
                System.out.println("   Valor Total R$ " + pedido.getValorTotalPedido(pedido.getId()));
                System.out.println("Valor Impostos R$ " + pedido.getValorImpostosPedido(pedido.getId()));
                System.out.println("=========================================================================");
              
                System.out.println("1 - Inserir Produto");
                System.out.println("2 - Excluir Produto");
                System.out.println("0 - Sair");
                System.out.print("Qual a sua opção: ");
                String valorTela = scanner.nextLine();
                opc = Integer.parseInt(valorTela);
                
                if (opc == 1) {
                    /*
                    Insere um produto na tabela PEDIDOS_ITENS
                    */
                    boolean produtoEncontrado   = false;
                    int quantidade              = 0;
                    boolean quantidadeAceita    = false;
                    double valor                = 0;
                    double valorTotal           = 0;
                    double valorImposto         = 0;
                    int itemPedido              = 0;
                   
                    
                    Produto produto             = new Produto();
                    Imposto imposto             = new Imposto();
                    
                    /*
                    Verifica se o produto está cadastrado
                    */
                    while(!produtoEncontrado) {
                        System.out.print("Qual o ID do produto?"); 
                        produto.setId(Integer.parseInt(scanner.nextLine()));
                        if(produto.isProduto(produto.getId())) {
                            produtoEncontrado = true;
                        }else{
                            System.out.println("Produto " + produto.getId() + " não encontrado!");
                        }
                    }
                    
                    /*
                    Seta os atributos do produto selecionado
                    */
                    produto.setUnidade(produto.getUnidadeProduto(produto.getId()));
                    produto.setNcm(produto.getNCMProduto(produto.getId()));
                    produto.setQtde(produto.getEstoqueProduto(produto.getId()));
                    produto.setDescricao(produto.getDescricaoProduto(produto.getId()));
                    produto.setValorUnitario(produto.getValorUnitarioProduto(produto.getId()));
                    
                    System.out.println("Produto " + produto.getDescricao());
                    /*
                    Controla para que a quantidade do Pedido seja menor que a em estoque
                    */
                    while(!quantidadeAceita) {
                        System.out.println("Quantidade em estoque:" + produto.getQtde());
                        System.out.print("Quantidade do Pedido: ");
                        quantidade = Integer.parseInt(scanner.nextLine());
                        if(quantidade > produto.getQtde()) {
                            System.out.println("Quantidade do pedido maior que a do estoque!");
                        }else{
                            quantidadeAceita = true;
                        }
                    }
                    
                    System.out.println("Valor do Produto " + produto.getValorUnitario());
                    System.out.print("Novo valor: ");
                    valor = Double.parseDouble(scanner.nextLine());
                    
                    /*
                    Define qual será o ID do item dentro do Pedido
                    */

                    conexao = DriverManager.getConnection(stringDeConexao);
                    try {
                        PreparedStatement comando = conexao.prepareStatement("SELECT MAX(ID_ITEM) AS ID_ITEM FROM PEDIDOS_ITENS WHERE ID_PEDIDO = ?");
                        comando.setInt(1, pedido.getId());
                        ResultSet linhas = comando.executeQuery();

                        if (linhas.next()) {
                            itemPedido = linhas.getInt("ID_ITEM");
                            itemPedido = itemPedido + 1;
                        }else{
                            itemPedido = 1;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conexao.close();
                    
                    valorTotal = valor * quantidade;
                    /*
                    Faz o cálculo do imposto para o item
                    */
                    valorImposto = imposto.calculaImposto(valorTotal, produto.getNcm());

                    /*
                    Retira o produto em estoque (para nao ser utilizado em outros pedidos
                    */
                    produto.retirarEstoque(pedido.getId(), quantidade);
                    
                    /*
                    E insere o item na tabela PEDIDOS_ITENS
                    */
                    conexao = DriverManager.getConnection(stringDeConexao);
                    try {
                        PreparedStatement comando = conexao.prepareStatement("INSERT INTO PEDIDOS_ITENS "
                                + "(ID_PEDIDO, ID_ITEM, ID_PRODUTO, QTDE_PRODUTO, V_UNITARIO, V_TOTAL, V_IMPOSTOS) "
                                + "VALUES (?, ?, ?, ?, ?, ?,?)");
                        comando.setInt(1, pedido.getId());
                        comando.setInt(2, itemPedido);
                        comando.setInt(3, produto.getId());
                        comando.setInt(4, quantidade);
                        comando.setDouble(5, valor);
                        comando.setDouble(6, valorTotal);
                        comando.setDouble(7, valorImposto);
                        comando.execute();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conexao.close();
                    
                    /*
                    Totaliza o Pedido
                    */
                    pedido.totalizaPedido(pedido.getId());

                }
                
                if (opc == 2) {
                    /*
                    Apaga um item da tabela PEDIDOS_ITENS
                    */
                    int qtdeItem            = 0;
                    Produto produto         = new Produto();
                    int idItem              = 0;
                    double valorImposto     = 0;
                    double valorPedido      = 0;
                    
                    System.out.print("ID do item: ");
                    idItem = Integer.parseInt(scanner.nextLine()); 
                    
                    /*
                    Determina qual o ID do Produto e a quantidade do Pedido
                    */
                    conexao = DriverManager.getConnection(stringDeConexao);
                    try {
                        PreparedStatement comando = conexao.prepareStatement("SELECT ID_PRODUTO, QTDE_PRODUTO "
                        + "FROM PEDIDOS_ITENS WHERE ID_PEDIDO = ? AND ID_ITEM = ?");
                        comando.setInt(1, pedido.getId());
                        comando.setInt(2, idItem);
                        ResultSet resultado = comando.executeQuery();
                    
                        if(resultado.next()) {
                            produto.setId(resultado.getInt("ID_PRODUTO"));
                            qtdeItem = resultado.getInt("QTDE_PRODUTO");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conexao.close();
                    
                    /*
                    Retorna a quantidade em estoque
                    */
                    produto.inserirEstoque(produto.getId(), qtdeItem);
                    
                    /*
                    Apaga o produto na tabela PEDIDOS_ITENS
                    */
                    conexao = DriverManager.getConnection(stringDeConexao);
                    try {
                        PreparedStatement comando = conexao.prepareStatement("DELETE FROM PEDIDOS_ITENS "
                                + "WHERE ID_PEDIDO = ? AND ID_ITEM = ?");
                        comando.setInt(1, pedido.getId());
                        comando.setInt(2, idItem);
                        comando.execute();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    conexao.close();
                    
                    /*
                    Totaliza o Pedido
                    */
                    pedido.totalizaPedido(pedido.getId());
                     
                }

            }
  
        }
  
    }
    
    /*
    Método totalizaPedido() - Efetua a totalização do Pedido
    */
    public void totalizaPedido(int idPedido) throws SQLException {
        double valorPedido      = 0;
        double valorImposto     = 0;
        
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        /*
        Soma o valor total dos itens e dos impostos
        */
        try {
            PreparedStatement comando = conexao.prepareStatement("SELECT SUM(V_TOTAL) AS V_TOTAL, "
                + "SUM(V_IMPOSTOS) AS V_IMPOSTOS FROM PEDIDOS_ITENS WHERE ID_PEDIDO = ?");
            comando.setInt(1, idPedido);
            ResultSet resultado = comando.executeQuery();
                    
            if(resultado.next()) {
                valorPedido     = resultado.getDouble("V_TOTAL");
                valorImposto    = resultado.getDouble("V_IMPOSTOS");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        /*
        Atualiza a tabela PEDIDOS com os TOTAIS
        */
        try {
            PreparedStatement comando = conexao.prepareStatement("UPDATE PEDIDOS "
                + "SET VALOR = ?, IMPOSTOS = ? WHERE ID_PEDIDO = ?");
            comando.setDouble(1, valorPedido );
            comando.setDouble(2, valorImposto );
            comando.setInt(3, idPedido);
            comando.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
        conexao.close();
    
    }
    
    /*
    método getValorTotalPedido() - Retorna o valor total do Pedido
    */
    public double getValorTotalPedido(int idPedido) throws SQLException {
        double valorPedido      = 0;
        
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        /*
        Soma o valor total dos itens e dos impostos
        */
        try {
            PreparedStatement comando = conexao.prepareStatement("SELECT VALOR FROM PEDIDOS WHERE ID_PEDIDO = ?");
            comando.setInt(1, idPedido);
            ResultSet resultado = comando.executeQuery();
                    
            if(resultado.next()) {
                valorPedido     = resultado.getDouble("VALOR");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        conexao.close();
        return valorPedido;
    }
    
    /*
    método getValorImpostosPedido() - Retorna o valor total dos impostos do Pedido
    */
    public double getValorImpostosPedido(int idPedido) throws SQLException {
        double valorImpostos      = 0;
        
        Connection conexao = DriverManager.getConnection(stringDeConexao);
        /*
        Soma o valor total dos itens e dos impostos
        */
        try {
            PreparedStatement comando = conexao.prepareStatement("SELECT IMPOSTOS FROM PEDIDOS WHERE ID_PEDIDO =  ?");
            comando.setInt(1, idPedido);
            ResultSet resultado = comando.executeQuery();
                    
            if(resultado.next()) {
                valorImpostos     = resultado.getDouble("IMPOSTOS");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        conexao.close();
        return valorImpostos;
    }
    
    

}
