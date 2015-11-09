package uam.grupoalemanha;

import java.sql.SQLException;
import java.util.Scanner;

/*
 * UAM - Grupo Alemanha - Projeto iClothe - 3 Semestre 2014
 * @author Paulo Ribeiro
 * 
 * @version 1.0
 * 
 * Classe MainVendedor - Classe de Gerenciamento dos Recursos dos usuários Tipo Vendedor
 */

public class MainVendedor {
	
    Usuario usuario         = new Usuario();
    Cliente cliente         = new Cliente();
    Imposto imposto         = new Imposto();
    Produto produto         = new Produto();
    ControleEstoque estoque = new ControleEstoque();
    Pedido pedido           = new Pedido();
    CupomFiscal cupom       = new CupomFiscal();

    /*
     * Tela principal da classe MainVendedor
     */
    public void TelaVendedor() throws SQLException {

        int opc = 1;
	Scanner scannerGerencia = new Scanner(System.in);
	String valorTela;
		
	while(opc != 0) {
            System.out.println("================================================");
            System.out.println(" iClothe - Modulo Vendedor                      ");
            System.out.println("================================================");
            System.out.println("1 - Cadastro de Clientes");
            System.out.println("2 - PEDIDOS"); 
            System.out.println("3 - CUPOM FISCAL");  
            System.out.println("0 - Sair do Sistema");	
            System.out.print("Qual a sua opção: ");
            valorTela = scannerGerencia.nextLine();

            opc = Integer.parseInt(valorTela);
			
            /*
             * Cadastro de Clientes
             */
            if(opc == 1) {
                cliente.menuCliente();
            }
			
            /*
             * Pedidos
             */
            if(opc == 2) {
                pedido.menuPedido();
            }
            
            /*
             * Cupom Fiscal
             */
            if(opc == 3) {
                cupom.menuCumpomFiscal();
            }
            
	}
	scannerGerencia.close();
	System.out.println("=============================");
	System.out.println("Sistema Finalizado! Obrigado!");
	System.out.println("=============================");
    }

}
