package uam.grupoalemanha;

import java.sql.SQLException;
import java.util.Scanner;

/*
 * UAM - Grupo Alemanha - Projeto iClothe - 3 Semestre 2014
 * @author Paulo Ribeiro
 * 
 * @version 1.0
 * 
 * Classe MainEstoque - Classe de Gerenciamento dos Recursos dos usuários Tipo Estoque
 */

public class MainEstoque {
	
    Cliente cliente         = new Cliente();
    Imposto imposto         = new Imposto();
    Produto produto         = new Produto();
    ControleEstoque estoque = new ControleEstoque();


    /*
     * Tela principal da classe MainEstoque
     */
    public void TelaEstoque() throws SQLException {

        int opc = 1;
	Scanner scannerGerencia = new Scanner(System.in);
	String valorTela;
		
	while(opc != 0) {
            System.out.println("================================================");
            System.out.println(" iClothe - Modulo Estoque                       ");
            System.out.println("================================================");
            System.out.println("1 - Cadastro de Produtos");
            System.out.println("2 - Gestão de Estoques");   
            System.out.println("0 - Sair do Sistema");	
            System.out.print("Qual a sua opção: ");
            valorTela = scannerGerencia.nextLine();

            opc = Integer.parseInt(valorTela);
			
            /*
             * Cadastro de Produtos
             */
            if(opc == 1) {
                produto.menuProduto();
            }
		
            /*
             * Gestão de Estoques
             */
            if(opc == 2) {
                estoque.menuEstoque();
            }

	}
	scannerGerencia.close();
	System.out.println("=============================");
	System.out.println("Sistema Finalizado! Obrigado!");
	System.out.println("=============================");
    }

}
