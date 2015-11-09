package uam.grupoalemanha;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/*
 * UAM - Grupo Alemanha - Projeto iClothe - 3 Semestre - 2014
 * @author Paulo Ribeiro
 * 
 * @version 1.0
 * 
 * Classe Cupom Fiscal - Emissao e Controle de Cupons Fiscais
 */

public class CupomFiscal {
    
    public void menuCumpomFiscal() throws SQLException {
        
        int opc = 1;
		
	while (opc != 0) {

            System.out.println("================================================");
            System.out.println(" iClothe - Cupom Fiscal                         ");
            System.out.println("================================================");
            System.out.println("1 - Lista Pedidos");
            System.out.println("2 - Emitir Cupom");
            System.out.println("3 - Cupons emitidos");
            System.out.println("4 - Re-imprimir Cupom");
            System.out.println("0 - Voltar");
            System.out.print("Qual a sua opção: ");
	
            Scanner scanner = new Scanner(System.in);

            String valorTela = scanner.nextLine();
            opc = Integer.parseInt(valorTela);
            String stringDeConexao      = "jdbc:sqlite:db/iclothe.db";
            Connection conexao; 
            Pedido pedido               = new Pedido();
            Cliente cliente             = new Cliente();
            boolean pedidoEncontrado    = false;
            int idCupom                 = 0;
            boolean cupomEncontrado     = false;


            if (opc == 1) {
                /*
                lista os Pedidos que estão prontos para faturamento
                */
                conexao = DriverManager.getConnection(stringDeConexao);
        
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

            if (opc == 2) {
                /*
                emite o cupom Fiscal
                */
                pedidoEncontrado = false;
                System.out.print("Qual o número do Pedido? ");
                pedido.setId(Integer.parseInt(scanner.nextLine()));
                
                conexao = DriverManager.getConnection(stringDeConexao);
                try {
                    PreparedStatement comando = conexao.prepareStatement("SELECT ID_PEDIDO, "
                    + "ID_CLIENTE, VALOR, IMPOSTOS, STATUS FROM PEDIDOS WHERE ID_PEDIDO = ?");
                    comando.setInt(1, pedido.getId());
                    ResultSet linhas = comando.executeQuery();

                    if (linhas.next()) {
                        pedido.setId(linhas.getInt("ID_PEDIDO"));
                        pedido.setIdCliente(linhas.getInt("ID_CLIENTE"));
                        pedido.setValorTotal(linhas.getDouble("VALOR"));
                        pedido.setValorImpostos(linhas.getDouble("IMPOSTOS"));
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
                Se o Pedido foi encontrado, emite o Cupom
                */
                if(pedidoEncontrado) {
                    /*
                    Determina o ID do CupomFiscal
                    */
                    conexao = DriverManager.getConnection(stringDeConexao);
                    try {
                        PreparedStatement comando = conexao.prepareStatement("SELECT MAX(ID_CUPOM) AS ID_CUPOM FROM PEDIDOS");
                        ResultSet linhas = comando.executeQuery();

                        if (linhas.next()) {
                            idCupom = linhas.getInt("ID_CUPOM");
                            idCupom = idCupom + 1;
                        }else{
                            idCupom = 1;
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    conexao.close();
                    
                    /*
                    Imprime o Cupom Fiscal
                    */
                    System.out.println("");
                    System.out.println("");
                    
                    System.out.println("=========================================================================");
                    System.out.println("LOJA DE ROUPAS - iClothe");
                    System.out.println("Rua Casa do Ator, 500 - Vila Olimpia - Sao Paulo - SP");
                    System.out.println("CNPJ: 99.999.999/0001.99");
                    System.out.println("IE: 999.999.999");
                    System.out.println("IM: 99.999.999");
                    System.out.println("-------------------------------------------------------------------------");
                    System.out.println("Cliente:" + cliente.getNome());
                    System.out.println("-------------------------------------------------------------------------");
                    System.out.println("                      C U P O M  F I S C A L                             ");
                    System.out.println("-------------------------------------------------------------------------");
                    /*
                    Aqui mostra os itens do Cupom
                    */
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
                    System.out.println("-------------------------------------------------------------------------");
                    System.out.println("TOTAL   R$                                                     " + pedido.getValorTotal());
                    System.out.println("");
                    System.out.println("Impostos (aproximado) R$ " + pedido.getValorImpostos());
                    System.out.println("");
                    System.out.println("                       Fonte: IBPT/FECOMERCIO                            ");
                    System.out.println("-------------------------------------------------------------------------");
               
                    
                    /*
                    Atualiza o Pedido
                    */
                    conexao = DriverManager.getConnection(stringDeConexao);
                    try {
                        PreparedStatement comando = conexao.prepareStatement("UPDATE PEDIDOS SET ID_CUPOM = ?,"
                                + "STATUS = ? WHERE ID_PEDIDO = ?");
                        comando.setInt(1, idCupom);
                        comando.setString(2, "FATURADO");
                        comando.setInt(3, pedido.getId());
                        comando.execute();

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    conexao.close();
        
                }
 

            }
            
            if (opc == 3) {
                /*
                lista os Cupons emitidos
                */
                 /*
                lista os Pedidos que estão prontos para faturamento
                */
                conexao = DriverManager.getConnection(stringDeConexao);
        
                try {
                    PreparedStatement comando = conexao.prepareStatement("SELECT ID_PEDIDO, ID_CUPOM, ID_CLIENTE, NOME ,VALOR ,IMPOSTOS,"
                        + " STATUS FROM PEDIDOS INNER JOIN CLIENTES ON PEDIDOS.ID_CLIENTE = CLIENTES.ID WHERE STATUS = ?");
                    comando.setString(1, "FATURADO");
                    ResultSet resultado = comando.executeQuery();
                    System.out.println("===================================================================================");
                    System.out.println("Faturamento                                                                        ");
                    System.out.println("-----------------------------------------------------------------------------------");
                    System.out.println("CUPOM CLIENTE                                VALOR    IMPOSTOS       ");
                    System.out.println("-----------------------------------------------------------------------------------");

                    while (resultado.next()) {
                    /*
                    * Caracteres de Formatação - Ex: %-15s - 15 posições, alinhado a esquerda
                    */
                        System.out.printf("%-6s"	, resultado.getInt("ID_CUPOM"));
                        System.out.printf("%-40s"	, resultado.getString("NOME"));
                        System.out.printf("%-10s"	, resultado.getDouble("VALOR"));
                        System.out.printf("%-10s"	, resultado.getDouble("IMPOSTOS"));
                        System.out.println("");
                    }
                    System.out.println("-----------------------------------------------------------------------------------");

                } catch (Exception e) {
                   e.printStackTrace();
                } finally {
                    conexao.close();
                }
                
                
            }
            
            if (opc == 4) {
                /*
                Imprime o espelho do CUPOM
                */
                cupomEncontrado = false;
                System.out.print("Qual o número do CUPOM? ");
                idCupom = Integer.parseInt(scanner.nextLine());
                conexao = DriverManager.getConnection(stringDeConexao);
                try {
                    PreparedStatement comando = conexao.prepareStatement("SELECT ID_PEDIDO, "
                    + "ID_CLIENTE, VALOR, IMPOSTOS, STATUS FROM PEDIDOS WHERE ID_CUPOM = ?");
                    comando.setInt(1, idCupom);
                    ResultSet linhas = comando.executeQuery();

                    if (linhas.next()) {
                        pedido.setId(linhas.getInt("ID_PEDIDO"));
                        pedido.setIdCliente(linhas.getInt("ID_CLIENTE"));
                        pedido.setValorTotal(linhas.getDouble("VALOR"));
                        pedido.setValorImpostos(linhas.getDouble("IMPOSTOS"));
                        pedido.setStatus(linhas.getString("STATUS"));
                        pedidoEncontrado           = true;
                        cliente.setNome(cliente.getNomeCliente(pedido.getIdCliente()));
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                conexao.close();
                
                System.out.println("");
                System.out.println("");
                   
                System.out.println("=========================================================================");
                System.out.println("LOJA DE ROUPAS - iClothe");
                System.out.println("Rua Casa do Ator, 500 - Vila Olimpia - Sao Paulo - SP");
                System.out.println("CNPJ: 99.999.999/0001.99");
                System.out.println("IE: 999.999.999");
                System.out.println("IM: 99.999.999");
                System.out.println("-------------------------------------------------------------------------");
                System.out.println("Cliente:" + cliente.getNome());
                System.out.println("-------------------------------------------------------------------------");
                System.out.println("                      C U P O M  F I S C A L                             ");
                System.out.println("-------------------------------------------------------------------------");
                /*
                Aqui mostra os itens do Cupom
                */
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
                System.out.println("-------------------------------------------------------------------------");
                System.out.println("TOTAL   R$                                                     " + pedido.getValorTotal());
                System.out.println("");
                System.out.println("Impostos (aproximado) R$ " + pedido.getValorImpostos());
                System.out.println("");
                System.out.println("                       Fonte: IBPT/FECOMERCIO                            ");
                System.out.println("-------------------------------------------------------------------------");

            }
	}
        
    }
    
}
