package uam.grupoalemanha;

import java.sql.SQLException;
import java.util.Scanner;

/*
 * UAM - Grupo Alemanha - Projeto iClothe - 3 Semestre 2014
 * @author Paulo Ribeiro
 * 
 * @version 1.0
 * 
 * Classe MainGerencia - Classe de Gerenciamento dos Recursos dos usuários Tipo Gerencia
 */

public class MainGerencia {
	
    Usuario usuario         = new Usuario();
    Cliente cliente         = new Cliente();
    Imposto imposto         = new Imposto();
    Produto produto         = new Produto();
    ControleEstoque estoque = new ControleEstoque();
    Pedido pedido           = new Pedido();
    CupomFiscal cupom       = new CupomFiscal();

    /*
     * Tela principal da classe MainGerencia
     */
    public void TelaGerencia() throws SQLException {

        int opc = 1;
	Scanner scannerGerencia = new Scanner(System.in);
	String valorTela;
		
	while(opc != 0) {
            System.out.println("================================================");
            System.out.println(" iClothe - Modulo Gerencia                      ");
            System.out.println("================================================");
            System.out.println("1 - Cadastro de Clientes");
            System.out.println("2 - Cadastro de Produtos");
            System.out.println("3 - Gestão de Estoques");   
            System.out.println("4 - PEDIDOS"); 
            System.out.println("5 - CUPOM FISCAL");  
            System.out.println("8 - Impostos - Lei 12741");	
            System.out.println("9 - Cadastro de Usuários");	
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
             * Cadastro de Produtos
             */
            if(opc == 2) {
                produto.menuProduto();
            }
		
            /*
             * Gestão de Estoques
             */
            if(opc == 3) {
                estoque.menuEstoque();
            }

            /*
             * Pedidos
             */
            if(opc == 4) {
                pedido.menuPedido();
            }
            
            /*
             * Cupom Fiscal
             */
            if(opc == 5) {
                cupom.menuCumpomFiscal();
            }
            
            
            /*
             * Cadastro de Impostos - Lei 12741
             */
            if(opc == 8) {
		imposto.menuImposto();
            }
			
            /*
             * Cadastro de Usuários
             */
            if(opc == 9) {
                usuario.menuUsuario();
            }
	}
	scannerGerencia.close();
	System.out.println("=============================");
	System.out.println("Sistema Finalizado! Obrigado!");
	System.out.println("=============================");
    }

}
